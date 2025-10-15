package com.mycompany.webhuongdannauan.utils;

import com.mycompany.webhuongdannauan.config.EnvConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "CookingAppPU"; // Phải khớp với persistence.xml

    static {
        try {
            // 🔹 Map chứa cấu hình override từ Env hoặc .env
            Map<String, String> properties = new HashMap<>();

            properties.put("jakarta.persistence.jdbc.driver",
                    EnvConfig.get("DB_DRIVER", "com.mysql.cj.jdbc.Driver"));
            properties.put("jakarta.persistence.jdbc.url",
                    EnvConfig.get("DB_URL", "jdbc:mysql://localhost:3306/cooking_db"));
            properties.put("jakarta.persistence.jdbc.user",
                    EnvConfig.get("DB_USER", "root"));
            properties.put("jakarta.persistence.jdbc.password",
                    EnvConfig.get("DB_PASSWORD", "1234"));

            // ⚙️ Tạo EntityManagerFactory với cấu hình được nạp
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);

            System.out.println("✅ Hibernate EntityManagerFactory initialized successfully.");
            System.out.println("   → DB URL: " + properties.get("jakarta.persistence.jdbc.url"));

        } catch (Exception ex) {
            System.err.println("❌ Initial EntityManagerFactory creation failed: " + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    // 🧩 Lấy EntityManagerFactory
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    // 🧩 Lấy EntityManager (dùng cho DAO)
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // 🧹 Đóng Factory khi ứng dụng tắt
    public static void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            System.out.println("Hibernate EntityManagerFactory shut down.");
        }
    }
}
