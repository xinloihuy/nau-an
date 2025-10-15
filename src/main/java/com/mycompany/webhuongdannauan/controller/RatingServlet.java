package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.RatingDAO;
import com.mycompany.webhuongdannauan.dao.impl.RatingDAOImpl;
import com.mycompany.webhuongdannauan.model.Rating;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@WebServlet(name = "RatingServlet", urlPatterns = {"/api/recipes/*/rate", "/api/ratings/*"})
public class RatingServlet extends HttpServlet {
    
    private RatingDAO ratingDAO;
    private EntityManagerFactory emf;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        ratingDAO = new RatingDAOImpl();
        emf = Persistence.createEntityManagerFactory("your-persistence-unit");
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }
    
    // GET: Lấy điểm trung bình và số lượt đánh giá của recipe
    // URL: /api/recipes/{recipeId}/rate
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long recipeId = extractRecipeIdFromPath(req);
            
            if (recipeId == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Recipe ID is required\"}");
                return;
            }
            
            Double average = ratingDAO.getAverageRating(recipeId);
            long count = ratingDAO.countByRecipeId(recipeId);
            
            String json = String.format(
                "{\"status\":\"success\",\"average\":%.2f,\"count\":%d}", 
                average, count
            );
            out.print(json);
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // POST: Tạo hoặc cập nhật rating
    // URL: /api/recipes/{recipeId}/rate
    // Body: userId, score (1-5)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long recipeId = extractRecipeIdFromPath(req);
            String userIdStr = req.getParameter("userId");
            String scoreStr = req.getParameter("score");
            
            if (recipeId == null || userIdStr == null || scoreStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Missing required parameters\"}");
                return;
            }
            
            Long userId = Long.parseLong(userIdStr);
            Integer score = Integer.parseInt(scoreStr);
            
            // Validate score (1-5)
            if (score < 1 || score > 5) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Score must be between 1 and 5\"}");
                return;
            }
            
            // Tạo hoặc cập nhật rating
            EntityManager em = emf.createEntityManager();
            try {
                User user = em.find(User.class, userId);
                Recipe recipe = em.find(Recipe.class, recipeId);
                
                if (user == null || recipe == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"status\":\"error\",\"message\":\"User or Recipe not found\"}");
                    return;
                }
                
                Rating rating = new Rating();
                rating.setUser(user);
                rating.setRecipe(recipe);
                rating.setScore(score);
                
                Rating saved = ratingDAO.createOrUpdate(rating);
                
                // Lấy điểm trung bình mới
                Double newAverage = ratingDAO.getAverageRating(recipeId);
                long count = ratingDAO.countByRecipeId(recipeId);
                
                String json = String.format(
                    "{\"status\":\"success\",\"data\":%s,\"average\":%.2f,\"count\":%d}", 
                    gson.toJson(saved), newAverage, count
                );
                out.print(json);
                
            } finally {
                em.close();
            }
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\":\"error\",\"message\":\"Invalid number format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // DELETE: Xóa rating
    // URL: /api/ratings/{ratingId}
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long ratingId = extractRatingIdFromPath(req);
            
            if (ratingId == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Rating ID is required\"}");
                return;
            }
            
            boolean deleted = ratingDAO.delete(ratingId);
            
            if (deleted) {
                out.print("{\"status\":\"success\",\"message\":\"Rating deleted\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"status\":\"error\",\"message\":\"Rating not found\"}");
            }
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\":\"error\",\"message\":\"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // Helper methods
    private Long extractRecipeIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo(); // /recipes/123/rate
        if (pathInfo != null && pathInfo.contains("/recipes/")) {
            String[] parts = pathInfo.split("/");
            for (int i = 0; i < parts.length; i++) {
                if ("recipes".equals(parts[i]) && i + 1 < parts.length) {
                    try {
                        return Long.parseLong(parts[i + 1]);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    
    private Long extractRatingIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo(); // /ratings/123
        if (pathInfo != null) {
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2) {
                try {
                    return Long.parseLong(parts[parts.length - 1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
    
    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
