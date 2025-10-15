package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Notification;
import com.mycompany.webhuongdannauan.service.NotificationService;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "NotificationServlet", urlPatterns = {"/notifications"})
public class NotificationServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        long userId = user.getId();
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        long unreadCount = notifications.stream().filter(n -> !n.getIsRead()).count();

        request.setAttribute("notifications", notifications);
        request.setAttribute("unreadCount", unreadCount);

        // SỬA LẠI ĐƯỜNG DẪN Ở ĐÂY
        request.getRequestDispatcher("/WEB-INF/views/notification/notifications.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Sửa: Dùng getSession(false) an toàn hơn
        User user = (session != null) ? (User) session.getAttribute("user") : null; // Sửa: Kiểm tra session có null không

        // Đặt kiểu nội dung phản hồi là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 - Chưa xác thực
            // Trả về JSON báo lỗi
            response.getWriter().write("{\"status\": \"error\", \"message\": \"User not logged in.\"}");
            return;
        }

        try {
            long userId = user.getId();
            notificationService.markAllAsRead(userId);

            // Trả về JSON báo thành công
            response.getWriter().write("{\"status\": \"success\"}");

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra log server để debug
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 - Lỗi máy chủ
            // Trả về JSON báo lỗi
            response.getWriter().write("{\"status\": \"error\", \"message\": \"An internal error occurred.\"}");
        }
    }
}