package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.service.RecipeService;
import com.mycompany.webhuongdannauan.service.UserService;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
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
    private final UserService userService = new UserService(); // Cần để kiểm tra Premium
    private final CategoryService categoryService = new CategoryService();

    private static final String HOME_VIEW = "/index.jsp"; // Đường dẫn của trang chủ

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        String keyword = req.getParameter("keyword"); // Lấy từ thanh tìm kiếm

        List<Recipe> recipesToShow;
        
        // 1. Xử lý Tìm Kiếm
        if (keyword != null && !keyword.isBlank()) {
            recipesToShow = recipeService.searchRecipes(keyword);
            req.setAttribute("searchQuery", keyword);
            // Có thể dùng một JSP khác để hiển thị kết quả tìm kiếm nếu muốn
        } else {
            // 2. Xử lý Hiển thị Mặc Định (Các món thịnh hành)
            // Giả định chúng ta chỉ lấy 8 món thịnh hành
            recipesToShow = recipeService.getFeaturedRecipes(8);
        }

        // 3. Tải Món VIP
        // Kiểm tra trạng thái Premium của người dùng hiện tại
        boolean isPremium = false;
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            Long userId = user.getId();
            User currentUser = userService.findUserById(userId);
            if (currentUser != null && userService.isUserPremium(currentUser)) {
                isPremium = true;
            }
        }
        
        List<Recipe> premiumRecipes = recipeService.getVipRecipes(isPremium);

        // 4. Đặt dữ liệu vào Request Scope
        session.setAttribute("featuredRecipes", recipesToShow);
        session.setAttribute("premiumRecipes", premiumRecipes);
        session.setAttribute("isPremiumUser", isPremium);
        session.setAttribute("categoriesWithCount", categoryService.getAllCategoriesWithCount());


        // 5. Chuyển tiếp tới trang chủ JSP
        req.getRequestDispatcher(HOME_VIEW).forward(req, resp);
    }
}