<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% 
    // Giả định Admin đăng nhập với ID 1
    Long adminId = 1L; // Hoặc lấy từ session.getAttribute("userId")
%>
<!DOCTYPE html>
<html>
<head>
    <title>Trả lời: ${currentSession.user.username}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .chat-container { max-width: 600px; margin: 20px auto; }
        #messageDisplay { height: 400px; overflow-y: scroll; padding: 15px; background: #fff; border: 1px solid #ddd; }
        .user-msg { text-align: left; color: #0d6efd; } /* User - Blue */
        .admin-msg { text-align: right; color: #dc3545; } /* Admin - Red */
        .msg-bubble { 
            display: inline-block; 
            padding: 8px 12px; 
            border-radius: 15px; 
            margin-bottom: 5px; 
            max-width: 80%;
            background: #e9ecef;
        }
        .admin-msg .msg-bubble { background: #ffe0e0; float: right; }
        .user-msg .msg-bubble { background: #e0f0ff; float: left; }
    </style>
</head>
<body>
    <div class="chat-container">
        <h3 class="mt-4">Chat với: ${currentSession.user.username}</h3>
        <p><a href="${pageContext.request.contextPath}/admin/chat">← Quay lại danh sách</a></p>

        <div id="messageDisplay">
            <c:forEach var="msg" items="${messages}">
                <div class="msg-container ${msg.senderRole == 'USER' ? 'user-msg' : 'admin-msg'}">
                    <span class="msg-bubble">${msg.content}</span>
                </div>
            </c:forEach>
        </div>

        <div class="input-group mt-3">
            <input type="text" id="messageInput" class="form-control" placeholder="Nhập tin nhắn...">
            <button class="btn btn-primary" onclick="sendMessage()">Gửi</button>
        </div>
        
        <button class="btn btn-sm btn-danger mt-3" onclick="closeSession(${currentSession.id})">Đóng Phiên Chat</button>

    </div>

    <script>
        const userId = "<%= adminId %>"; // ID của Admin
        const targetUserId = "${currentSession.user.id}"; // ID của User đang chat
        const role = "ADMIN";
        
        // Kết nối WebSocket (Admin chỉ cần kết nối 1 lần)
        const socket = new WebSocket(`ws://localhost:8080<%= request.getContextPath() %>/chat/${userId}`);

        socket.onopen = function() { console.log("Admin Connected."); };
        
        socket.onmessage = function(event) {
            const data = JSON.parse(event.data);
            if (data.type === 'message') {
                // Thêm tin nhắn vào khung chỉ khi tin nhắn dành cho phiên này
                if (data.userId == targetUserId || data.role === 'ADMIN') { 
                    appendMessage(data.role, data.content);
                }
            }
        };

        function appendMessage(senderRole, content) {
            const display = document.getElementById('messageDisplay');
            const isUser = senderRole === 'USER';
            
            const container = document.createElement('div');
            container.className = `msg-container ${isUser ? 'user-msg' : 'admin-msg'}`;
            
            const bubble = document.createElement('span');
            bubble.className = 'msg-bubble';
            bubble.textContent = content;
            
            container.appendChild(bubble);
            display.appendChild(container);
            display.scrollTop = display.scrollHeight;
        }

        function sendMessage() {
            const input = document.getElementById('messageInput');
            const content = input.value.trim();
            if (content === "") return;

            const messageObject = {
                content: content,
                role: role // 'ADMIN'
            };

            socket.send(JSON.stringify(messageObject));
            input.value = '';
        }
        
        function closeSession(sessionId) {
            if (confirm("Bạn có chắc chắn muốn đóng phiên chat này không?")) {
                // Cần một Servlet POST riêng để đóng phiên (ví dụ: /admin/chat/close)
                fetch(`${window.location.origin}${window.location.pathname}/close`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `sessionId=${sessionId}`
                })
                .then(response => {
                    if (response.ok) {
                        alert('Đã đóng phiên chat thành công.');
                        window.location.href = '${pageContext.request.contextPath}/admin/chat';
                    } else {
                        alert('Lỗi: Không thể đóng phiên.');
                    }
                });
            }
        }
    </script>
</body>
</html>