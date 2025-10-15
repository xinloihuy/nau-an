package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.service.RecipeService;
import com.mycompany.webhuongdannauan.service.CategoryService;
import com.mycompany.webhuongdannauan.model.Category;
import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/admin/recipes")
public class AdminRecipeServlet extends HttpServlet {

    private final RecipeService recipeService = new RecipeService();
    private final CategoryService categoryService = new CategoryService();
    private final UserService userService = new UserService();
    
    private static final String RECIPE_LIST_VIEW = "/WEB-INF/views/admin/admin-recipe.jsp";
    private static final String RECIPE_FORM_VIEW = "/WEB-INF/views/admin/recipe-form.jsp";
    private static final String ADMIN_LOGIN_URL = "/admin/login";

    // --- 1. GET: Hiển thị Danh sách hoặc Form Thêm/Sửa ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // KIỂM TRA QUYỀN ADMIN
        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL);
            return;
        }

        String action = request.getParameter("action");
        String recipeIdParam = request.getParameter("id");
        
        // Luôn tải Category cho Form
        request.setAttribute("allCategories", categoryService.getAllCategoriesWithCount().keySet());

       try {
        if ("edit".equals(action)) {
            
            if (recipeIdParam != null && !recipeIdParam.isBlank()) {
                // TRƯỜNG HỢP 1: Xử lý Form SỬA (action=edit VÀ id có)
                Long recipeId = Long.parseLong(recipeIdParam);
                Recipe recipe = recipeService.getRecipeDetails(recipeId, null); 
                request.setAttribute("recipe", recipe);
                request.setAttribute("mode", "EDIT"); // <--- ĐẶT FLAG MODE
            } else {
                // TRƯỜNG HỢP 2: Xử lý Form THÊM MỚI (action=edit NHƯNG id KHÔNG CÓ)
                // Đơn giản là forward form trống.
                request.setAttribute("recipe", new Recipe()); // Thiết lập object Recipe trống
                request.setAttribute("mode", "ADD"); // <--- ĐẶT FLAG MODE
            }

            // Forward đến form (chỉ thực hiện ở đây)
            request.getRequestDispatcher(RECIPE_FORM_VIEW).forward(request, response);
            return;
        } 
            
            if ("delete".equals(action) && recipeIdParam != null) {
                // Xử lý Xóa
                Long recipeId = Long.parseLong(recipeIdParam);
                recipeService.deleteRecipe(recipeId);
                request.setAttribute("message", "Đã xóa món ăn ID: " + recipeId);
                // Chuyển hướng về trang danh sách (POST/REDIRECT/GET)
                response.sendRedirect(request.getContextPath() + "/admin/recipes");
                return;
            }
            
            // Mặc định: Hiển thị Danh sách món ăn
            List<Recipe> recipeList = recipeService.getAllRecipes();
            request.setAttribute("recipeList", recipeList);
            request.getRequestDispatcher(RECIPE_LIST_VIEW).forward(request, response);

        } catch (Exception e) {
            System.err.println("Lỗi xử lý Admin Recipe GET: " + e.getMessage());
            request.setAttribute("error", "Lỗi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher(RECIPE_LIST_VIEW).forward(request, response);
        }
    }

    // --- 2. POST: Xử lý Form Thêm/Sửa ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL);
            return;
        }
        
        // Đảm bảo encoding
        request.setCharacterEncoding("UTF-8"); 

        // Lấy dữ liệu từ form
        String recipeIdParam = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String ingredients = request.getParameter("ingredients");
        String steps = request.getParameter("steps");
        String imageUrl = request.getParameter("imageUrl");
        String videoUrl = request.getParameter("videoUrl");
        String isVipParam = request.getParameter("isVip");
        String cookingTimeStr = request.getParameter("cookingTimeMinutes");
        String[] categoryIds = request.getParameterValues("categories"); // Lấy danh sách ID Category

        Recipe recipe = null;
        try {
            // Lấy hoặc tạo mới Recipe
            if (recipeIdParam != null && !recipeIdParam.isBlank()) {
                recipe = recipeService.getRecipeDetails(Long.parseLong(recipeIdParam), null);
            } else {
                recipe = new Recipe();
                // Admin tự động là tác giả cho món ăn Admin tạo
                // Giả định Admin là User ID 1 (hoặc User trong session)
                recipe.setAuthor(userService.findUserById(1L)); 
            }

            // Cập nhật thuộc tính
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setIngredients(ingredients);
            recipe.setSteps(steps);
            recipe.setImageUrl(imageUrl);
            recipe.setVideoUrl(videoUrl);
            recipe.setIsVip("on".equals(isVipParam) || "true".equalsIgnoreCase(isVipParam));
            recipe.setCookingTimeMinutes(Integer.parseInt(cookingTimeStr));
            
            // Xử lý Category
            if (categoryIds != null) {
                Set<Category> categories = categoryService.getCategoriesByIds(categoryIds);
                recipe.setCategories(categories);
            }
            
            // Lưu vào database
            recipeService.saveOrUpdateRecipe(recipe);
            
            request.setAttribute("message", "Lưu món ăn thành công!");
            response.sendRedirect(request.getContextPath() + "/admin/recipes");

        } catch (Exception e) {
            System.err.println("Lỗi xử lý Admin Recipe POST: " + e.getMessage());
            request.setAttribute("error", "Lỗi xử lý form: " + e.getMessage());
            request.setAttribute("recipe", recipe); // Giữ lại dữ liệu đã nhập
            request.getRequestDispatcher(RECIPE_FORM_VIEW).forward(request, response);
        }
    }
    
    // Phương thức kiểm tra quyền Admin
    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("isAdmin") != null && (boolean)session.getAttribute("isAdmin");
    }
}