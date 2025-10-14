package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String link = "/views/register.jsp";
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String nickname = req.getParameter("nickname");
        String ageStr = req.getParameter("age");

        int age = 0;
        try {
            if (ageStr != null && !ageStr.isBlank()) {
                age = Integer.parseInt(ageStr);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Tuổi không hợp lệ!");
            req.getRequestDispatcher(link).forward(req, resp);
            return;
        }
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setNickname(nickname);
            newUser.setAge(age);
            var returnUser = userService.registerUser(newUser);
            if (returnUser == null) {
                req.setAttribute("error", "Email hoặc tên đăng nhập đã tồn tại!");
            } else {
                req.setAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
                link = "/views/login.jsp";
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher(link).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }
}
