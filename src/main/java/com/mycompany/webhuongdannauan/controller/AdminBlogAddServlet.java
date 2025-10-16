package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.BlogPost;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import java.util.Date;


@WebServlet(name = "AdminBlogAddServlet", urlPatterns = {"/admin/blog/add"})
public class AdminBlogAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-blog-add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String authorName = request.getParameter("author");

        EntityManager em = HibernateUtil.getEntityManager();
        BlogPostDAOImpl blogDAO = new BlogPostDAOImpl(em);

        try {
            em.getTransaction().begin();

            BlogPost blog = new BlogPost();
            blog.setTitle(title);
            blog.setContent(content);
            blog.setCreatedAt(new Date());


            // Lấy user theo tên (nếu có)
            User author = em.createQuery("SELECT u FROM User u WHERE u.username = :name", User.class)
                    .setParameter("name", authorName)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            blog.setAuthor(author);
            blogDAO.save(blog);

            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/admin/blog");

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
