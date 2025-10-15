package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.CategoryDAO;
import com.mycompany.webhuongdannauan.dao.impl.CategoryDAOImpl;
import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    public Set<Category> getCategoriesByIds(String[] idStrings) {
        if (idStrings == null || idStrings.length == 0) {
            return Collections.emptySet();
        }
        
        return Arrays.stream(idStrings)
            .map(Long::parseLong)
            .map(categoryDAO::findById) // Sử dụng findById của GenericDAOImpl
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());
    }
}