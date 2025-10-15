package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.BlogPost;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminBlogServlet", urlPatterns = {"/admin/blog"})
public class AdminBlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BlogPostDAOImpl blogDAO = new BlogPostDAOImpl();
        List<BlogPost> blogs = blogDAO.findAllBlogs();

        request.setAttribute("blogs", blogs);
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-blog.jsp").forward(request, response);
    }
}
