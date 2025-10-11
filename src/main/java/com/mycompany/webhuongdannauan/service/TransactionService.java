package com.mycompany.webhuongdannauan.service;

import com.google.gson.JsonObject;
import com.mycompany.webhuongdannauan.config.MoMoConfig;
import com.mycompany.webhuongdannauan.dao.TransactionDAO;
import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.TransactionDAOImpl;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.model.PremiumPackage;
import com.mycompany.webhuongdannauan.model.PremiumAccount;
import com.mycompany.webhuongdannauan.model.Transaction;
import com.mycompany.webhuongdannauan.service.MoMoPaymentService;
import com.mycompany.webhuongdannauan.service.UserService;
import com.mycompany.webhuongdannauan.utils.MoMoSecurity;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    // Sử dụng GenericDAOImpl cho PremiumPackage vì chỉ cần CRUD cơ bản
    private final GenericDAOImpl<PremiumPackage, Long> packageDAO; 
    private final UserService userService; 
    private final PremiumAccountService premiumAccountService; // Tầng mới để quản lý PremiumAccount
    private final MoMoPaymentService momoService; // Khai báo MoMo Service

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl(); // Dùng DAO cụ thể cho truy vấn đặc thù
        this.packageDAO = new GenericDAOImpl<PremiumPackage, Long>() {}; // Khởi tạo Generic DAO
        this.userService = new UserService(); 
        this.premiumAccountService = new PremiumAccountService(); // Cần lớp Service này
        this.momoService = new MoMoPaymentService(); // Khởi tạo MoMo Service
    }

    
    //Xử lý giao dịch mua gói Premium.
    public String createMoMoPaymentRequest(Long userId, Long packageId) throws Exception {
        PremiumPackage pkg = packageDAO.findById(packageId); 
        User user = userService.findUserById(userId);

//        if (user == null || pkg == null) {
//            return null;
//        }
        
        // 1. Tạo orderId Unique cho MoMo
        long orderIdLong = userId + System.currentTimeMillis();
        String orderIdString = String.valueOf(orderIdLong); // Order ID kiểu chuỗi cho MoMo API

        String orderInfo = "Gói Premium: " + pkg.getName();

        // Dữ liệu bổ sung: Encode base64 chứa thông tin cần thiết khi MoMo trả về
        String extraDataJson = String.format("{\"userId\":%d, \"packageId\":%d}", userId, packageId);
        String extraData = Base64.getEncoder().encodeToString(extraDataJson.getBytes());

        // 2. Tạo bản ghi giao dịch (PENDING)
        Transaction transaction = new Transaction();

        // Nếu BaseEntity dùng ID tự tăng, CHỈ CẦN set các trường khác.
        // Nếu bạn muốn ID Transaction (Long) = OrderId (Long) của MoMo:
//        transaction.setId(orderIdLong); // Giả định BaseEntity.id là Long

        // LƯU MO MO ORDER ID VÀO TRƯỜNG RIÊNG (orderId - đã thêm vào Entity)
        transaction.setOrderId(orderIdString); 

        transaction.setUser(user);
        transaction.setPremiumPackage(pkg);
        transaction.setAmount(pkg.getPrice());
        transaction.setStatus("PENDING"); // Đợi callback từ MoMo

        // LƯU: merge() sẽ xử lý đối tượng detached (user, pkg)
        transactionDAO.save(transaction); 
        
        // 3. Gọi API MoMo
        // Chú ý: amount phải là Long và KHÔNG CÓ DẤU CHẤM/PHẨY (ví dụ: 99000)
        String payUrl = momoService.createPaymentRequest(
            orderIdString,
            pkg.getPrice(), // Giả định PremiumPackage.getPrice() trả về long/long
            orderInfo,
            extraData,
            String.valueOf(userId) // userId cho MoMo
        );
        
        return payUrl; // Trả về URL để Controller chuyển hướng
    }
    
    public List<Transaction> getCompletedTransactions(Long userId) {
        // Sử dụng phương thức đặc thù từ TransactionDAOImpl
        return transactionDAO.findUserCompletedTransactions(userId);
    }

    // PHƯƠNG THỨC 1: findTransactionById (Sử dụng ID kiểu Long)
    public Transaction findTransactionByOrderId(String orderId) {
        // Gọi phương thức đặc thù đã thêm vào DAO
        return transactionDAO.findByOrderId(orderId);
    }
    
    /**
     * Phương thức Facade để lưu/cập nhật Transaction (sử dụng merge/save).
     */
    public void saveTransaction(Transaction transaction) {
        // Gọi save/update được kế thừa từ GenericDAOImpl
        transactionDAO.save(transaction); 
    }

    // PHƯƠNG THỨC 3: verifyMoMoCallback (Giữ nguyên)
    public boolean verifyPaymentCallback(String signature, JsonObject data) {
        try {
            // Tái tạo chuỗi Raw Signature theo ĐÚNG CÁC TRƯỜNG VÀ THỨ TỰ CỦA MỠ MỖ CALLBACK
            String rawSignature =
                    "accessKey=" + MoMoConfig.ACCESS_KEY +
                            "&amount=" + data.get("amount").getAsString() +
                            "&extraData=" + (data.has("extraData") && !data.get("extraData").isJsonNull() ? data.get("extraData").getAsString() : "") +
                            "&message=" + data.get("message").getAsString() +
                            "&orderId=" + data.get("orderId").getAsString() +
                            "&orderInfo=" + data.get("orderInfo").getAsString() +
                            "&orderType=" + data.get("orderType").getAsString() +
                            "&partnerCode=" + data.get("partnerCode").getAsString() +
                            "&payType=" + data.get("payType").getAsString() +
                            "&requestId=" + data.get("requestId").getAsString() +
                            "&responseTime=" + data.get("responseTime").getAsString() +
                            "&resultCode=" + data.get("resultCode").getAsString() +
                            "&transId=" + data.get("transId").getAsString();

            // In ra để kiểm tra chuỗi Raw Signature này

            return MoMoSecurity.verifySignature(signature, rawSignature, MoMoConfig.SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}