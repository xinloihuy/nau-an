package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.User;
import java.util.List;

public interface ChatSessionDAO extends GenericDAO<ChatSession, Long> {
    
    /**
     * Tìm phiên chat đang mở (status='OPEN') của một User, hoặc tạo mới nếu không tồn tại.
     * @param user Đối tượng User.
     * @return ChatSession đang mở.
     */
    ChatSession findOrCreateByUser(User user);
    
    /**
     * Tìm ChatSession theo User ID (thường dùng cho lịch sử).
     * @param userId ID của người dùng.
     * @return ChatSession.
     */
    ChatSession findByUserId(long userId); 

    /**
     * Lấy tất cả các phiên chat đang mở (cho Admin Dashboard).
     */
    List<ChatSession> findAllOpenSessions();
    
    /**
     * Cập nhật một ChatSession (thường dùng để thay đổi status hoặc lastMessageAt).
     */
    void update(ChatSession session);
}