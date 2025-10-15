package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminBlogDeleteServlet", urlPatterns = {"/admin/blog/delete"})
public class AdminBlogDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        BlogPostDAOImpl blogDAO = new BlogPostDAOImpl();
        blogDAO.deleteById(id);

        response.sendRedirect(request.getContextPath() + "/admin/blog");
    }
}
