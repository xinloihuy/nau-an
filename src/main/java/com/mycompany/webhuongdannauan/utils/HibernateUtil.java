package com.mycompany.webhuongdannauan.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {

    private static EntityManagerFactory entityManagerFactory;
    private static final String PERSISTENCE_UNIT_NAME = "CookingAppPU"; // Phải khớp tên

    static {
        try {
            // Bước này kích hoạt Hibernate đọc persistence.xml và tạo/cập nhật schema
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("Hibernate EntityManagerFactory initialized successfully.");
        } catch (Exception ex) {
            // Ghi lỗi nếu kết nối hoặc khởi tạo thất bại
            System.err.println("Initial EntityManagerFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Phương thức để lấy Factory (dùng để tạo EntityManager)
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    // Phương thức tiện ích để lấy EntityManager (dùng cho các thao tác DAO)
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    // Phương thức để đóng Factory khi ứng dụng tắt
    public static void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            System.out.println("Hibernate EntityManagerFactory shut down.");
        }
    }
}