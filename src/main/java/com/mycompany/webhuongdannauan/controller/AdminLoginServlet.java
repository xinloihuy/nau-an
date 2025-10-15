package com.mycompany.webhuongdannauan.controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ("admin".equals(username) && "admin".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("admin", username);

            // Chuyển sang trang admin chính
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp");
            rd.forward(request, response);
        } else {
            // Sai tài khoản
            request.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/login-admin.jsp");
            rd.forward(request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // Khi bấm "Quay lại trang Admin" — hiển thị lại dashboard
    request.getRequestDispatcher("/WEB-INF/views/admin/admin.jsp").forward(request, response);
    }

}
