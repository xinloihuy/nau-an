package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.UserService;
import com.mycompany.webhuongdannauan.utils.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            res.sendRedirect("/login");
            return;
        }

        User user = userService.findUserById(userId);
        user.setEmail(req.getParameter("email"));
        user.setNickname(req.getParameter("nickname"));

        String newPass = req.getParameter("newPassword");
        String confirmPass = req.getParameter("confirmPassword");
        if (newPass != null && !newPass.equals(confirmPass)) {
            req.getSession().setAttribute("error", "Mật khẩu xác nhận không khớp!");
            res.sendRedirect("/profile");
            return;
        }

        if (userService.findByEmailAndNotId(user.getEmail(), userId) != null) {
            req.getSession().setAttribute("error", "Email " + user.getEmail() + " đã được sử dụng bởi người dùng khác!");
            res.sendRedirect("/profile");
            return;
        }

        if (newPass != null && !newPass.trim().isEmpty()) {
            user.setPassword(SecurityUtil.hashPassword(newPass));
        }

        userService.saveUser(user);
        req.getSession().setAttribute("message", "Cập nhật thành công!");
        res.sendRedirect("/profile");
    }
}

