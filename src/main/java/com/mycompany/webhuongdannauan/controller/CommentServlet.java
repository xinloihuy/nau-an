package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.dao.CommentDAO;
import com.mycompany.webhuongdannauan.dao.impl.CommentDAOImpl;
import com.mycompany.webhuongdannauan.model.Comment;
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
@WebServlet(name = "CommentServlet", urlPatterns = {"/comment"})
public class CommentServlet extends HttpServlet {
    
    private CommentDAO commentDAO;
    private EntityManagerFactory emf;
    // private Gson gson; // Không cần nữa
    
    @Override
    public void init() throws ServletException {
        commentDAO = new CommentDAOImpl();
        // Bạn cần thay "your-persistence-unit" bằng tên đúng trong file persistence.xml
        emf = Persistence.createEntityManagerFactory("CookingAppPU");
        // gson = new GsonBuilder()
        //         .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        //         .create();
    }
    
    // SỬA 3: Phương thức doGet không còn cần thiết cho luồng này nữa vì dữ liệu
    // comments sẽ được RecipeDetailServlet tải. Có thể xóa hoặc giữ lại nếu bạn dùng cho mục đích khác.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        // Chuyển hướng về trang chủ nếu có ai đó cố gắng truy cập URL này bằng GET
        resp.sendRedirect(req.getContextPath() + "/home");
    }
    
    /**
     * SỬA 4: Toàn bộ logic được dồn vào doPost để xử lý cả việc thêm và xóa comment
     * được gửi từ các form trong recipe-detail.jsp.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Luôn set encoding để xử lý tiếng Việt
        req.setCharacterEncoding("UTF-8");
        
        HttpSession session = req.getSession(false);
        
        // Bắt buộc người dùng phải đăng nhập
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String action = req.getParameter("action");
        String recipeIdStr = req.getParameter("recipeId");
        
        // Kiểm tra các tham số cơ bản
        if (action == null || recipeIdStr == null) {
            // Chuyển về trang lỗi hoặc trang chủ nếu thiếu thông tin
            resp.sendRedirect(req.getContextPath() + "/home"); 
            return;
        }

        try {
            long recipeId = Long.parseLong(recipeIdStr);

            if ("add".equals(action)) {
                String commentText = req.getParameter("commentText");
                if (commentText != null && !commentText.trim().isEmpty()) {
                    // Gọi service/DAO để lưu bình luận
                    // Bạn cần một phương thức để tạo comment, ví dụ:
                    // commentService.addComment(currentUser.getId(), recipeId, commentText);
                    
                    EntityManager em = emf.createEntityManager();
                    try {
                        Recipe recipe = em.find(Recipe.class, recipeId);
                        if (recipe != null) {
                            Comment newComment = new Comment();
                            newComment.setUser(currentUser);
                            newComment.setRecipe(recipe);
                            newComment.setContent(commentText);
                            commentDAO.create(newComment);
                        }
                    } finally {
                        em.close();
                    }
                }
            } else if ("delete".equals(action)) {
                String commentIdStr = req.getParameter("commentId");
                if (commentIdStr != null) {
                    long commentId = Long.parseLong(commentIdStr);
                    // Gọi service/DAO để xóa bình luận
                    // Nên có một bước kiểm tra xem `currentUser` có phải là chủ của comment này không
                    commentDAO.delete(commentId);
                }
            }

            // SỬA 5: Chuyển hướng người dùng về lại trang chi tiết công thức sau khi xử lý xong
            resp.sendRedirect(req.getContextPath() + "/recipe?id=" + recipeId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Nếu ID không hợp lệ, chuyển hướng về trang chủ
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi khác, chuyển hướng đến trang lỗi chung
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