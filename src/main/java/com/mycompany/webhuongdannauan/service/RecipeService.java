package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.RecipeDAO;
import com.mycompany.webhuongdannauan.dao.impl.RecipeDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.GenericDAOImpl;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.model.ViewHistory;
import java.util.Collections;
import java.util.List;

public class RecipeService {

    private final RecipeDAO recipeDAO;
    private final UserService userService;
    private final GenericDAOImpl<ViewHistory, Long> viewHistoryDAO;

    private static final int RELATED_LIMIT = 4;

    public RecipeService() {
        this.recipeDAO = new RecipeDAOImpl();
        this.userService = new UserService(); 
        this.viewHistoryDAO = new GenericDAOImpl<ViewHistory, Long>() {};
    }

    // ----------------------------------------------------
    // --- 1. CHỨC NĂNG TRANG CHỦ & PHÂN TÁCH VIP/THƯỜNG ---
    // ----------------------------------------------------

    /**
     * Lấy danh sách món ăn thịnh hành (CHỈ MÓN THƯỜNG: isVip=false).
     */
    public List<Recipe> getFeaturedRecipes(int limit) {
        return recipeDAO.findFeaturedNonVipRecipes(limit);
    }
    
    /**
     * Lấy danh sách MÓN VIP (isVip=true).
     */
    public List<Recipe> getVipRecipes() {
        return recipeDAO.findVipRecipes();
    }

    // ----------------------------------------------------
    // --- 2. CHỨC NĂNG TÌM KIẾM & PHÂN LOẠI ---
    // ----------------------------------------------------

    /**
     * Tìm kiếm món ăn theo từ khóa (CHỈ MÓN THƯỜNG).
     */
    public List<Recipe> searchNonVipRecipes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // Nếu không có từ khóa, trả về danh sách trống hoặc món thịnh hành
            return Collections.emptyList();
        }
        // Gọi DAO tìm kiếm được sửa đổi để chỉ tìm món isVip=false
        return recipeDAO.searchNonVipRecipes(keyword);
    }

    /**
     * Lấy danh sách món ăn theo chủ đề (Category).
     * Hàm này được sử dụng cho CategoryServlet.
     */
    public List<Recipe> getRecipesByCategory(Long categoryId) {
        // Giả định DAO này tự động loại trừ món VIP (hoặc cần kiểm tra logic DAO)
        return recipeDAO.findByCategory(categoryId);
    }

    // ----------------------------------------------------
    // --- 3. CHỨC NĂNG XEM CHI TIẾT & LỊCH SỬ ---
    // ----------------------------------------------------

    /**
     * Lấy chi tiết món ăn, cập nhật lượt xem và lịch sử xem.
     */
    public Recipe getRecipeDetails(Long recipeId, Long userId) {
        // Dùng findByIdWithCategories để tải EAGERLY Author và Categories
        Recipe recipe = recipeDAO.findByIdWithCategories(recipeId);
        
        if (recipe != null) {
            recipeDAO.updateViewCount(recipeId); // Cập nhật lượt xem
            
            // Cập nhật lịch sử xem (Chỉ cho User)
            if (userId != null) {
                User user = userService.findUserById(userId);
                if (user != null) {
                    recordViewHistory(user, recipe);
                }
            }
        }
        return recipe;
    }

    /**
     * Ghi lại sự kiện xem vào ViewHistory.
     */
    private void recordViewHistory(User user, Recipe recipe) {
        ViewHistory history = new ViewHistory();
        history.setUser(user);
        history.setRecipe(recipe);
        viewHistoryDAO.save(history); 
    }
    
    // --- Gợi ý Món ăn Liên quan ---
    public List<Recipe> getRelatedRecipes(Recipe currentRecipe) {
        return recipeDAO.findRelatedRecipes(currentRecipe, RELATED_LIMIT);
    }

    // ----------------------------------------------------
    // --- 4. CHỨC NĂNG QUẢN LÝ (Admin/Premium User) ---
    // ----------------------------------------------------

    /**
     * Lấy tất cả món ăn cùng với Author (dành cho trang Admin).
     */
    public List<Recipe> getAllRecipes() {
        return recipeDAO.findAllWithAuthor(); 
    }
    
    /**
     * Lọc nâng cao (dùng cho FilterServlet).
     */
    public List<Recipe> getFilteredRecipes(String keyword, Long categoryId, Integer maxTime, Boolean hasVideo, Boolean isVip) {
        return recipeDAO.filterRecipes(keyword, categoryId, maxTime, hasVideo, isVip);
    }

    /**
     * Đăng tải/Cập nhật món ăn (Premium/Admin).
     */
    public void saveOrUpdateRecipe(Recipe recipe) {
        recipeDAO.save(recipe);
    }
    
    /**
     * Xóa món ăn theo ID (Admin).
     */
    public boolean deleteRecipe(Long recipeId) {
        Recipe recipe = recipeDAO.findById(recipeId);
        if (recipe != null) {
            recipeDAO.delete(recipe);
            return true;
        }
        return false;
    }
    
    /**
     * Đăng tải món ăn mới (Chỉ cho User Premium).
     */
    public boolean uploadNewRecipe(Recipe recipe, Long authorId) {
        User author = userService.findUserById(authorId);
        
        if (author == null || !userService.isUserPremium(author)) {
            return false;
        }

        recipe.setAuthor(author);
        recipeDAO.save(recipe);
        return true;
    }
}