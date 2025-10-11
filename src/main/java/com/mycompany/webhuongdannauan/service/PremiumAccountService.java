package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.model.PremiumAccount;
import com.mycompany.webhuongdannauan.model.PremiumPackage;
import com.mycompany.webhuongdannauan.model.User;

import java.util.Calendar;
import java.util.Date;

public class PremiumAccountService {
    
    // Giả định PremiumAccountDAO dùng GenericDAOImpl với ID là Long
    private final GenericDAOImpl<PremiumAccount, Long> premiumAccountDAO;

    public PremiumAccountService() {
        // Khởi tạo Generic DAO
        this.premiumAccountDAO = new GenericDAOImpl<PremiumAccount, Long>() {}; 
    }
    
    /**
     * Cập nhật hoặc tạo PremiumAccount cho User.
     * @param user Đối tượng User đã tải.
     * @param pkg Đối tượng PremiumPackage đã mua.
     */
    public void updateUserPremiumStatus(User user, PremiumPackage pkg) {
        // Lấy PremiumAccount hiện tại (cần Lazy Loading được xử lý hoặc Eager Loading)
        PremiumAccount currentAccount = user.getPremiumAccount();
        Date newStartDate;
        
        // 1. Tính toán ngày bắt đầu mới
        // Nếu gói Premium đang hoạt động và chưa hết hạn, bắt đầu gói mới từ ngày hết hạn của gói cũ
        if (currentAccount != null && currentAccount.getEndDate() != null && currentAccount.getEndDate().after(new Date())) {
            newStartDate = currentAccount.getEndDate();
        } else {
            // Nếu không có gói hoặc đã hết hạn, bắt đầu từ hôm nay
            newStartDate = new Date();
        }

        // 2. Tính toán ngày kết thúc mới
        Calendar cal = Calendar.getInstance();
        cal.setTime(newStartDate);
        // Giả định PremiumPackage có phương thức getDurationDays()
        cal.add(Calendar.DAY_OF_MONTH, pkg.getDurationInDays()); 
        Date newEndDate = cal.getTime();

        // 3. Tạo hoặc cập nhật Entity
        if (currentAccount == null) {
            currentAccount = new PremiumAccount();
            currentAccount.setUser(user);
        }
        
        currentAccount.setStartDate(newStartDate);
        currentAccount.setEndDate(newEndDate);
        currentAccount.setIsActive(true); // Đảm bảo trạng thái kích hoạt
        
        // Lưu/Cập nhật PremiumAccount qua Generic DAO
        premiumAccountDAO.save(currentAccount);
    }
    
    /**
     * Kiểm tra xem gói Premium của người dùng có còn hiệu lực không.
     */
    public boolean isUserPremium(User user) {
        PremiumAccount account = user.getPremiumAccount();
        // Kiểm tra nếu account tồn tại, active và ngày hết hạn lớn hơn ngày hiện tại
        return account != null && account.getIsActive() && account.getEndDate().after(new Date());
    }
}