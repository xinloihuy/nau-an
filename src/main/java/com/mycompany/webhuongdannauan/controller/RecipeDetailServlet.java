package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
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

@WebServlet("/recipe") // <--- ÁNH XẠ VỚI URL /recipe
public class RecipeDetailServlet extends HttpServlet {

    private final RecipeService recipeService = new RecipeService();
    private final UserService userService = new UserService();
    
    private static final String RECIPE_DETAIL_VIEW = "/WEB-INF/views/recipe/recipe-detail.jsp";
    private static final String NOT_FOUND_VIEW = "/WEB-INF/views/error/404.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String recipeIdParam = req.getParameter("id");
        Long recipeId = null;
        
        // Lấy thông tin User từ Session (nếu có)
        HttpSession session = req.getSession(false);
        Long userId = (session != null) ? (Long) session.getAttribute("userId") : null;

        try {
            // 1. Kiểm tra ID hợp lệ
            if (recipeIdParam == null || recipeIdParam.isBlank()) {
                throw new NumberFormatException("ID món ăn không được cung cấp.");
            }
            recipeId = Long.parseLong(recipeIdParam);
            
            // 2. Lấy chi tiết Recipe (bao gồm cập nhật lượt xem và lịch sử)
            Recipe recipe = recipeService.getRecipeDetails(recipeId, userId);
            
            if (recipe == null) {
                // Món ăn không tồn tại trong DB
                req.setAttribute("errorMessage", "Món ăn bạn tìm không tồn tại.");
                req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
                return;
            }

            // 3. Xử lý quyền truy cập (Nếu là món VIP)
            boolean isPremiumUser = false;
            if (userId != null) {
                User user = userService.findUserById(userId);
                if (user != null) {
                    isPremiumUser = userService.isUserPremium(user);
                }
            }
            
            if (recipe.getIsVip() && !isPremiumUser) {
                // Nếu là món VIP và người dùng KHÔNG phải Premium
                // Tránh lộ công thức, chuyển hướng đến trang mời mua Premium
                resp.sendRedirect(req.getContextPath() + "/premium/plan?access_denied=vip_recipe");
                return;
            }

            // 4. Lấy các món liên quan (tối ưu trải nghiệm)
            List<Recipe> relatedRecipes = recipeService.getRelatedRecipes(recipe);

            // 5. Đặt dữ liệu vào Request Scope
            req.setAttribute("recipe", recipe);
            req.setAttribute("relatedRecipes", relatedRecipes);
            
            // 6. Chuyển tiếp tới trang chi tiết
            req.getRequestDispatcher(RECIPE_DETAIL_VIEW).forward(req, resp);

        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu ID không phải là số
            req.setAttribute("errorMessage", "Đường dẫn không hợp lệ.");
            req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
        }
    }
}