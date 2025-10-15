package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.CategoryDAO;
import com.mycompany.webhuongdannauan.dao.RecipeDAO; // Thêm import RecipeDAO
import com.mycompany.webhuongdannauan.dao.impl.CategoryDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.RecipeDAOImpl; // Thêm import RecipeDAOImpl
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
    private final RecipeDAO recipeDAO; // <-- THÊM: Dùng DAO để truy vấn Recipe
    // Loại bỏ: private final RecipeService recipeService;

    public CategoryService() {
        this.categoryDAO = new CategoryDAOImpl();
        this.recipeDAO = new RecipeDAOImpl(); // Khởi tạo RecipeDAO
    }

    /**
     * Lấy danh sách tất cả Category và số lượng món ăn cho Sidebar.
     */
    public Map<Category, Long> getAllCategoriesWithCount() {
        return categoryDAO.findAllCategoriesWithCount();
    }

    /**
     * Tìm Category theo ID.
     */
    public Category getCategoryById(Long categoryId) {
        return categoryDAO.findById(categoryId);
    }
    
    // ----------------------------------------------------
    // --- LỚP NÀY ĐÃ ĐƯỢC CHUYỂN LOGIC ĐỂ GỌI DAO MỚI ---
    // ----------------------------------------------------
    
    /**
     * Lấy các món ăn thuộc về một Category cụ thể.
     * Phương thức này CHỈ trả về món THƯỜNG (isVip = false) để phân tách.
     */
    public List<Recipe> getRecipesByCategoryId(Long categoryId) {
        // GỌI TRUY VẤN DAO CHUYÊN BIỆT ĐÃ LỌC isVip=false
        // Giả định RecipeDAO có phương thức này
        return recipeDAO.findNonVipByCategory(categoryId); 
    }
    
    /**
     * Lấy tập hợp Category từ một mảng ID (dùng cho form Admin).
     */
    public Set<Category> getCategoriesByIds(String[] idStrings) {
        if (idStrings == null || idStrings.length == 0) {
            return Collections.emptySet();
        }
        
        // Sử dụng findById (được kế thừa bởi CategoryDAOImpl)
        return Arrays.stream(idStrings)
            .map(Long::parseLong)
            .map(categoryDAO::findById) 
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());
    }
}