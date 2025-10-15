package com.mycompany.webhuongdannauan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private static final String DASHBOARD_VIEW = "/WEB-INF/views/admin/admin.jsp";
    private static final String LOGIN_URL = "/admin/login";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // 1. Kiểm tra session và quyền ADMIN
        if (session == null || session.getAttribute("isAdmin") == null || !(boolean)session.getAttribute("isAdmin")) {
            
            // Nếu không phải Admin hoặc chưa đăng nhập, chuyển hướng về trang login
            response.sendRedirect(request.getContextPath() + LOGIN_URL + "?error=access_denied");
            return;
        }

        // 2. Tải dữ liệu cần thiết cho dashboard (Thống kê, danh sách User, v.v.)
        // Ví dụ: AdminService adminService = new AdminService();
        // request.setAttribute("stats", adminService.getSystemStatistics());
        
        // 3. Hiển thị Dashboard
        request.getRequestDispatcher(DASHBOARD_VIEW).forward(request, response);
    }
}