<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% 
    // Lấy ID và Username (nickname) từ Session
    Long userId = (Long) session.getAttribute("userId");
    String username = (String) session.getAttribute("username");
    
    // Gán giá trị mặc định nếu username là null (ví dụ: User mới hoặc lỗi session)
    String safeUsername = (username != null && !username.isBlank()) ? username : "Khách";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Chat Hỗ Trợ</title>
    <meta charset="UTF-8">
    <style>
        #chatWindow { max-width: 450px; margin: 20px auto; font-family: sans-serif; }
        #messageDisplay { 
            height: 300px; 
            overflow-y: scroll; 
            border: 1px solid #ccc; 
            padding: 10px; 
            margin-bottom: 10px;
            background: #f9f9f9;
        }
        #messageDisplay p { margin: 5px 0; line-height: 1.4; word-wrap: break-word; }
        #messageInput { width: calc(100% - 70px); padding: 8px; box-sizing: border-box; }
        .user-msg { color: #007bff; } /* Blue for User */
        .admin-msg { color: #dc3545; } /* Red for Admin */
    </style>
</head>
<body>
    <div id="chatWindow">
        <h3>Hỗ trợ Admin</h3>
        <div id="messageDisplay">
            </div>
        <input type="text" id="messageInput" placeholder="Nhập tin nhắn...">
        <button onclick="sendMessage()">Gửi</button>
    </div>
    
    <script>
        const userId = "<%= userId %>";
        const username = "<%= safeUsername %>"; // Dùng username đã được xử lý
        const role = "USER";
        
        // Kết nối WebSocket
        const socket = new WebSocket("ws://localhost:8080<%= request.getContextPath() %>/chat/" + userId);

        const display = document.getElementById('messageDisplay');
        const input = document.getElementById('messageInput');
        
        // Hàm tiện ích để hiển thị tin nhắn (Dùng cho cả lịch sử và tin nhắn mới)
        function appendMessage(senderRole, content) {
            
            // 1. Xác định Tên hiển thị và Class
            const isUser = senderRole === 'USER';
            const senderName = isUser ? username : 'ADMIN';
            const msgClass = isUser ? 'user-msg' : 'admin-msg';

            const messageDiv = document.createElement('p');
            messageDiv.className = msgClass;
            
            // 2. Tạo nội dung an toàn
            const senderStrong = document.createElement('strong');
            senderStrong.textContent = senderName + ': ';
            
            const contentSpan = document.createElement('span');
            contentSpan.textContent = content; 
            
            messageDiv.appendChild(senderStrong);
            messageDiv.appendChild(contentSpan);
            display.appendChild(messageDiv);
            
            display.scrollTop = display.scrollHeight;
        }

        socket.onopen = function() {
            console.log("Connected to chat server. User:", username);
        };

        socket.onmessage = function(event) {
            console.log("Received:", event.data);
            
            try {
                const data = JSON.parse(event.data);
                
                if (data.type === 'message') {
                    // Xử lý tin nhắn mới (Tin nhắn được broadcast)
                    appendMessage(data.role, data.content);
                    
                } else if (data.type === 'history') {
                    // Xử lý Lịch sử chat (Chỉ chạy khi kết nối)
                    if (Array.isArray(data.data)) {
                        display.innerHTML = ''; // Xóa placeholder/tin nhắn cũ
                        data.data.forEach(function(msg) {
                            // Dùng msg.senderRole vì đây là dữ liệu từ Entity DB
                            appendMessage(msg.senderRole, msg.content); 
                        });
                        display.innerHTML += '<p style="text-align: center; font-size: small; color: #888;">--- Hết lịch sử chat ---</p>';
                    }
                }
            } catch (error) {
                console.error("Error processing message:", error);
            }
        };
        
        socket.onerror = function(error) {
            console.error("WebSocket error:", error);
        };

        function sendMessage() {
            const content = input.value.trim();
            
            if (content === "") return;
            
            const messageObject = {
                content: content,
                role: role // 'USER'
            };
            
            socket.send(JSON.stringify(messageObject));
            input.value = '';
        }
        
        // Cho phép gửi tin nhắn bằng phím Enter
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });
    </script>
</body>
</html>