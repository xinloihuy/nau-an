package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Ánh xạ đến đường dẫn login admin (ví dụ: /admin/login)
@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private final UserService userService = new UserService();
    
    private static final String LOGIN_VIEW = "/WEB-INF/views/admin/login-admin.jsp";
    private static final String DASHBOARD_URL = "/admin/dashboard"; // URL chuyển hướng sau login

    // --- 1. Xử lý Đăng nhập (POST) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");

        // 1. Xác thực User bằng Service (Sử dụng mật khẩu đã băm)
        User user = userService.login(usernameOrEmail, password);

        if (user != null && userService.isAdmin(user)) {
            // 2. Thành công: User tồn tại và có quyền ADMIN
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId()); 
            session.setAttribute("username", user.getUsername());
            session.setAttribute("isAdmin", true); // Đánh dấu quyền admin
            
            // Chuyển hướng (Redirect) để tránh gửi lại form
            response.sendRedirect(request.getContextPath() + DASHBOARD_URL);
            
        } else {
            // 3. Thất bại: Sai thông tin hoặc không có quyền Admin
            request.setAttribute("error", "Sai tên đăng nhập/mật khẩu hoặc bạn không có quyền Admin!");
            request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
        }
    }

    // --- 2. Xử lý Hiển thị Form Login (GET) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Chuyển tiếp tới trang login form
        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
    }
}