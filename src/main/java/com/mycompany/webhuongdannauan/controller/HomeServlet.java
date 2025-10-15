package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.service.RecipeService;
import com.mycompany.webhuongdannauan.service.UserService;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet({"/home"})
public class HomeServlet extends HttpServlet {

    private final RecipeService recipeService = new RecipeService();
    private final UserService userService = new UserService();
    private final CategoryService categoryService = new CategoryService();

    private static final String HOME_VIEW = "/index.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        String keyword = req.getParameter("keyword");

        List<Recipe> recipesToShow;
        boolean isPremium = false;
        
        // 1. Kiểm tra trạng thái Premium (Dùng để quyết định quyền truy cập món VIP)
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            // Tải lại User (hoặc chỉ cần ID) để kiểm tra trạng thái Premium mới nhất
            User currentUser = userService.findUserById(user.getId());
            if (currentUser != null && userService.isUserPremium(currentUser)) {
                isPremium = true;
            }
        }
        
        // 2. Xử lý Món Thường / Tìm kiếm (isVip=false)
        if (keyword != null && !keyword.isBlank()) {
            // Giả định searchNonVipRecipes() đã được sửa để chỉ tìm món thường
            recipesToShow = recipeService.searchNonVipRecipes(keyword);
            req.setAttribute("searchQuery", keyword);
        } else {
            // Món thịnh hành (chỉ lấy món thường isVip=false)
            recipesToShow = recipeService.getFeaturedRecipes(8);
        }

        // 3. Tải Món VIP (isVip=true)
        // Luôn tải danh sách này. Logic hiển thị khóa/mở sẽ nằm ở JSP.
        List<Recipe> premiumRecipes = recipeService.getVipRecipes(); 

        // 4. Đặt dữ liệu vào Request Scope (ĐÃ SỬA LỖI)
        // Dữ liệu này chỉ cần tồn tại cho lần hiển thị trang này
        session.setAttribute("featuredRecipes", recipesToShow);
        session.setAttribute("premiumRecipes", premiumRecipes);
        req.setAttribute("isPremiumUser", isPremium);
        session.setAttribute("categoriesWithCount", categoryService.getAllCategoriesWithCount());

        // 5. Chuyển tiếp tới trang chủ JSP
        req.getRequestDispatcher(HOME_VIEW).forward(req, resp);
    }
}