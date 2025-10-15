package com.mycompany.webhuongdannauan.controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Hủy session admin
        }

        // Chuyển về trang đăng nhập người dùng chính
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
