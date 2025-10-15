package com.mycompany.webhuongdannauan.websocket;

import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.service.ChatService;
import com.mycompany.webhuongdannauan.service.UserService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

@ServerEndpoint("/chat/{userId}") 
public class ChatEndpoint {

    // Map để lưu trữ các session đang hoạt động: Key = User ID (String), Value = WebSocket Session
    private static final Map<String, Session> activeSessions = new ConcurrentHashMap<>();
    private final ChatService chatService = new ChatService();
    private final UserService userService = new UserService(); // Cần để xác định vai trò (tùy chọn)

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        activeSessions.put(userId, session);
        System.out.println("User " + userId + " connected.");
        
        try {
            Long uId = Long.parseLong(userId);
            
            // 1. Tìm/tạo Session Chat trong DB
            ChatSession dbSession = chatService.findOrCreateSession(uId);
            
            // 2. Gửi lịch sử chat về client (đảm bảo JS nhận mảng JSON)
            List<ChatMessage> history = chatService.getMessageHistory(uId);
            
            JSONObject historyResponse = new JSONObject();
            historyResponse.put("type", "history");
            historyResponse.put("data", history); // Thư viện JSON sẽ chuyển List thành mảng JSON
            historyResponse.put("sessionId", dbSession.getId());
            
            session.getBasicRemote().sendText(historyResponse.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            try { session.close(); } catch (IOException ignored) {}
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") String userId) throws IOException {
        
        JSONObject json = new JSONObject(message);
        String content = json.getString("content");
        String senderRole = json.getString("role"); // "USER" hoặc "ADMIN"
        
        Long uId = Long.parseLong(userId);
        
        // LẤY SESSION ID (Cần thiết cho Admin gửi tin)
        long sessionId;
        try {
            // Nếu là Admin, tin nhắn có thể chứa sessionId. Nếu là User, tìm session theo userId
            sessionId = json.optLong("sessionId", chatService.findOrCreateSession(uId).getId()); 
        } catch (Exception e) {
             System.err.println("Could not determine session ID.");
             return;
        }
        
        // 1. Lưu tin nhắn vào DB và cập nhật Session
        ChatMessage savedMessage = chatService.sendMessage(sessionId, content, senderRole);
        
        if (savedMessage == null) return;
        
        // 2. TẠO JSON RESPONSE ĐÚNG CÁCH (FIXED: Sử dụng savedMessage.getSenderRole())
        JSONObject messageResponse = new JSONObject();
        messageResponse.put("type", "message");
        messageResponse.put("role", savedMessage.getSenderRole()); // Role đã được DB xác nhận
        messageResponse.put("content", content);
        messageResponse.put("sessionId", sessionId);
        
        final String formattedMessage = messageResponse.toString();
        
        // 3. Phân phối tin nhắn (Broadcast/Direct)
        final String finalUserId = userId;
        
        // Gửi lại cho tất cả các bên tham gia session này
        activeSessions.forEach((key, remoteSession) -> {
            // Logic đơn giản: Nếu không phải người gửi VÀ đang online, gửi
            // (Trong môi trường thực, cần kiểm tra xem người nhận có phải Admin hay User trong phiên này không)
            if (!key.equals(finalUserId)) { 
                try {
                    remoteSession.getBasicRemote().sendText(formattedMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Gửi lại cho người gửi
        session.getBasicRemote().sendText(formattedMessage);
    }
    
    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        activeSessions.remove(userId);
        System.out.println("User " + userId + " disconnected.");
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Chat error: " + throwable.getMessage());
    }
}