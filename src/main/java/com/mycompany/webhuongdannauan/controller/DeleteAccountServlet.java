package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-account")
public class DeleteAccountServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");
        if (userId == null) {
            res.sendRedirect("login");
            return;
        }

        User currentUser = userDAO.findById(userId);
        if (currentUser == null) {
            req.getSession().setAttribute("error", "Người dùng không tồn tại!");
            res.sendRedirect("/logout");
            return;
        }
        currentUser.getAccountSetting().setIsDeleted(true);
        userDAO.save(currentUser);
        req.getSession().setAttribute("message", "Xóa tài khoản thành công!");

        req.getSession().invalidate();
        res.sendRedirect("/register");
    }
}

