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
    private final UserService userService; // Cần thiết để kiểm tra quyền Premium
    private final GenericDAOImpl<ViewHistory, Long> viewHistoryDAO;

    // Giới hạn mặc định cho các món nổi bật
    private static final int FEATURED_LIMIT = 8;
    private static final int RELATED_LIMIT = 4;

    public RecipeService() {
        this.recipeDAO = new RecipeDAOImpl();
        this.userService = new UserService(); 
        this.viewHistoryDAO = new GenericDAOImpl<ViewHistory, Long>() {};
    }

    // --- 1. Chức năng Hiển thị (Trang chủ) ---

    /**
     * Lấy danh sách các món ăn nổi bật (dựa trên lượt xem cao nhất).
     */
    public List<Recipe> getFeaturedRecipes(int limit) {
        return recipeDAO.findFeaturedRecipes(limit);
    }
    
    /**
     * Lấy danh sách món VIP. Nếu người dùng là Premium, hiển thị món thật.
     */
    public List<Recipe> getVipRecipes(boolean isPremium) {
        if (isPremium) {
            return recipeDAO.findVipRecipes();
        } else {
            // Nếu không phải Premium, chỉ hiển thị một vài món VIP để khuyến khích mua gói, 
            // nhưng không tiết lộ thông tin chi tiết (hoặc hiển thị placeholder).
            // Logic ở đây sẽ chỉ lấy một số món isVip=true.
            return recipeDAO.findVipRecipes(); 
        }
    }

    // --- 2. Chức năng Tìm kiếm & Phân loại ---

    /**
     * Tìm kiếm món ăn theo từ khóa trong tiêu đề và mô tả.
     */
    public List<Recipe> searchRecipes(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return recipeDAO.searchByKeyword(keyword);
    }

    /**
     * Lấy danh sách món ăn theo chủ đề (Category).
     */
    public List<Recipe> getRecipesByCategory(Long categoryId) {
        return recipeDAO.findByCategory(categoryId);
    }

    // --- 3. Chức năng Xem chi tiết món ăn ---

    /**
     * Lấy chi tiết món ăn, cập nhật lượt xem và lịch sử xem.
     */
     public Recipe getRecipeDetails(Long recipeId, Long userId) {
        // THAY ĐỔI: Sử dụng phương thức DAO mới để đảm bảo Categories được tải
        Recipe recipe = recipeDAO.findByIdWithCategories(recipeId); 
        
        if (recipe != null) {
            // 3a. Cập nhật lượt xem (vẫn cần cập nhật thủ công vì dùng findByIdWithCategories)
            recipeDAO.updateViewCount(recipeId); 
            
            // 3b. Cập nhật lịch sử xem
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
        viewHistoryDAO.save(history); // Sử dụng Generic DAO cho thao tác CRUD
    }

    // --- 4. Chức năng Premium & Gợi ý ---

    /**
     * Kiểm tra quyền Premium và gợi ý món ăn liên quan.
     */
    public List<Recipe> getRelatedRecipes(Recipe currentRecipe) {
        return recipeDAO.findRelatedRecipes(currentRecipe, RELATED_LIMIT);
    }
    
    /**
     * Đăng tải món ăn mới (Chỉ cho User Premium).
     */
    public boolean uploadNewRecipe(Recipe recipe, Long authorId) {
        User author = userService.findUserById(authorId);
        
        // Kiểm tra xem tác giả có phải là Premium không
        if (author == null || !userService.isUserPremium(author)) {
            return false;
        }

        recipe.setAuthor(author);
        recipeDAO.save(recipe);
        return true;
    }

    // --- 5. Thao tác CRUD cơ bản cho Admin/Tác giả (Dùng GenericDAOImpl/RecipeDAOImpl) ---
    
    // Admin/Tác giả chỉnh sửa
    public void updateRecipe(Recipe recipe) {
        recipeDAO.save(recipe); // save() trong GenericDAOImpl xử lý cả update
    }

    
    public List<Recipe> getFilteredRecipes(String keyword, Long categoryId, Integer maxTime, Boolean hasVideo, Boolean isVip) {
        return recipeDAO.filterRecipes(keyword, categoryId, maxTime, hasVideo, isVip); // <--- THÊM isVip
    }
    
    /**
     * Lấy tất cả món ăn (dành cho Admin).
     */
    public List<Recipe> getAllRecipes() {
        // THAY ĐỔI: Sử dụng DAO mới để tránh LazyInitializationException
        return recipeDAO.findAllWithAuthor(); 
    }
    
    /**
     * Thêm/Cập nhật món ăn (Dùng cho cả tác giả Premium và Admin).
     */
    public void saveOrUpdateRecipe(Recipe recipe) {
        recipeDAO.save(recipe);
    }
    
    /**
     * Xóa món ăn theo ID (Dùng cho Admin).
     */
    
    public boolean deleteRecipe(Long recipeId) {
        // Kiểm tra tồn tại trước khi xóa
        Recipe recipe = recipeDAO.findById(recipeId);
        if (recipe != null) {
            recipeDAO.delete(recipe);
            return true;
        }
        return false;
    }
}