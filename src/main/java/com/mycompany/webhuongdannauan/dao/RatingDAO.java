package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Rating;
import java.util.List;

public interface RatingDAO {
    
    /**
     * Tạo mới hoặc cập nhật rating (nếu user đã đánh giá rồi)
     */
    Rating createOrUpdate(Rating rating);
    
    /**
     * Tìm rating theo ID
     */
    Rating findById(Long id);
    
    /**
     * Tìm rating của một user cho một recipe cụ thể
     */
    Rating findByUserAndRecipe(Long userId, Long recipeId);
    
    /**
     * Lấy tất cả ratings của một recipe
     */
    List<Rating> findByRecipeId(Long recipeId);
    
    /**
     * Tính điểm trung bình của một recipe
     */
    Double getAverageRating(Long recipeId);
    
    /**
     * Đếm số lượng ratings của một recipe
     */
    long countByRecipeId(Long recipeId);
    
    /**
     * Xóa rating
     */
    boolean delete(Long id);
}