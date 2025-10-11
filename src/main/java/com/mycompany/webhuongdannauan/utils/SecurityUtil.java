package com.mycompany.webhuongdannauan.utils;

// Giả định sử dụng thư viện jBCrypt cho các phương thức hash
import org.mindrot.jbcrypt.BCrypt; 

public class SecurityUtil {

    // Số lần xử lý (work factor): Chi phí tính toán. 
    // Giá trị cao hơn (ví dụ: 12) an toàn hơn nhưng chậm hơn. 10 là mức chuẩn.
    private static final int HASH_ROUNDS = 10; 

    /**
     * Mã hóa mật khẩu thô (plain password) bằng thuật toán BCrypt.
     * @param plainPassword Mật khẩu thô từ người dùng.
     * @return Chuỗi mật khẩu đã được băm (hash).
     */
    public static String hashPassword(String plainPassword) {
        // Tự động tạo ra một 'salt' (chuỗi ngẫu nhiên) và băm mật khẩu
        String salt = BCrypt.gensalt(HASH_ROUNDS);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Xác minh mật khẩu thô có khớp với mật khẩu đã băm được lưu trong DB hay không.
     * @param plainPassword Mật khẩu thô người dùng nhập khi đăng nhập.
     * @param hashedPassword Mật khẩu đã băm được lưu trong database.
     * @return true nếu khớp, false nếu không khớp.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // BCrypt tự động trích xuất salt từ hashedPassword và so sánh
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /*
     * Lưu ý quan trọng: 
     * - KHÔNG BAO GIỜ lưu trữ mật khẩu thô (plain text).
     * - BCrypt tự động bao gồm salt trong chuỗi băm trả về.
     */
}