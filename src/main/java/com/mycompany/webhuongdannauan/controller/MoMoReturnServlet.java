package com.mycompany.webhuongdannauan.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.webhuongdannauan.model.Transaction;
import com.mycompany.webhuongdannauan.service.MoMoPaymentService; 
import com.mycompany.webhuongdannauan.service.PremiumAccountService;
import com.mycompany.webhuongdannauan.service.TransactionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet("/momo-return")
public class MoMoReturnServlet extends HttpServlet {

    private final MoMoPaymentService momoService = new MoMoPaymentService();
    private final TransactionService transactionService = new TransactionService();
    private final PremiumAccountService premiumAccountService = new PremiumAccountService();
    private final Gson gson = new Gson(); 

    // Helper function để URL Decode giá trị
    private String decodeParam(String param) {
        if (param == null) return "";
        try {
            return URLDecoder.decode(param, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return param;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy parameters MoMo
        String partnerCode = request.getParameter("partnerCode");
        String orderId = request.getParameter("orderId"); // <-- Dùng cái này để tìm kiếm
        System.out.println("THIS IS ORDERID:" + orderId);
        String requestId = request.getParameter("requestId");
        String amount = request.getParameter("amount");
        String orderInfo = decodeParam(request.getParameter("orderInfo")); 
        String orderType = request.getParameter("orderType");
        String transId = request.getParameter("transId");
        String resultCode = request.getParameter("resultCode");
        String message = decodeParam(request.getParameter("message")); 
        String payType = request.getParameter("payType");
        String responseTime = request.getParameter("responseTime");
        String extraData = request.getParameter("extraData");
        String signature = request.getParameter("signature");
        
        String redirectPath = request.getContextPath() + "/premium";

        // 1. Hoàn thiện JsonObject data (Cần đủ 13 trường đã được decode)
        JsonObject data = new JsonObject();
        data.addProperty("partnerCode", partnerCode);
        data.addProperty("orderId", orderId);
        data.addProperty("requestId", requestId);
        data.addProperty("amount", amount);
        data.addProperty("orderInfo", orderInfo);
        data.addProperty("orderType", orderType);
        data.addProperty("transId", transId);
        data.addProperty("resultCode", resultCode);
        data.addProperty("message", message);
        data.addProperty("payType", payType);
        data.addProperty("responseTime", responseTime);
        data.addProperty("extraData", extraData != null ? extraData : ""); // Tránh Null
        
        try {
            // 2. Verify signature
            boolean isValid = momoService.verifyPaymentCallback(signature, data);
            System.out.println("valid: " + isValid);
            
            if (!isValid) {
                response.sendRedirect(redirectPath + "?status=error&msg=invalid_signature");
                return;
            }

            // 3. Tìm Transaction bằng MoMo Order ID (String)
            // Giả định TransactionService.findTransactionByOrderId(String) đã được thêm.
            Transaction transaction = transactionService.findTransactionByOrderId(orderId); 

            if (transaction == null) {
                System.out.println("THIS IS HERE!");
                response.sendRedirect(redirectPath + "?status=error&msg=transaction_not_found");
                return;
            }
            
            // 4. Xử lý Logic Nghiệp vụ
            if ("0".equals(resultCode)) {
                
                if ("PENDING".equals(transaction.getStatus())) {

                    // Cập nhật trạng thái và transId
                    transaction.setStatus("COMPLETED");
                    // Giả định setter tồn tại:
                    transaction.setTransId(transId); 
                    
                    transactionService.saveTransaction(transaction); 

                    // Cập nhật PremiumAccount
                    // ...
                }
                
                response.sendRedirect(request.getContextPath() + "/premium?status=success");
                
            } else {
                // Xử lý thất bại
                if ("PENDING".equals(transaction.getStatus())) {
                    transaction.setStatus("FAILED");
                    transactionService.saveTransaction(transaction);
                }
                response.sendRedirect(redirectPath + "?status=error&msg=" + message);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(redirectPath + "?status=error&msg=internal_server_error");
        }
    }
}