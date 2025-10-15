package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.Notification;
import java.util.List;

public interface NotificationDAO {

    void markAllAsRead(long userId);
    /**
     * Lưu một thông báo mới vào cơ sở dữ liệu.
     * @param notification Đối tượng Notification cần lưu.
     */
    void save(Notification notification);

    /**
     * Tìm tất cả các thông báo của một người dùng, sắp xếp theo thời gian mới nhất.
     * @param userId ID của người dùng nhận thông báo.
     * @return Một danh sách các Notification.
     */
    List<Notification> findByUserId(long userId);

    /**
     * Đánh dấu một thông báo cụ thể là đã đọc.
     * @param notificationId ID của thông báo cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    boolean markAsRead(long notificationId);
}