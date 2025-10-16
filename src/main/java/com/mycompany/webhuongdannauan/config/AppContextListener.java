package com.mycompany.webhuongdannauan.config;

import com.mycompany.webhuongdannauan.dao.impl.TriggerSetupDAOImpl;
import com.mycompany.webhuongdannauan.utils.JPAUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Lớp này lắng nghe các sự kiện vòng đời của ứng dụng web.
 * Nó sẽ được tự động gọi bởi server khi ứng dụng bắt đầu hoặc kết thúc.
 */
@WebListener // Annotation này tự động đăng ký listener mà không cần cấu hình trong web.xml
public class AppContextListener implements ServletContextListener {

    /**
     * Phương thức này được gọi DUY NHẤT MỘT LẦN khi ứng dụng khởi chạy.
     * Đây là nơi hoàn hảo để thiết lập các kết nối, cài đặt ban đầu,
     * và trong trường hợp này là cài đặt các trigger cho database.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========================================================");
        System.out.println("🚀 APPLICATION STARTING UP: Initializing context...");
        
        // Gọi DAO để thiết lập các trigger
        TriggerSetupDAOImpl triggerDAO = new TriggerSetupDAOImpl();
        triggerDAO.setupTriggers();
        
        System.out.println("========================================================");
    }

    /**
     * Phương thức này được gọi khi ứng dụng dừng lại (shutdown server).
     * Dùng để giải phóng tài nguyên, ví dụ như đóng EntityManagerFactory.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("========================================================");
        System.out.println("APPLICATION SHUTTING DOWN: Closing resources...");
        JPAUtil.shutdown(); // Gọi phương thức shutdown của bạn để đóng factory
        System.out.println("========================================================");
    }
}