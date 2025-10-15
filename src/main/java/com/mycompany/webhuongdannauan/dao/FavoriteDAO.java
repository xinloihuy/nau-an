package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Favorite;
import com.mycompany.webhuongdannauan.model.Recipe;
import java.util.List;

public interface FavoriteDAO {
    
    /**
     * Thêm recipe vào favorites
     */
    Favorite create(Favorite favorite);
    
    /**
     * Xóa favorite (bỏ yêu thích)
     */
    boolean delete(Long id);
    
    /**
     * Xóa favorite theo user và recipe
     */
    boolean deleteByUserAndRecipe(Long userId, Long recipeId);
    
    /**
     * Tìm favorite theo ID
     */
    Favorite findById(Long id);
    
    /**
     * Kiểm tra user đã yêu thích recipe chưa
     */
    boolean existsByUserAndRecipe(Long userId, Long recipeId);
    
    /**
     * Tìm favorite của user cho recipe cụ thể
     */
    Favorite findByUserAndRecipe(Long userId, Long recipeId);
    
    /**
     * Lấy tất cả recipes yêu thích của user
     */
    List<Recipe> findFavoriteRecipesByUserId(Long userId);
    
    /**
     * Đếm số lượng favorites của một recipe
     */
    long countByRecipeId(Long recipeId);
    
    /**
     * Toggle favorite (thêm nếu chưa có, xóa nếu đã có)
     */
    boolean toggle(Long userId, Long recipeId);
}