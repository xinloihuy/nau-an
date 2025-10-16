package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.model.User;
import java.util.List;

public interface ChatDAO extends GenericDAO<ChatSession, Long> {

    /**
     * Tìm kiếm phiên chat đang MỞ (Status = 'OPEN') của một User cụ thể.
     * @param userId ID của người dùng.
     * @return ChatSession nếu tìm thấy, ngược lại null.
     */
    ChatSession findOpenSessionByUserId(Long userId);
    
    /**
     * Lấy lịch sử tin nhắn của một phiên chat.
     * @param sessionId ID của phiên chat.
     * @return Danh sách các tin nhắn (ChatMessage) theo thứ tự thời gian.
     */
    List<ChatMessage> getMessagesBySessionId(Long sessionId);
    
    /**
     * Lưu một tin nhắn mới (sử dụng DAO cho ChatMessage).
     * @param message Tin nhắn cần lưu.
     */
    void saveMessage(ChatMessage message);
    
    /**
     * [Tùy chọn cho Admin Dashboard] Lấy tất cả các phiên chat đang mở.
     */
    List<ChatSession> findAllOpenSessions(); 
    ChatSession findOrCreateByUser(User user);
    
}