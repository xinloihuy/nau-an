package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.service.EmailService;
import com.mycompany.webhuongdannauan.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Random;

@WebServlet("/send-otp")
public class SendOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            resp.getWriter().write("ERROR: Email không hợp lệ!");
            return;
        }

        try {
            // Sinh OTP ngẫu nhiên 6 chữ số
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Lưu OTP vào cache (5 phút)
            OtpService.getInstance().put(email, otp);

            // Gửi email OTP
            String body = "<h3>Xin chào!</h3>"
                    + "<p>Bạn vừa yêu cầu mã xác thực tài khoản tại <b>Cooking App</b>.</p>"
                    + "<p>Mã OTP của bạn là:</p>"
                    + "<h2 style='color:teal; letter-spacing:2px;'>" + otp + "</h2>"
                    + "<p>Mã này sẽ hết hạn sau <b>5 phút</b>.</p>"
                    + "<hr><small>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</small>";

            EmailService.sendEmail(email, "Mã xác thực Cooking App", body);
            resp.getWriter().write("SUCCESS: Mã OTP đã được gửi!");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("ERROR: Gửi email thất bại: " + e.getMessage());
        }
    }
}
