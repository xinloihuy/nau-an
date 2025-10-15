package com.mycompany.webhuongdannauan.controller;

import com.mycompany.webhuongdannauan.model.ChatSession;
import com.mycompany.webhuongdannauan.model.ChatMessage;
import com.mycompany.webhuongdannauan.service.ChatService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/chat/*") // Ánh xạ đến /admin/chat và các sub-path (ví dụ: /admin/chat/close)
public class AdminChatServlet extends HttpServlet {

    private final ChatService chatService = new ChatService();
    
    private static final String SESSION_LIST_VIEW = "/WEB-INF/views/admin/chat/chat-admin-list.jsp";
    private static final String CHAT_SESSION_VIEW = "/WEB-INF/views/admin/chat/chat-admin-session.jsp";
    private static final String ADMIN_LOGIN_URL = "/admin/login";

    // --- Phương thức kiểm tra quyền Admin ---
    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("isAdmin") != null && (boolean)session.getAttribute("isAdmin");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // KIỂM TRA QUYỀN ADMIN
        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL + "?error=access_denied");
            return;
        }

        String pathInfo = request.getPathInfo();
        String sessionIdParam = request.getParameter("sessionId");

        try {
            if (sessionIdParam != null && !sessionIdParam.isBlank()) {
                // TRƯỜNG HỢP 1: Mở giao diện chat cụ thể
                Long sessionId = Long.parseLong(sessionIdParam);
                ChatSession session = chatService.chatSessionDAO.findById(sessionId);
                
                if (session == null) throw new IllegalArgumentException("Invalid Chat Session ID.");
                
                // Lấy lịch sử tin nhắn
                List<ChatMessage> messages = chatService.getMessagesBySessionId(sessionId); 
                
                request.setAttribute("currentSession", session);
                request.setAttribute("messages", messages);
                
                request.getRequestDispatcher(CHAT_SESSION_VIEW).forward(request, response);
                return;
            }
            
            // TRƯỜNG HỢP 2 (Mặc định: /admin/chat): Hiển thị danh sách các phiên đang mở
            List<ChatSession> openSessions = chatService.getAllOpenSessions();
            
            request.setAttribute("openSessions", openSessions);
            request.getRequestDispatcher(SESSION_LIST_VIEW).forward(request, response);

        } catch (Exception e) {
            System.err.println("Lỗi xử lý Admin Chat GET: " + e.getMessage());
            request.setAttribute("error", "Lỗi tải dữ liệu chat.");
            request.getRequestDispatcher(SESSION_LIST_VIEW).forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra quyền Admin (cần thiết cho mọi POST Admin)
        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + ADMIN_LOGIN_URL + "?error=access_denied");
            return;
        }

        String pathInfo = request.getPathInfo();
        
        if ("/close".equalsIgnoreCase(pathInfo)) {
            // Xử lý đóng phiên chat (URL: /admin/chat/close)
            String sessionIdParam = request.getParameter("sessionId");
            if (sessionIdParam != null) {
                try {
                    Long sessionId = Long.parseLong(sessionIdParam);
                    chatService.closeSession(sessionId);
                    
                    // Trả về JSON/text thành công (cho Fetch API)
                    response.setStatus(HttpServletResponse.SC_OK); 
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": true}");
                    return;
                } catch (NumberFormatException e) {
                     response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Session ID.");
                     return;
                }
            }
        }
        
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not supported.");
    }
}