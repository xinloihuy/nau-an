package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.service.ChatService;
import com.mycompany.webhuongdannauan.service.UserService;
import com.mycompany.webhuongdannauan.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/chat/*")
public class AdminChatServlet extends HttpServlet {

    private final ChatService chatService = new ChatService();
    private final UserService userService = new UserService();
    
    private static final String SESSION_LIST_VIEW = "/WEB-INF/views/admin/chat/chat-admin-list.jsp";
    private static final String CHAT_USER_VIEW = "/WEB-INF/views/chat/chat-user.jsp"; // TÁI SỬ DỤNG
    private static final String ADMIN_LOGIN_URL = "/admin/login";

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("isAdmin") != null && (boolean)session.getAttribute("isAdmin");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL + "?error=access_denied");
            return;
        }

        String sessionIdParam = request.getParameter("sessionId");
        Long adminUserId = (Long) request.getSession().getAttribute("userId");

        try {
            if (sessionIdParam != null && !sessionIdParam.isBlank()) {
                // TRƯỜNG HỢP 1: Mở giao diện chat cụ thể (CHẾ ĐỘ ADMIN)
                Long sessionId = Long.parseLong(sessionIdParam);
                ChatSession currentSession = chatService.chatSessionDAO.findById(sessionId);
                
                if (currentSession == null) throw new IllegalArgumentException("Invalid Chat Session ID.");
                
                // LẤY DỮ LIỆU CẦN THIẾT CHO CHATBOX ADMIN
                request.setAttribute("ADMIN_ID", adminUserId); // ID của Admin đang gửi
                request.setAttribute("USER_ID_PEER", currentSession.getUser().getId()); // ID của User đang chat
                request.setAttribute("SESSION_ID", currentSession.getId()); 
                request.setAttribute("USER_NICKNAME", currentSession.getUser().getNickname());
                
                // Lấy lịch sử tin nhắn
                List<ChatMessage> messages = chatService.getMessagesBySessionId(sessionId); 
                System.out.println("DEBUG: Loaded messages count for session " + sessionId + ": " + messages.size()); // <--- KIỂM TRA TẠI ĐÂY
                request.setAttribute("messages", messages);
                
                // Forward tới chat-user.jsp
                request.getRequestDispatcher(CHAT_USER_VIEW).forward(request, response);
                return;
            }
            
            // TRƯỜNG HỢP 2: Hiển thị danh sách các phiên đang mở
            List<ChatSession> openSessions = chatService.getAllOpenSessions();
            
            request.setAttribute("openSessions", openSessions);
            request.getRequestDispatcher(SESSION_LIST_VIEW).forward(request, response);

        } catch (Exception e) {
            System.err.println("Lỗi xử lý Admin Chat GET: " + e.getMessage());
            request.setAttribute("error", "Lỗi tải dữ liệu chat: " + e.getMessage());
            request.getRequestDispatcher(SESSION_LIST_VIEW).forward(request, response);
        }
    }
    
    // Logic đóng phiên chat (đã có trong câu trả lời trước)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ... (Logic đóng phiên chat)
        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL + "?error=access_denied");
            return;
        }

        String pathInfo = request.getPathInfo();
        if ("/close".equalsIgnoreCase(pathInfo)) {
            String sessionIdParam = request.getParameter("sessionId");
            if (sessionIdParam != null) {
                try {
                    Long sessionId = Long.parseLong(sessionIdParam);
                    if (chatService.closeSession(sessionId)) {
                        response.setStatus(HttpServletResponse.SC_OK); 
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": true}");
                        return;
                    }
                } catch (NumberFormatException e) {
                     response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Session ID.");
                     return;
                }
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not supported.");
    }
}