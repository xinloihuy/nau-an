package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.FavoriteDAO;
import com.mycompany.webhuongdannauan.dao.impl.FavoriteDAOImpl;
import com.mycompany.webhuongdannauan.model.Recipe;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorites", "/api/users/*/favorites", "/api/favorites/*"})
public class FavoriteServlet extends HttpServlet {
    
    private FavoriteDAO favoriteDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        favoriteDAO = new FavoriteDAOImpl();
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }
    
    // GET: Lấy danh sách favorites của user
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String path = req.getServletPath();
        
        // Nếu người dùng gọi /favorites (hiển thị trang JSP)
        if ("/favorites".equals(path) && req.getPathInfo() == null) {
            // Kiểm tra user đã login chưa
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            
            // Lấy userId từ session
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            
            // Load favorites data từ database
            List<Recipe> favorites = favoriteDAO.findFavoriteRecipesByUserId(userId);
            req.setAttribute("favorites", favorites);
            
            // ✅ SỬA ĐƯỜNG DẪN CHO ĐÚNG
            req.getRequestDispatcher("/views/interaction/favorites.jsp").forward(req, resp);
            return;
        }
        
        // API endpoint: /api/users/{userId}/favorites
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long userId = extractUserIdFromPath(req);
            
            if (userId == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"User ID is required\"}");
                return;
            }
            
            List<Recipe> favorites = favoriteDAO.findFavoriteRecipesByUserId(userId);
            
            String jsonResponse = gson.toJson(favorites);
            out.print(jsonResponse);
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // POST: Toggle favorite (thêm hoặc xóa)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long userId = extractUserIdFromPath(req);
            String recipeIdStr = req.getParameter("recipeId");
            
            if (userId == null || recipeIdStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Missing required parameters\"}");
                return;
            }
            
            Long recipeId = Long.parseLong(recipeIdStr);
            
            // Toggle favorite
            boolean isFavorited = favoriteDAO.toggle(userId, recipeId);
            
            String json = String.format(
                "{\"status\":\"success\",\"favorited\":%s,\"message\":\"%s\"}", 
                isFavorited, 
                isFavorited ? "Added to favorites" : "Removed from favorites"
            );
            out.print(json);
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\":\"error\",\"message\":\"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // DELETE: Xóa favorite cụ thể
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long favoriteId = extractFavoriteIdFromPath(req);
            
            if (favoriteId == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Favorite ID is required\"}");
                return;
            }
            
            boolean deleted = favoriteDAO.delete(favoriteId);
            
            if (deleted) {
                out.print("{\"status\":\"success\",\"message\":\"Favorite removed\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"status\":\"error\",\"message\":\"Favorite not found\"}");
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
    private Long extractUserIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo(); // /users/123/favorites
        if (pathInfo != null && pathInfo.contains("/users/")) {
            String[] parts = pathInfo.split("/");
            for (int i = 0; i < parts.length; i++) {
                if ("users".equals(parts[i]) && i + 1 < parts.length) {
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
    
    private Long extractFavoriteIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo(); // /favorites/123
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
}
