package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.BlogPost;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BlogServlet", urlPatterns = {"/blog"})
public class BlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = HibernateUtil.getEntityManager();
        BlogPostDAOImpl dao = new BlogPostDAOImpl(em);

        List<BlogPost> blogs = dao.findAllBlogs();
        request.setAttribute("blogs", blogs);

        em.close();
        request.getRequestDispatcher("/WEB-INF/views/blog.jsp").forward(request, response);
    }
}
