package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.BlogPost;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminBlogEditServlet", urlPatterns = {"/admin/blog/edit"})
public class AdminBlogEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        BlogPostDAOImpl blogDAO = new BlogPostDAOImpl();
        BlogPost blog = blogDAO.findById(id);

        request.setAttribute("blog", blog);
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-blog-edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        BlogPostDAOImpl blogDAO = new BlogPostDAOImpl();
        BlogPost blog = blogDAO.findById(id);

        blog.setTitle(title);
        blog.setContent(content);

        blogDAO.save(blog);
        response.sendRedirect(request.getContextPath() + "/admin/blog");
    }
}
