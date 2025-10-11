package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.service.TransactionService;
import com.mycompany.webhuongdannauan.service.PremiumAccountService;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/buy-premium")
public class TransactionServlet extends HttpServlet {

    private final TransactionService transactionService = new TransactionService();
    private final PremiumAccountService premiumAccountService = new PremiumAccountService();
    // Giả định có UserService để lấy User object từ ID
    // private final UserService userService = new UserService(); 

    // POST: Xử lý mua gói Premium
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
//        Long userId = (Long) session.getAttribute("userId"); // Giả định userId được lưu trong Session
        Long userId = 1L;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String packageIdParam = request.getParameter("packageId");

        try {
            Long packageId = Long.parseLong(packageIdParam);

            // Gọi phương thức tạo request MoMo
            String payUrl = transactionService.createMoMoPaymentRequest(userId, packageId);
            System.out.println(payUrl);

            if (payUrl != null) {
                // Lưu trữ thông tin cần thiết vào session trước khi chuyển hướng
                // (Đã làm ở code MoMo cũ, nhưng cần đảm bảo)
                // session.setAttribute("pendingOrderId", orderId); // Nếu cần

                // CHUYỂN HƯỚNG ĐẾN MO MO
                response.sendRedirect(payUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/premium?error=payment_request_failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/premium?error=invalid_selection");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/premium?error=payment_api_failed");
        }
    }
    
    // GET: Hiển thị trang mua gói Premium
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Load danh sách các gói Premium để hiển thị trên plan.jsp
        // request.setAttribute("packages", transactionService.getAllPremiumPackages()); 
        
        request.getRequestDispatcher("/premium").forward(request, response);
    }
}