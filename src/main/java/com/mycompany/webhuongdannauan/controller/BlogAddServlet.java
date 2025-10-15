package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.BlogPost;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "BlogAddServlet", urlPatterns = {"/blog/add"})
public class BlogAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/blog-add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        User author = (User) request.getSession().getAttribute("user");
        if (author == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        EntityManager em = HibernateUtil.getEntityManager();
        BlogPostDAOImpl blogDAO = new BlogPostDAOImpl(em);

        try {
            em.getTransaction().begin();

            BlogPost blog = new BlogPost();
            blog.setTitle(title);
            blog.setContent(content);
            blog.setAuthor(author);

            blogDAO.save(blog); // DAO dùng cùng EntityManager

            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/blog");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
