package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.FavoriteDAO;
import com.mycompany.webhuongdannauan.dao.impl.FavoriteDAOImpl;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User; // SỬA 1: Import thêm model User
import com.mycompany.webhuongdannauan.dao.RatingDAO;
import com.mycompany.webhuongdannauan.dao.impl.RatingDAOImpl;
import com.mycompany.webhuongdannauan.dto.FavoriteRecipeInfo;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;


// SỬA 3: Cập nhật URL Patterns. Giữ lại "/favorites" cho trang danh sách yêu thích
// và thêm "/favorite" cho hành động thêm/xóa từ trang chi tiết.
@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorite", "/favorites"})
public class FavoriteServlet extends HttpServlet {
    
    private FavoriteDAO favoriteDAO;
    private RatingDAO ratingDAO;
    // private Gson gson; // Không cần nữa
    
    @Override
    public void init() throws ServletException {
        favoriteDAO = new FavoriteDAOImpl();
        ratingDAO = new RatingDAOImpl();
        // gson = new GsonBuilder()
        //         .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        //         .create();
    }
    
    /**
     * SỬA 4: Giữ nguyên logic xử lý trang danh sách yêu thích (/favorites).
     * Loại bỏ phần xử lý API trả về JSON không còn cần thiết.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String path = req.getServletPath();
        
        // Luồng này để hiển thị trang JSP liệt kê các món ăn yêu thích của người dùng.
        // Phần code này được GIỮ NGUYÊN vì nó phục vụ chức năng khác.
        if ("/favorites".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
            
            // Lấy User object từ session để đảm bảo tính nhất quán
            User currentUser = (User) session.getAttribute("user");
            
            // Load favorites data từ database
            List<Recipe> favorites = favoriteDAO.findFavoriteRecipesByUserId(currentUser.getId());
            req.setAttribute("favorites", favorites);
            
            // Chuyển tiếp đến trang JSP để hiển thị danh sách
            req.getRequestDispatcher("/WEB-INF/views/interaction/favorites.jsp").forward(req, resp);
            return;
        }
        
        // Nếu người dùng truy cập /favorite bằng GET, chỉ cần chuyển hướng họ đi
        resp.sendRedirect(req.getContextPath() + "/home");
    }
    
    /**
     * SỬA 5: Viết lại hoàn toàn phương thức doPost để xử lý thêm/xóa favorite
     * và chuyển hướng thay vì trả về JSON.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Bắt buộc người dùng phải đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String action = req.getParameter("action");
        String recipeIdStr = req.getParameter("recipeId");

        if (action == null || recipeIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            long recipeId = Long.parseLong(recipeIdStr);
            long userId = currentUser.getId();

            if ("add".equals(action)) {
                // Giả sử DAO có phương thức add, nếu không bạn có thể dùng toggle
                // favoriteDAO.add(userId, recipeId); 
                favoriteDAO.toggle(userId, recipeId); // Hoặc dùng toggle nếu nó xử lý đúng logic thêm
            } else if ("remove".equals(action)) {
                // Giả sử DAO có phương thức remove
                // favoriteDAO.remove(userId, recipeId);
                favoriteDAO.toggle(userId, recipeId); // Hoặc dùng toggle nếu nó xử lý đúng logic xóa
            }
            
            // Lấy thông tin trang cần redirect đến
            String redirectTo = req.getParameter("redirect_to");
            String redirectURL;

            if ("favorites".equals(redirectTo)) {
                // Nếu yêu cầu đến từ trang favorites, quay lại trang favorites
                redirectURL = req.getContextPath() + "/favorites";
            } else {
                // Mặc định, quay lại trang chi tiết công thức
                redirectURL = req.getContextPath() + "/recipe?id=" + recipeId;
            }

            // Thực hiện chuyển hướng
            resp.sendRedirect(redirectURL);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }
    }
    
}
