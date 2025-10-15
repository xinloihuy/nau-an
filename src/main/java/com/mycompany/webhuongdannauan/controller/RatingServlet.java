package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.RatingService; // Import service mới
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RatingServlet", urlPatterns = {"/rate"})
public class RatingServlet extends HttpServlet {
    
    // Servlet chỉ làm việc với Service
    private final RatingService ratingService = new RatingService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String recipeIdStr = req.getParameter("recipeId");
        String ratingValueStr = req.getParameter("ratingValue");

        if (recipeIdStr == null || ratingValueStr == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            long recipeId = Long.parseLong(recipeIdStr);
            int score = Integer.parseInt(ratingValueStr);

            // Gọi Service để xử lý
            ratingService.createOrUpdateRating(currentUser, recipeId, score);

            // Luôn chuyển hướng về trang chi tiết
            resp.sendRedirect(req.getContextPath() + "/recipe?id=" + recipeId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }
    }
}