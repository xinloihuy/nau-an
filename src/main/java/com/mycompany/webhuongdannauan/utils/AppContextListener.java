/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.webhuongdannauan.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Khởi tạo HibernateUtil (và Entity Manager Factory) khi ứng dụng được deploy
        HibernateUtil.getEntityManagerFactory(); 
        System.out.println("Application context initialized. Hibernate setup completed.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Đóng EntityManagerFactory khi ứng dụng tắt
        HibernateUtil.shutdown();
        System.out.println("Application context destroyed. Hibernate shut down.");
    }
}