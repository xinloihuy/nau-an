package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();
    // Sử dụng lại HOME_VIEW để hiển thị kết quả theo format trang chủ
    private static final String CATEGORY_VIEW = "/index.jsp"; 
    private static final String NOT_FOUND_VIEW = "/WEB-INF/views/error/404.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String categoryIdParam = req.getParameter("id");

        try {
            Long categoryId = Long.parseLong(categoryIdParam);

            // 1. Lấy Category để hiển thị tiêu đề
            Category category = categoryService.getCategoryById(categoryId);
            
            if (category == null) {
                req.setAttribute("errorMessage", "Danh mục không tồn tại.");
                req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
                return;
            }

            // 2. Lấy danh sách món ăn
            List<Recipe> recipes = categoryService.getRecipesByCategoryId(categoryId);

            // 3. Đặt dữ liệu vào Request Scope
            req.setAttribute("featuredRecipes", recipes); // Sử dụng lại tên thuộc tính của trang chủ
            req.setAttribute("categoryTitle", category.getName()); // Tiêu đề để hiển thị
            req.setAttribute("categoriesWithCount", categoryService.getAllCategoriesWithCount()); 

            // 4. Chuyển tiếp tới trang chủ (hoặc trang danh sách món ăn riêng)
            req.getRequestDispatcher(CATEGORY_VIEW).forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Đường dẫn danh mục không hợp lệ.");
            req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
        }
    }
}