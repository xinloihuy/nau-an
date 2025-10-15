package com.mycompany.webhuongdannauan.test;

import com.mycompany.webhuongdannauan.dao.impl.CommentDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.RecipeDAOImpl;
import com.mycompany.webhuongdannauan.model.Comment;
import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.model.Recipe;
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
        Long existingUserId = 1L;   // Thay bằng ID User có sẵn trong DB
        Long existingRecipeId = 5L; // Thay bằng ID Recipe có sẵn trong DB

        // Tải các đối tượng tham chiếu
        User commentAuthor = userDAO.findById(existingUserId);
        Recipe targetRecipe = recipeDAO.findById(existingRecipeId);

        if (commentAuthor == null || targetRecipe == null) {
            System.err.println("❌ Không tìm thấy User hoặc Recipe với ID đã cho. Kiểm tra lại dữ liệu trong DB.");
            return;
        }

        System.out.println("🔹 Bắt đầu kiểm thử CommentDAO...");

        // 🟢 1. Tạo mới một Comment
        Comment newComment = new Comment();
        newComment.setContent("Món này ngon quá! Sẽ thử vào cuối tuần.");
        newComment.setUser(commentAuthor);
        newComment.setRecipe(targetRecipe);

        // ✅ Gọi đúng hàm create()
        commentDAO.create(newComment);
        System.out.println("✅ Đã lưu Comment mới với ID: " + newComment.getId());

        // ----------------------------------------------------
        // 🟡 2. Tìm theo ID
        Long savedCommentId = newComment.getId();
        Comment found = commentDAO.findById(savedCommentId);

        if (found != null) {
            System.out.println("🔍 Tìm được comment: " + found.getContent());
            System.out.println("  - Tác giả: " + found.getUser().getUsername());
            System.out.println("  - Món ăn: " + found.getRecipe().getTitle());
        } else {
            System.out.println("⚠️ Không tìm thấy comment vừa tạo.");
        }

        // ----------------------------------------------------
        // 🔵 3. Lấy tất cả comment
        System.out.println("\n--- DANH SÁCH TẤT CẢ COMMENT ---");
        List<Comment> allComments = commentDAO.findAll();
        allComments.forEach(cm -> {
            System.out.println("Comment ID " + cm.getId() + ": " + cm.getContent());
        });
        System.out.println("📊 Tổng số comments: " + allComments.size());

        // ----------------------------------------------------
        // 🔴 4. Xóa comment
        if (found != null) {
            commentDAO.delete(found.getId()); // ✅ Truyền ID thay vì đối tượng
            System.out.println("\n🗑️ Đã xóa comment có ID: " + savedCommentId);
        }

        // Kiểm tra lại sau khi xóa
        Comment deletedCheck = commentDAO.findById(savedCommentId);
        if (deletedCheck == null) {
            System.out.println("✅ Xóa thành công!");
        } else {
            System.out.println("⚠️ Comment vẫn còn trong DB!");
        }

        // ✅ Đã loại bỏ commentDAO.close() vì không còn cần thiết
        System.out.println("🎉 Test hoàn thành!");
    }
}
