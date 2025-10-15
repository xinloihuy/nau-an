package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.service.EmailService;
import com.mycompany.webhuongdannauan.service.UserService;
import com.mycompany.webhuongdannauan.utils.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            req.setAttribute("error", "Vui lòng nhập email hợp lệ!");
            doGet(req, resp);
            return;
        }
        var user = userService.findByEmail(email);
        if (user == null) {
            req.setAttribute("error", "Email không tồn tại trong hệ thống!");
            doGet(req, resp);
            return;
        }

        try {
            String otp = String.format("%06d", new Random().nextInt(999999));

            String body = "<h3>Khôi phục mật khẩu - Cooking App</h3>"
                    + "<p>Mật khẩu mới của bạn là:</p>"
                    + "<h2 style='color:#ff8c00; letter-spacing:2px;'>" + otp + "</h2>";

            EmailService.sendEmail(email, "Khôi phục mật khẩu - Cooking App", body);

            req.setAttribute("message", "Khôi phục mật khẩu thành công vui lòng kiểm tra email của bạn!");
            user.setPassword(SecurityUtil.hashPassword(otp));
            userService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Không thể gửi email: " + e.getMessage());
        }

        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }
}
