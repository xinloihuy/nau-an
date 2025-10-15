package com.mycompany.webhuongdannauan.dao;

import com.mycompany.webhuongdannauan.model.ChatMessage;
import java.util.List;

public interface ChatMessageDAO extends GenericDAO<ChatMessage, Long> {

    /**
     * Lấy tất cả tin nhắn thuộc về một Session cụ thể, sắp xếp theo thời gian.
     * @param sessionId ID của phiên chat.
     * @return Danh sách ChatMessage.
     */
    List<ChatMessage> findBySessionId(long sessionId);
    
    // Lưu ý: Các phương thức save/findById đã được kế thừa từ GenericDAO.
}