package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.RatingDAO;
import com.mycompany.webhuongdannauan.dao.impl.RatingDAOImpl;
import com.mycompany.webhuongdannauan.model.Rating;
import com.mycompany.webhuongdannauan.model.Recipe;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// SỬA 2: Đơn giản hóa URL pattern để khớp với action của form trong JSP
@WebServlet(name = "RatingServlet", urlPatterns = {"/rate"})
public class RatingServlet extends HttpServlet {
    
    private RatingDAO ratingDAO;
    private EntityManagerFactory emf;
    // private Gson gson; // Không cần nữa
    
    @Override
    public void init() throws ServletException {
        ratingDAO = new RatingDAOImpl();
        // Bạn cần thay "your-persistence-unit" bằng tên đúng trong file persistence.xml
        emf = Persistence.createEntityManagerFactory("CookingAppPU");
        // gson = new GsonBuilder()
        //         .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        //         .create();
    }
    
    // SỬA 3: Phương thức doGet không còn cần thiết cho luồng này nữa.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Chuyển hướng về trang chủ nếu có ai đó cố gắng truy cập URL này bằng GET
        resp.sendRedirect(req.getContextPath() + "/home");
    }
    
    /**
     * SỬA 4: Viết lại doPost để xử lý việc gửi đánh giá từ form
     * và chuyển hướng người dùng trở lại.
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
        String recipeIdStr = req.getParameter("recipeId");
        // Tên tham số này đã được đổi trong JSP để nhất quán
        String ratingValueStr = req.getParameter("ratingValue");

        if (recipeIdStr == null || ratingValueStr == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            long recipeId = Long.parseLong(recipeIdStr);
            int score = Integer.parseInt(ratingValueStr);

            // Validate score (1-5)
            if (score < 1 || score > 5) {
                // Nếu điểm không hợp lệ, chỉ cần chuyển hướng về trang công thức
                resp.sendRedirect(req.getContextPath() + "/recipe?id=" + recipeId);
                return;
            }

            // Tạo hoặc cập nhật rating
            EntityManager em = emf.createEntityManager();
            try {
                Recipe recipe = em.find(Recipe.class, recipeId);
                
                if (recipe != null) {
                    Rating rating = new Rating();
                    rating.setUser(currentUser);
                    rating.setRecipe(recipe);
                    rating.setScore(score);
                    
                    // Phương thức này của bạn sẽ tạo mới hoặc cập nhật nếu đã tồn tại
                    ratingDAO.createOrUpdate(rating);
                }
            } finally {
                em.close();
            }

            // SỬA 5: Chuyển hướng người dùng về lại trang chi tiết công thức sau khi xử lý xong
            resp.sendRedirect(req.getContextPath() + "/recipe?id=" + recipeId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }
    }
    
    
    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}