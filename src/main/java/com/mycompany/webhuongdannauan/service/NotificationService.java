package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.NotificationDAO;
import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.dao.impl.NotificationDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.model.Notification;
import com.mycompany.webhuongdannauan.model.User;
import java.util.List;

public class NotificationService {

    // Khởi tạo các DAO cần thiết
    private final UserDAO userDAO = new UserDAOImpl();
    private final NotificationDAO notificationDAO = new NotificationDAOImpl();
    
    // Constructor rỗng
    public NotificationService() {}
    public void markAllAsRead(long userId) {
        notificationDAO.markAllAsRead(userId);
    }
    /**
     * Tạo một thông báo mới cho người dùng.
     * @param userId ID của người dùng sẽ nhận thông báo.
     * @param type Loại thông báo (ví dụ: 'FOLLOW', 'ADMIN_MESSAGE').
     * @param content Nội dung của thông báo.
     */
    public void createNotification(long userId, String type, String content) {
        // Bước 1: Tìm người dùng sẽ nhận thông báo
        User user = userDAO.findById(userId);
        if (user == null) {
            System.err.println("Cannot create notification for non-existent user with ID: " + userId);
            return;
        }

        // Bước 2: Tạo đối tượng Notification mới
        Notification newNotification = new Notification();
        newNotification.setUser(user);
        newNotification.setType(type);
        newNotification.setContent(content);
        newNotification.setIsRead(false);

        // Bước 3: Lưu thông báo vào DB thông qua DAO
        notificationDAO.save(newNotification);
    }

    /**
     * Lấy tất cả thông báo của một người dùng.
     * @param userId ID của người dùng.
     * @return Danh sách các thông báo.
     */
    public List<Notification> getNotificationsByUserId(long userId) {
        return notificationDAO.findByUserId(userId);
    }

    /**
     * Đánh dấu một thông báo là đã đọc.
     * @param notificationId ID của thông báo.
     */
    public void markAsRead(long notificationId) {
        notificationDAO.markAsRead(notificationId);
    }
}