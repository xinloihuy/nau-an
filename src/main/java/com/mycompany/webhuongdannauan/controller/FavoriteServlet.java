package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.FavoriteDAO;
import com.mycompany.webhuongdannauan.dao.impl.FavoriteDAOImpl;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User; // SỬA 1: Import thêm model User
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
    // private Gson gson; // Không cần nữa
    
    @Override
    public void init() throws ServletException {
        favoriteDAO = new FavoriteDAOImpl();
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
            
            // Chuyển hướng người dùng về lại trang chi tiết công thức
            resp.sendRedirect(req.getContextPath() + "/recipe?id=" + recipeId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }
    }
    
}
