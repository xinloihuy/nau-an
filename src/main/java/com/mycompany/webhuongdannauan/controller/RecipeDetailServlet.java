package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.CommentDAO;
import com.mycompany.webhuongdannauan.dao.FavoriteDAO;
import com.mycompany.webhuongdannauan.dao.RatingDAO;
import com.mycompany.webhuongdannauan.dao.impl.CommentDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.FavoriteDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.RatingDAOImpl;
import com.mycompany.webhuongdannauan.model.Comment;
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
    
    // SỬA 2: Khai báo các DAO cần thiết để lấy dữ liệu tương tác
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final RatingDAO ratingDAO = new RatingDAOImpl();
    private final FavoriteDAO favoriteDAO = new FavoriteDAOImpl();
    
    private static final String RECIPE_DETAIL_VIEW = "/WEB-INF/views/recipe/recipe-detail.jsp";
    private static final String NOT_FOUND_VIEW = "/WEB-INF/views/error/404.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String recipeIdParam = req.getParameter("id");
        Long recipeId = null;
        
        // Lấy thông tin User từ Session (nếu có)
        HttpSession session = req.getSession(false);
        
        // SỬA 3: Lấy toàn bộ đối tượng User thay vì chỉ userId để nhất quán và dễ sử dụng hơn
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        Long userId = (currentUser != null) ? currentUser.getId() : null;

        try {
            // 1. Kiểm tra ID hợp lệ (CODE CŨ GIỮ NGUYÊN)
            if (recipeIdParam == null || recipeIdParam.isBlank()) {
                throw new NumberFormatException("ID món ăn không được cung cấp.");
            }
            recipeId = Long.parseLong(recipeIdParam);
            
            // 2. Lấy chi tiết Recipe (bao gồm cập nhật lượt xem và lịch sử) (CODE CŨ GIỮ NGUYÊN)
            Recipe recipe = recipeService.getRecipeDetails(recipeId, userId);
            
            if (recipe == null) {
                // Món ăn không tồn tại trong DB
                req.setAttribute("errorMessage", "Món ăn bạn tìm không tồn tại.");
                req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
                return;
            }

            // 3. Xử lý quyền truy cập (Nếu là món VIP) (CODE CŨ GIỮ NGUYÊN, tối ưu hóa một chút)
            boolean isPremiumUser = false;
            if (currentUser != null) {
                // Không cần tìm lại user vì đã có đối tượng currentUser từ session
                isPremiumUser = userService.isUserPremium(currentUser);
            }
            
            if (recipe.getIsVip() && !isPremiumUser) {
                // Nếu là món VIP và người dùng KHÔNG phải Premium
                // Tránh lộ công thức, chuyển hướng đến trang mời mua Premium
                resp.sendRedirect(req.getContextPath() + "/premium/plan?access_denied=vip_recipe");
                return;
            }

            // 4. Lấy các món liên quan (tối ưu trải nghiệm) (CODE CŨ GIỮ NGUYÊN)
            List<Recipe> relatedRecipes = recipeService.getRelatedRecipes(recipe);

            // =======================================================
            // SỬA 4: BỔ SUNG LOGIC TẢI DỮ LIỆU TƯƠNG TÁC
            // =======================================================
            // Lấy danh sách bình luận cho công thức
            List<Comment> comments = commentDAO.findByRecipeId(recipeId);
            
            // Lấy điểm đánh giá trung bình
            Double avgRating = ratingDAO.getAverageRating(recipeId);
            
            // Kiểm tra xem người dùng hiện tại đã yêu thích công thức này chưa
            boolean isFavorited = false;
            if (currentUser != null) {
                // Giả sử FavoriteDAO có phương thức isFavorited(userId, recipeId)
                // Nếu không, bạn cần thêm phương thức này vào DAO.
                isFavorited = favoriteDAO.existsByUserAndRecipe(userId, recipeId);
            }
            // =======================================================

            // 5. Đặt dữ liệu vào Request Scope
            req.setAttribute("recipe", recipe); // (CODE CŨ)
            req.setAttribute("relatedRecipes", relatedRecipes); // (CODE CŨ)
            
            // SỬA 5: Gửi thêm dữ liệu tương tác sang cho JSP
            req.setAttribute("comments", comments);
            req.setAttribute("avgRating", (avgRating != null) ? avgRating : 0.0);
            req.setAttribute("isFavorited", isFavorited);
            
            // 6. Chuyển tiếp tới trang chi tiết (CODE CŨ GIỮ NGUYÊN)
            req.getRequestDispatcher(RECIPE_DETAIL_VIEW).forward(req, resp);

        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu ID không phải là số
            req.setAttribute("errorMessage", "Đường dẫn không hợp lệ.");
            req.getRequestDispatcher(NOT_FOUND_VIEW).forward(req, resp);
        }
    }
}