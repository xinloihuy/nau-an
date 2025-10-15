package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.CommentDAO;
import com.mycompany.webhuongdannauan.dao.impl.CommentDAOImpl;
import com.mycompany.webhuongdannauan.model.Comment;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.utils.HibernateUtil;
import jakarta.persistence.EntityManager;

public class CommentService {

    private final CommentDAO commentDAO = new CommentDAOImpl();

    public void addComment(User user, Long recipeId, String content) {
        if (user == null || recipeId == null || content == null || content.trim().isEmpty()) {
            return; // Không làm gì nếu thiếu thông tin
        }

        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Lấy Recipe từ CSDL
            Recipe recipe = em.find(Recipe.class, recipeId);
            
            if (recipe != null) {
                Comment newComment = new Comment();
                newComment.setUser(user);
                newComment.setRecipe(recipe);
                newComment.setContent(content);
                em.persist(newComment); // Dùng EntityManager để lưu
            }
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // In lỗi ra để debug
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteComment(Long commentId, Long userId) {
        // Có thể thêm logic kiểm tra xem người xóa có phải chủ comment không
        commentDAO.delete(commentId);
    }
}