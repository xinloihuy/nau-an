package com.mycompany.webhuongdannauan.dao.impl;

import com.mycompany.webhuongdannauan.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Lớp này chịu trách nhiệm thiết lập các trigger trong cơ sở dữ liệu
 * bằng cách đọc và thực thi một file .sql từ thư mục resources.
 * Nó được thiết kế để chạy một lần khi ứng dụng khởi động.
 */
public class TriggerSetupDAOImpl {

    /**
     * Đọc file 'triggers.sql' từ resources và thực thi các lệnh SQL bên trong.
     * Phương thức này xử lý các lệnh DELIMITER để có thể định nghĩa các trigger phức tạp.
     */
    public void setupTriggers() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        // Sử dụng try-with-resources để đảm bảo InputStream được đóng tự động
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("triggers.sql")) {

            if (is == null) {
                System.err.println("FATAL ERROR: Could not find 'triggers.sql' in resources folder.");
                return;
            }

            // Dùng Scanner để đọc toàn bộ nội dung file
            // Chúng ta sẽ tự xử lý delimiter thay vì dùng scanner.useDelimiter()
            // để kiểm soát tốt hơn.
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
            String sqlScript = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();

            // Tách các lệnh SQL dựa trên delimiter tùy chỉnh là '$$'
            // và sau đó là dấu chấm phẩy ';' cho các lệnh đơn giản
            String[] commands = sqlScript.split("\\$\\$");

            transaction.begin();

            for (String command : commands) {
                String trimmedCommand = command.trim();
                
                // Bỏ qua các chuỗi rỗng
                if (trimmedCommand.isEmpty()) {
                    continue;
                }

                // Loại bỏ các lệnh DELIMITER khỏi chuỗi lệnh
                if (trimmedCommand.toUpperCase().startsWith("DELIMITER")) {
                    continue;
                }
                
                System.out.println("Executing SQL Command: " + trimmedCommand.substring(0, Math.min(trimmedCommand.length(), 80)).replace("\n", " ") + "...");
                
                // Thực thi lệnh SQL gốc
                em.createNativeQuery(trimmedCommand).executeUpdate();
            }

            transaction.commit();
            System.out.println("✅ All database triggers have been set up successfully from 'triggers.sql'.");

        } catch (Exception e) {
            System.err.println("❌ FAILED to set up database triggers.");
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                System.err.println("Transaction has been rolled back.");
            }
            // In ra lỗi chi tiết để dễ dàng debug
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}