package com.mycompany.webhuongdannauan.utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static final EntityManagerFactory factory;

    static {
        try {
            // SỬA LỖI: Chỉ định rõ đường dẫn đến thư mục chứa file .env
            Dotenv dotenv = Dotenv.configure()
                                  .directory("D:/") // Yêu cầu dotenv tìm trong thư mục gốc của ổ D
                                  .load();

            // Tạo một Map để chứa thông tin kết nối đọc từ .env
            Map<String, String> properties = new HashMap<>();
            properties.put("jakarta.persistence.jdbc.driver", dotenv.get("DB_DRIVER"));
            properties.put("jakarta.persistence.jdbc.url", dotenv.get("DB_URL"));
            properties.put("jakarta.persistence.jdbc.user", dotenv.get("DB_USER"));
            properties.put("jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD"));

            // Giữ lại các thuộc tính hibernate khác
            properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            properties.put("hibernate.hbm2ddl.auto", "update");
            properties.put("hibernate.show_sql", "true");
            properties.put("hibernate.format_sql", "true");

            // Khởi tạo EntityManagerFactory với các thuộc tính đã đọc từ .env
            // Sửa "CookingAppPU" thành đúng tên persistence-unit trong file persistence.xml của bạn
            factory = Persistence.createEntityManagerFactory("CookingAppPU", properties);

        } catch (Throwable ex) {
            System.err.println("!!! LỖI KHỞI TẠO JPAUtil - KIỂM TRA LẠI FILE .env VÀ ĐƯỜNG DẪN !!!");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return factory;
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}