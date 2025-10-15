package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.CommentService; // Import service mới
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "CommentServlet", urlPatterns = {"/comment"})
public class CommentServlet extends HttpServlet {
    
    // Servlet chỉ làm việc với Service
    private final CommentService commentService = new CommentService();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String action = req.getParameter("action");
        String recipeIdStr = req.getParameter("recipeId");
        
        if (action == null || recipeIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }

        try {
            long recipeId = Long.parseLong(recipeIdStr);

            if ("add".equals(action)) {
                String commentText = req.getParameter("commentText");
                // Gọi Service để xử lý
                commentService.addComment(currentUser, recipeId, commentText);
            } else if ("delete".equals(action)) {
                String commentIdStr = req.getParameter("commentId");
                if (commentIdStr != null) {
                    long commentId = Long.parseLong(commentIdStr);
                    // Gọi Service để xử lý
                    commentService.deleteComment(commentId, currentUser.getId());
                }
            }

            // Luôn chuyển hướng về trang chi tiết
            resp.sendRedirect(req.getContextPath() + "/recipe?id=" + recipeId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/error.jsp"); 
        }
    }
}