package com.mycompany.webhuongdannauan.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.model.User;

public class AdminUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Gọi DAO để lấy danh sách người dùng
        UserDAOImpl userDAO = new UserDAOImpl();
        List<User> userList = userDAO.getAllUsers();

        // Truyền dữ liệu qua JSP
        request.setAttribute("userList", userList);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin/admin-user.jsp");
        rd.forward(request, response);
    }
}
