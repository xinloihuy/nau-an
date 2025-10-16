package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.impl.BlogPostDAOImpl;
import com.mycompany.webhuongdannauan.model.BlogPost;
<<<<<<< HEAD
=======
import com.mycompany.webhuongdannauan.service.UserService; 
>>>>>>> f341c34c1fe90cdd19b11ce5d0672527e2c6e7bb
import com.mycompany.webhuongdannauan.utils.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BlogServlet", urlPatterns = {"/blog"})
public class BlogServlet extends HttpServlet {

<<<<<<< HEAD
=======
    // KHỞI TẠO UserService
    private final UserService userService = new UserService(); 

>>>>>>> f341c34c1fe90cdd19b11ce5d0672527e2c6e7bb
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = HibernateUtil.getEntityManager();
<<<<<<< HEAD
        BlogPostDAOImpl dao = new BlogPostDAOImpl(em);

        List<BlogPost> blogs = dao.findAllBlogs();
        request.setAttribute("blogs", blogs);

        em.close();
        request.getRequestDispatcher("/WEB-INF/views/blog.jsp").forward(request, response);
    }
}
=======
        BlogPostDAOImpl dao = new BlogPostDAOImpl(em); 
        
        HttpSession session = request.getSession(false);
        // Lấy userId (ID của người xem hiện tại)
        Long userId = (session != null) ? (Long) session.getAttribute("userId") : null;

        try {
            List<BlogPost> blogs = dao.findAllBlogs();
            request.setAttribute("blogs", blogs);
            
            // CHUẨN BỊ BIẾN CẦN THIẾT CHO FOLLOW LOGIC TRONG JSP
            // 1. Gửi UserService Object
            request.setAttribute("userService", userService);
            // 2. Gửi userId hiện tại
            request.setAttribute("currentUserId", userId); 
            // 3. Gửi User Object (nếu bạn cần truy cập chi tiết user khác)
            // if (userId != null) {
            //     request.setAttribute("currentUser", userService.findUserById(userId));
            // }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi tải danh sách blog.");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        
        // Forward đến trang blog
        request.getRequestDispatcher("/WEB-INF/views/blog.jsp").forward(request, response);
    }
}
>>>>>>> f341c34c1fe90cdd19b11ce5d0672527e2c6e7bb
