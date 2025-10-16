<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% 
    // Dữ liệu AdminMode được truyền qua Request Scope
    Long adminModeId = (Long) request.getAttribute("ADMIN_ID");
    Long currentSessionId = (Long) request.getAttribute("SESSION_ID");
    boolean isAdminMode = (adminModeId != null && currentSessionId != null);
    
    // Dữ liệu User cơ bản (từ Session)
    Long sessionUserId = (Long) session.getAttribute("userId");
    String sessionUsername = (String) session.getAttribute("username");
    
    // Dữ liệu User đang chat (Peer)
    String targetNickname = isAdminMode ? (String) request.getAttribute("USER_NICKNAME") : "Admin";

    // ĐỊNH NGHĨA BIẾN MÔI TRƯỜNG CHO JS
    Long jsUserId = isAdminMode ? adminModeId : sessionUserId;
    String jsUsername = isAdminMode ? "ADMIN" : (sessionUsername != null ? sessionUsername : "Bạn");
    String jsRole = isAdminMode ? "ADMIN" : "USER";
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= isAdminMode ? "Trả lời " : "Chat" %> Hỗ Trợ</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .chat-wrapper { max-width: 600px; margin: 20px auto; font-family: sans-serif; background: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        #messageDisplay { 
            height: 350px; 
            overflow-y: scroll; 
            border: 1px solid #ddd; 
            padding: 10px; 
            margin-bottom: 10px;
            background: #f9f9f9;
        }
        #messageDisplay p { margin: 5px 0; line-height: 1.4; word-wrap: break-word; }
        .user-msg { text-align: left; color: #007bff; }
        .admin-msg { text-align: right; color: #dc3545; }
        .admin-msg strong { color: #dc3545; }
        .user-msg strong { color: #007bff; }
    </style>
</head>
<body>
    <div class="chat-wrapper">
        <c:if test="${isAdminMode}">
            <h3 class="text-danger">Trả lời: ${targetNickname}</h3>
            <p><a href="${pageContext.request.contextPath}/admin/chat">← Quay lại danh sách phiên</a></p>
            <p style="font-size: small; color: #888;">Phiên ID: ${SESSION_ID}</p>
        </c:if>
        <c:if test="${!isAdminMode}">
            <h3 class="text-primary">Hỗ trợ Admin</h3>
        </c:if>
        
        <div id="messageDisplay">
            <c:if test="${not empty messages}">
                <c:forEach var="msg" items="${messages}">
                    <p class="${msg.senderRole == 'USER' ? 'user-msg' : 'admin-msg'}">
                        <strong>${msg.senderRole == 'USER' ? targetNickname : 'ADMIN'}:</strong> 
                        <span>${msg.content}</span>
                    </p>
                </c:forEach>
                <p style="text-align: center; font-size: small; color: #888;">--- Hết lịch sử chat ---</p>
            </c:if>
        </div>
        
        <div class="input-group">
            <input type="text" id="messageInput" class="form-control" placeholder="Nhập tin nhắn...">
            <button class="btn btn-primary" onclick="sendMessage()">Gửi</button>
        </div>
    </div>
    
    <script>
        // TRUYỀN DỮ LIỆU TỪ JAVA SANG JS
        const userId = "<%= jsUserId %>";
        const username = "<%= jsUsername %>";
        const role = "<%= jsRole %>";
        const isAdm = <%= isAdminMode %>;
        const sessionId = "<%= isAdminMode ? currentSessionId : "0" %>";
        const targetNickname = "<%= targetNickname %>"; // Tên user đang chat (cho Admin mode)

        const wsUrl = "ws://localhost:8080<%= request.getContextPath() %>/chat/" + userId;
        const socket = new WebSocket(wsUrl);
        
        const display = document.getElementById('messageDisplay');
        const input = document.getElementById('messageInput');
        
        // Hàm hiển thị tin nhắn - KHÔNG DÙNG TEMPLATE LITERALS
        function appendMessage(senderRole, content) {
            const isUser = senderRole === 'USER';
            
            // Xác định tên người gửi
            const senderName = isUser 
                ? (isAdm ? targetNickname : username) 
                : 'ADMIN';
            
            const messageDiv = document.createElement('p');
            messageDiv.className = isUser ? 'user-msg' : 'admin-msg';
            
            // TẠO DOM ELEMENTS THAY VÌ innerHTML với template literals
            const strongElem = document.createElement('strong');
            strongElem.textContent = senderName + ': ';
            
            const contentSpan = document.createElement('span');
            contentSpan.textContent = content;
            
            messageDiv.appendChild(strongElem);
            messageDiv.appendChild(contentSpan);
            display.appendChild(messageDiv);
            display.scrollTop = display.scrollHeight;
        }

        socket.onopen = function() {
            console.log("WebSocket connected");
        };

        socket.onmessage = function(event) {
            console.log("Received:", event.data);
            
            try {
                const data = JSON.parse(event.data);
                
                if (data.type === 'message') {
                    // Chỉ hiển thị nếu tin nhắn đến từ phiên chat hiện tại
                    if (!isAdm || data.sessionId == sessionId) {
                        appendMessage(data.role, data.content);
                    }
                } else if (data.type === 'history') {
                    // Logic xử lý lịch sử từ WebSocket (chỉ User cần)
                    if (!isAdm) { 
                        display.innerHTML = ''; 
                        if (Array.isArray(data.data)) {
                            data.data.forEach(function(msg) {
                                appendMessage(msg.senderRole, msg.content); 
                            });
                            
                            const historyEnd = document.createElement('p');
                            historyEnd.style.textAlign = 'center';
                            historyEnd.style.fontSize = 'small';
                            historyEnd.style.color = '#888';
                            historyEnd.textContent = '--- Hết lịch sử chat ---';
                            display.appendChild(historyEnd);
                        }
                    }
                }
            } catch (error) {
                console.error("Error parsing message:", error);
                console.error("Raw data:", event.data);
            }
        };

        socket.onerror = function(error) {
            console.error("WebSocket error:", error);
        };

        socket.onclose = function() {
            console.log("WebSocket disconnected");
        };

        function sendMessage() {
            const content = input.value.trim();
            if (content === "") return;
            
            const messageObject = {
                content: content,
                role: role,
                sessionId: isAdm ? sessionId : null 
            };
            
            console.log("Sending:", JSON.stringify(messageObject));
            socket.send(JSON.stringify(messageObject));
            input.value = '';
        }
        
        // Gửi bằng Enter
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
                e.preventDefault();
            }
        });
    </script>
</body>
</html>