package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.CommentDAO;
import com.mycompany.webhuongdannauan.dao.impl.CommentDAOImpl;
import com.mycompany.webhuongdannauan.model.Comment;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@WebServlet(name = "CommentServlet", urlPatterns = {"/api/recipes/*/comments", "/api/comments/*"})
public class CommentServlet extends HttpServlet {
    
    private CommentDAO commentDAO;
    private EntityManagerFactory emf;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        commentDAO = new CommentDAOImpl();
        emf = Persistence.createEntityManagerFactory("your-persistence-unit");
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }
    
    // GET: Lấy danh sách comments của một recipe
    // URL: /api/recipes/{recipeId}/comments
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
            
            List<Comment> comments = commentDAO.findByRecipeId(recipeId);
            
            // Tạo response JSON
            String jsonResponse = gson.toJson(comments);
            out.print(jsonResponse);
            
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // POST: Tạo comment mới
    // URL: /api/recipes/{recipeId}/comments
    // Body: userId, content
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long recipeId = extractRecipeIdFromPath(req);
            String userIdStr = req.getParameter("userId");
            String content = req.getParameter("content");
            
            if (recipeId == null || userIdStr == null || content == null || content.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Missing required parameters\"}");
                return;
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            // Tạo comment mới
            EntityManager em = emf.createEntityManager();
            try {
                User user = em.find(User.class, userId);
                Recipe recipe = em.find(Recipe.class, recipeId);
                
                if (user == null || recipe == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"status\":\"error\",\"message\":\"User or Recipe not found\"}");
                    return;
                }
                
                Comment comment = new Comment();
                comment.setUser(user);
                comment.setRecipe(recipe);
                comment.setContent(content);
                
                Comment created = commentDAO.create(comment);
                
                out.print("{\"status\":\"success\",\"data\":" + gson.toJson(created) + "}");
                
            } finally {
                em.close();
            }
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\":\"error\",\"message\":\"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // PUT: Cập nhật comment
    // URL: /api/comments/{commentId}
    // Body: content
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long commentId = extractCommentIdFromPath(req);
            String content = req.getParameter("content");
            
            if (commentId == null || content == null || content.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Missing required parameters\"}");
                return;
            }
            
            Comment comment = commentDAO.findById(commentId);
            
            if (comment == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"status\":\"error\",\"message\":\"Comment not found\"}");
                return;
            }
            
            comment.setContent(content);
            Comment updated = commentDAO.update(comment);
            
            out.print("{\"status\":\"success\",\"data\":" + gson.toJson(updated) + "}");
            
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\":\"error\",\"message\":\"Invalid ID format\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // DELETE: Xóa comment
    // URL: /api/comments/{commentId}
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        try {
            Long commentId = extractCommentIdFromPath(req);
            
            if (commentId == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Comment ID is required\"}");
                return;
            }
            
            boolean deleted = commentDAO.delete(commentId);
            
            if (deleted) {
                out.print("{\"status\":\"success\",\"message\":\"Comment deleted\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"status\":\"error\",\"message\":\"Comment not found\"}");
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
        String pathInfo = req.getPathInfo(); // /recipes/123/comments
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
    
    private Long extractCommentIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo(); // /comments/123
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
