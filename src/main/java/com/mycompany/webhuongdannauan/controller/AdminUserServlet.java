package com.mycompany.webhuongdannauan.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.model.User;

@WebServlet("/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {

    private UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy danh sách người dùng từ DB
        List<User> userList = userDAO.getAllUsers();

        // Truyền dữ liệu qua JSP
        request.setAttribute("userList", userList);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/admin-user.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nhận hành động từ form (add hoặc delete)
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // Lấy dữ liệu từ form thêm người dùng
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            // Thêm người dùng mới
            userDAO.addUser(username, email, password, role);

        } else if ("delete".equals(action)) {
            // Lấy ID cần xóa
            int id = Integer.parseInt(request.getParameter("id"));
            userDAO.deleteUser(id);
        }

        // Sau khi thêm hoặc xóa → quay lại danh sách
        response.sendRedirect(request.getContextPath() + "/AdminUserServlet");
    }
}
