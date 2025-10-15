package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Comment;
import java.util.List;

public interface CommentDAO {
    
    /**
     * Tạo mới comment
     */
    Comment create(Comment comment);
    
    /**
     * Cập nhật comment
     */
    Comment update(Comment comment);
    
    /**
     * Xóa comment theo ID
     */
    boolean delete(Long id);
    
    /**
     * Tìm comment theo ID
     */
    Comment findById(Long id);
    
    List<Comment> findAll();
    /**
     * Lấy tất cả comments của một recipe
     */
    List<Comment> findByRecipeId(Long recipeId);
    
    /**
     * Lấy tất cả comments của một user
     */
    List<Comment> findByUserId(Long userId);
    
    /**
     * Đếm số lượng comment của một recipe
     */
    long countByRecipeId(Long recipeId);
}