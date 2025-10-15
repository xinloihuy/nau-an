package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.CategoryService;
import com.mycompany.webhuongdannauan.service.RecipeService;
import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();
    private final RecipeService recipeService = new RecipeService();
    private final UserService userService = new UserService();
    
    // Sử dụng lại trang chủ để hiển thị kết quả theo format trang chính
    private static final String CATEGORY_VIEW = "/index.jsp"; 
    private static final String NOT_FOUND_VIEW = "/WEB-INF/views/error/404.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String categoryIdParam = req.getParameter("id");
        HttpSession session = req.getSession(false);

        // --- 1. Xác định Trạng thái User và Quyền Premium ---
        boolean isPremium = false;
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            // Tải lại user để có dữ liệu Premium mới nhất
            User freshUser = userService.findUserById(user.getId());
            if (freshUser != null) {
                isPremium = userService.isUserPremium(freshUser);
            }
        }
        
        try {
            if (categoryIdParam == null || categoryIdParam.isBlank()) {
                 throw new NumberFormatException("ID danh mục không được cung cấp.");
            }
            Long categoryId = Long.parseLong(categoryIdParam);

            // 2. Lấy Category, kiểm tra tồn tại
            Category category = categoryService.getCategoryById(categoryId);
            
            if (category == null) {
                req.setAttribute("errorMessage", "Danh mục không tồn tại.");
                req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
                return;
            }

            // 3. Lấy danh sách món ăn theo Category (CHỈ MÓN THƯỜNG: Đã sửa ở Service/DAO)
            List<Recipe> recipes = categoryService.getRecipesByCategoryId(categoryId);

            // 4. Lấy Món VIP (Món có isVip=true, dùng cho phần tĩnh của trang index)
            List<Recipe> premiumRecipes = recipeService.getVipRecipes();


            // 5. Đặt dữ liệu vào Request Scope (Dữ liệu hiển thị)
            req.setAttribute("featuredRecipes", recipes); // Danh sách món thường (theo danh mục)
            req.setAttribute("categoryTitle", category.getName()); // Tiêu đề hiển thị trên trang
            
            // Đặt các thuộc tính cần thiết cho index.jsp:
            req.setAttribute("premiumRecipes", premiumRecipes);     // Danh sách món VIP (isVip=true)
            req.setAttribute("isPremiumUser", isPremium);          // Cờ Premium
            req.setAttribute("categoriesWithCount", categoryService.getAllCategoriesWithCount()); // Dữ liệu Sidebar

            // 6. Chuyển tiếp
            req.getRequestDispatcher(CATEGORY_VIEW).forward(req, resp);

        } catch (NumberFormatException e) {
            System.err.println("Lỗi đường dẫn danh mục: " + e.getMessage());
            req.setAttribute("errorMessage", "Đường dẫn danh mục không hợp lệ.");
            req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
        }
    }
}