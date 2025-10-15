package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Transaction;
import com.mycompany.webhuongdannauan.service.StatisticService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin/statistics")
public class AdminStatisticServlet extends HttpServlet {
    
    private final StatisticService statisticService = new StatisticService(); // Sử dụng Service

    private static final String STATS_VIEW = "/WEB-INF/views/admin/statistics.jsp";
    private static final String ADMIN_LOGIN_URL = "/admin/login";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        // Kiểm tra quyền Admin (cần thiết cho mọi trang Admin)
        if (session == null || session.getAttribute("isAdmin") == null || !(boolean)session.getAttribute("isAdmin")) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL + "?error=access_denied");
            return;
        }

        try {
            // 1. Lấy Thống kê tổng quan từ Service
            Map<String, Object> overallStats = statisticService.getOverallStatistics();
            
            // 2. Lấy dữ liệu Biểu đồ từ Service
            Map<String, List<?>> chartData = statisticService.getChartData();
            
            // 3. Lấy Giao dịch gần nhất
            List<Transaction> recentTransactions = statisticService.getRecentTransactions(5);


            // 4. Đặt dữ liệu vào Request Scope
            // Thống kê tổng quan
            overallStats.forEach(request::setAttribute); // Đặt tất cả các key/value vào request scope

            // Dữ liệu biểu đồ
            request.setAttribute("monthLabels", chartData.get("monthLabels"));
            request.setAttribute("userCounts", chartData.get("userCounts"));
            request.setAttribute("revenueValues", chartData.get("revenueValues"));
            
            // Giao dịch
            request.setAttribute("recentTransactions", recentTransactions);


            // 5. Forward
            request.getRequestDispatcher(STATS_VIEW).forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Lỗi hệ thống khi tải thống kê: " + e.getMessage());
            request.setAttribute("error", "Không thể tải dữ liệu thống kê. Lỗi: " + e.getMessage());
            request.getRequestDispatcher(STATS_VIEW).forward(request, response);
        }
    }
}