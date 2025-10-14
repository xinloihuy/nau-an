package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.User;
import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    // Khởi tạo Service để tương tác với Business Logic
    private final UserService userService = new UserService();
    
    // Đường dẫn gốc tới thư mục views (WEB-INF/views)
    private static final String REGISTER_VIEW = "/WEB-INF/views/auth/register.jsp";
    private static final String LOGIN_VIEW = "/WEB-INF/views/auth/login.jsp";

    // --- Phương thức GET: Hiển thị trang đăng ký ---
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Chuyển tiếp yêu cầu đến trang đăng ký JSP
        req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
    }

    // --- Phương thức POST: Xử lý dữ liệu đăng ký ---
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // 1. Lấy dữ liệu từ form
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String nickname = req.getParameter("nickname");
        String ageStr = req.getParameter("age");
        
        String forwardPath = REGISTER_VIEW; // Mặc định quay lại trang đăng ký nếu lỗi

        // 2. Kiểm tra dữ liệu bắt buộc (Validation cơ bản)
        if (username == null || password == null || email == null || 
            username.isBlank() || password.isBlank() || email.isBlank()) {
            req.setAttribute("error", "Vui lòng điền đầy đủ Tên đăng nhập, Mật khẩu và Email.");
            req.getRequestDispatcher(forwardPath).forward(req, resp);
            return;
        }

        Integer age = null;
        try {
            // 3. Xử lý và kiểm tra tuổi
            if (ageStr != null && !ageStr.isBlank()) {
                age = Integer.parseInt(ageStr);
                if (age < 12 || age > 100) { // Giả định giới hạn tuổi
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Tuổi không hợp lệ. Vui lòng nhập số nguyên hợp lệ.");
            req.getRequestDispatcher(forwardPath).forward(req, resp);
            return;
        }

        try {
            // 4. Tạo đối tượng User
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            // Nickname có thể null/blank, set giá trị nếu không blank
            if (nickname != null && !nickname.isBlank()) {
                newUser.setNickname(nickname);
            }
            newUser.setAge(age);
            
            // 5. Gọi tầng Service để xử lý logic (mã hóa, lưu DB, tạo AccountSetting)
            User registeredUser = userService.registerUser(newUser);

            // 6. Xử lý kết quả từ Service
            if (registeredUser == null) {
                // Logic trong UserService đã trả về null nếu username/email bị trùng
                req.setAttribute("error", "Email hoặc Tên đăng nhập đã tồn tại trong hệ thống.");
            } else {
                req.setAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
                forwardPath = LOGIN_VIEW; // Chuyển sang trang đăng nhập
            }
        } catch (RuntimeException e) {
            // Bắt các lỗi RuntimeException từ tầng DAO/Service (ví dụ: lỗi DB, lỗi Role không tìm thấy)
            System.err.println("Đã xảy ra lỗi khi đăng ký: " + e.getMessage());
            req.setAttribute("error", "Đã xảy ra lỗi hệ thống khi đăng ký. Vui lòng thử lại sau.");
        }
        
        // 7. Chuyển tiếp tới trang đích
        req.getRequestDispatcher(forwardPath).forward(req, resp);
    }
}