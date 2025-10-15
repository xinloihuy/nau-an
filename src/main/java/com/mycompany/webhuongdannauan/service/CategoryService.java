package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.CategoryDAO;
import com.mycompany.webhuongdannauan.dao.impl.CategoryDAOImpl;
import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import java.util.List;
import java.util.Map;

public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final RecipeService recipeService; // Dùng lại RecipeService để lấy món ăn

    public CategoryService() {
        this.categoryDAO = new CategoryDAOImpl();
        this.recipeService = new RecipeService();
    }

    /**
     * Lấy danh sách tất cả Category và số lượng món ăn cho Sidebar.
     */
    public Map<Category, Long> getAllCategoriesWithCount() {
        return categoryDAO.findAllCategoriesWithCount();
    }

    /**
     * Lấy các món ăn thuộc về một Category cụ thể.
     */
    public List<Recipe> getRecipesByCategoryId(Long categoryId) {
        return recipeService.getRecipesByCategory(categoryId); // Gọi lại phương thức đã có
    }
    
    /**
     * Tìm Category theo ID.
     */
    public Category getCategoryById(Long categoryId) {
        return categoryDAO.findById(categoryId);
    }
}