package com.mycompany.webhuongdannauan.test;

import com.mycompany.webhuongdannauan.dao.impl.CommentDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.RecipeDAOImpl;
import com.mycompany.webhuongdannauan.model.Comment;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.model.Recipe;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

public class TestCommentDAO {

    public static void main(String[] args) {
        // Khởi tạo các DAO cần thiết
        CommentDAOImpl commentDAO = new CommentDAOImpl();
        UserDAOImpl userDAO = new UserDAOImpl();
        RecipeDAOImpl recipeDAO = new RecipeDAOImpl();

        // ----------------------------------------------------
        // GIẢ ĐỊNH DỮ LIỆU: Cần phải tải đối tượng User và Recipe
        // ----------------------------------------------------
        Long existingUserId = 1L; // Thay bằng ID User có sẵn trong DB của bạn
        Long existingRecipeId = 5L; // Thay bằng ID Recipe có sẵn trong DB của bạn

        // Tải các đối tượng tham chiếu (Sử dụng findById từ DAO)
        User commentAuthor = userDAO.findById(existingUserId);
        Recipe targetRecipe = recipeDAO.findById(existingRecipeId);

        if (commentAuthor == null || targetRecipe == null) {
            System.err.println("Lỗi: Không tìm thấy User hoặc Recipe với ID đã cho. Vui lòng kiểm tra dữ liệu.");
            return;
        }

        System.out.println("Bắt đầu kiểm thử CommentDAO...");

        // 🟢 1. Tạo mới một Comment
        Comment newComment = new Comment();
        newComment.setContent("Món này ngon quá! Sẽ thử vào cuối tuần.");
        
        // SỬA LỖI: Thiết lập đối tượng User và Recipe thay vì ID
        newComment.setUser(commentAuthor); 
        newComment.setRecipe(targetRecipe); 
        
        commentDAO.save(newComment); 
        System.out.println("Đã lưu Comment mới với ID: " + newComment.getId());

        // ----------------------------------------------------
        
        // 🟡 2. Tìm theo ID
        Long savedCommentId = newComment.getId();
        Comment found = commentDAO.findById(savedCommentId);
        
        if (found != null) {
             // Để truy cập User/Recipe, Hibernate có thể cần Entity Manager vẫn mở.
             // Dù vậy, findById thường trả về đối tượng đã được detach.
             // Nếu lỗi LazyInitializationException xảy ra, bạn cần fetch sớm hơn hoặc xử lý trong cùng transaction.
             System.out.println("Tìm được comment: " + found.getContent());
             System.out.println("  - Tác giả: " + found.getUser().getUsername()); 
             System.out.println("  - Món ăn: " + found.getRecipe().getTitle());
        } else {
             System.out.println("Lỗi: Không tìm thấy comment vừa tạo.");
        }


        // 🔵 3. Lấy tất cả comment
        System.out.println("\n--- DANH SÁCH TẤT CẢ COMMENT ---");
        List<Comment> allComments = commentDAO.findAll();
        allComments.forEach(cm -> {
            // Cẩn thận với LazyInitializationException nếu truy cập User/Recipe ở đây
            System.out.println("Comment ID " + cm.getId() + ": " + cm.getContent());
        });
        System.out.println("Tổng số comments: " + allComments.size());

        // 🔴 4. Xóa comment
        if (found != null) {
            commentDAO.delete(found);
            System.out.println("\nĐã xóa comment có ID: " + savedCommentId);
        }
        
        // Kiểm tra lại sau khi xóa
        Comment deletedCheck = commentDAO.findById(savedCommentId);
        if (deletedCheck == null) {
             System.out.println("Xóa thành công!");
        }
    }
}