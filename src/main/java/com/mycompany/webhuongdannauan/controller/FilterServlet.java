package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.service.CategoryService;
import com.mycompany.webhuongdannauan.service.RecipeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/filter")
public class FilterServlet extends HttpServlet {

    private final RecipeService recipeService = new RecipeService();
    private final CategoryService categoryService = new CategoryService();
    
    private static final String FILTER_FORM_VIEW = "/WEB-INF/views/filter/filter-form.jsp";
    private static final String SEARCH_RESULTS_VIEW = "/index.jsp"; // Tái sử dụng trang chủ/kết quả

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // 1. Lấy tất cả Category để hiển thị trong dropdown của form lọc
        Map<Category, Long> categories = categoryService.getAllCategoriesWithCount();
        req.setAttribute("allCategories", categories.keySet());

        // 2. Kiểm tra nếu có tham số lọc được gửi đến
        String keyword = req.getParameter("keyword");
        String categoryIdParam = req.getParameter("category");
        String maxTimeParam = req.getParameter("maxTime");
        String hasVideoParam = req.getParameter("hasVideo");

        if (keyword != null || categoryIdParam != null || maxTimeParam != null || hasVideoParam != null) {
            // Xử lý Lọc
            Long categoryId = (categoryIdParam != null && !categoryIdParam.isBlank()) ? Long.parseLong(categoryIdParam) : null;
            Integer maxTime = (maxTimeParam != null && !maxTimeParam.isBlank()) ? Integer.parseInt(maxTimeParam) : null;
            Boolean hasVideo = (hasVideoParam != null) ? Boolean.parseBoolean(hasVideoParam) : null;

            List<Recipe> filteredRecipes = recipeService.getFilteredRecipes(keyword, categoryId, maxTime, hasVideo);
            
            // Đặt kết quả vào Request Scope
            req.setAttribute("featuredRecipes", filteredRecipes); // Sử dụng lại tên này
            req.setAttribute("searchQuery", "Bộ lọc Nâng cao"); 
            
            // Forward tới trang hiển thị kết quả
            req.getRequestDispatcher(SEARCH_RESULTS_VIEW).forward(req, resp);
            return;
        }

        // 3. Hiển thị form lọc lần đầu
        req.getRequestDispatcher(FILTER_FORM_VIEW).forward(req, resp);
    }
}