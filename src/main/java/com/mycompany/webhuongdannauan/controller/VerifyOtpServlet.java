package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        if (email == null || otp == null || email.isBlank() || otp.isBlank()) {
            resp.getWriter().write("ERROR: Thiếu thông tin cần thiết!");
            return;
        }
        try {
            boolean verified = OtpService.getInstance().verify(email, otp);
            if (verified) {
                resp.getWriter().write("SUCCESS: Xác thực thành công! Bạn có thể đăng ký hoặc đăng nhập ngay.");
            } else {
                resp.getWriter().write("ERROR: Mã OTP không hợp lệ hoặc đã hết hạn.");
            }
        } catch (Exception e) {
            resp.getWriter().write("ERROR: Lỗi khi kích hoạt tài khoản: " + e.getMessage());
        }
    }
}
