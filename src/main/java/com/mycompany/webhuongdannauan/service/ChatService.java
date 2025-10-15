package com.mycompany.webhuongdannauan.service;

import com.mycompany.webhuongdannauan.dao.ChatMessageDAO;
import com.mycompany.webhuongdannauan.dao.ChatSessionDAO;
import com.mycompany.webhuongdannauan.dao.UserDAO;
import com.mycompany.webhuongdannauan.dao.impl.ChatMessageDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.ChatSessionDAOImpl;
import com.mycompany.webhuongdannauan.dao.impl.UserDAOImpl;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.User;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatService {

    // Khai báo DAO (Dependency Injection - Khởi tạo)
    private final UserDAO userDAO;
    public final ChatSessionDAO chatSessionDAO;
    private final ChatMessageDAO chatMessageDAO;

    public ChatService() {
        // Giả định các DAO Impl đã có và được khởi tạo
        this.userDAO = new UserDAOImpl();
        this.chatSessionDAO = new ChatSessionDAOImpl();
        this.chatMessageDAO = new ChatMessageDAOImpl();
    }
    
    // ---------------------------------------------------
    // --- PHƯƠNG THỨC CỐT LÕI (DÙNG TRONG WEBSOCKET) ---
    // ---------------------------------------------------

    /**
     * Tìm phiên chat đang mở của User. Nếu không có, tạo phiên mới.
     * Phương thức này được gọi khi User/Admin kết nối WebSocket.
     * @param userId ID của người dùng.
     * @return ChatSession đang mở.
     */
    public ChatSession findOrCreateSession(long userId) {
        User user = userDAO.findById(userId);
        if (user == null) {
            System.err.println("User not found with ID: " + userId);
            return null;
        }
        // Sử dụng logic phức tạp của ChatSessionDAO để tìm hoặc tạo phiên
        return chatSessionDAO.findOrCreateByUser(user);
    }

    /**
     * Lấy toàn bộ lịch sử tin nhắn của một phiên chat dựa trên User ID.
     * Dùng cho User khi mới kết nối để tải lịch sử chat gần nhất.
     * @param userId ID người dùng (dùng để tìm Session gần nhất).
     * @return Lịch sử tin nhắn.
     */
    public List<ChatMessage> getMessageHistory(long userId) {
        // Tìm session gần nhất của user
        ChatSession session = chatSessionDAO.findByUserId(userId); 
        if (session != null) {
            return chatMessageDAO.findBySessionId(session.getId());
        }
        return Collections.emptyList();
    }
    
    /**
     * Lấy toàn bộ lịch sử tin nhắn của một phiên chat dựa trên Session ID.
     * Dùng cho Admin khi mở một phiên chat cụ thể. (FIXED: Đã thêm phương thức này)
     * @param sessionId ID của phiên chat.
     * @return Lịch sử tin nhắn.
     */
    public List<ChatMessage> getMessagesBySessionId(long sessionId) {
        return chatMessageDAO.findBySessionId(sessionId);
    }
    
    // ---------------------------------------------------
    // --- PHƯƠNG THỨC GỬI TIN NHẮN (DÙNG TRONG WEBSOCKET) ---
    // ---------------------------------------------------

    /**
     * Gửi tin nhắn và lưu vào DB, cập nhật Session. Dùng chung cho cả User và Admin.
     * @param sessionId ID của phiên chat hiện tại.
     * @param content Nội dung tin nhắn.
     * @param senderRole Vai trò của người gửi ("USER" hoặc "ADMIN").
     * @return ChatMessage đã lưu.
     */
    public ChatMessage sendMessage(long sessionId, String content, String senderRole) {
        
        ChatSession session = chatSessionDAO.findById(sessionId);
        if (session == null) {
            System.err.println("Chat session not found with ID: " + sessionId);
            return null;
        }
        
        ChatMessage newMessage = new ChatMessage();
        newMessage.setSession(session);
        newMessage.setContent(content);
        newMessage.setSenderRole(senderRole);
        chatMessageDAO.save(newMessage);
        
        // Cập nhật: Đánh dấu thời gian hoạt động mới nhất cho session
        session.setLastMessageAt(new Date());
        // Sử dụng phương thức update của DAO (hoặc merge)
        chatSessionDAO.update(session); 
        
        return newMessage;
    }


    // ---------------------------------------------------
    // --- PHƯƠNG THỨC CHO ADMIN DASHBOARD ---
    // ---------------------------------------------------

    /**
     * Lấy tất cả các phiên chat đang mở (cho Admin Dashboard).
     */
    public List<ChatSession> getAllOpenSessions() {
        return chatSessionDAO.findAllOpenSessions();
    }

    /**
     * Đóng một phiên chat (chuyển status sang 'CLOSED').
     */
    public boolean closeSession(long sessionId) {
        ChatSession session = chatSessionDAO.findById(sessionId);
        if (session != null && "OPEN".equals(session.getStatus())) {
            session.setStatus("CLOSED");
            chatSessionDAO.update(session);
            return true;
        }
        return false;
    }
}