<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông báo - Hướng dẫn nấu ăn</title>
    <link rel="stylesheet" href="styles/notifications.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <section class="main-content">
        <%-- KHỐI CODE ĐÃ BỊ XÓA --%>
        <%-- 
        <header class="header">
            <div class="search-bar">
                <input type="text" placeholder="Tìm tên món hay nguyên liệu...">
                <button>Tìm kiếm</button>
            </div>
        </header> 
        --%>

        <div class="notification-container">
            <div class="notification-header">
                <h2>Thông báo</h2>
                <a href="#" class="mark-read">Đánh dấu đã đọc</a>
            </div>

            <div class="notification-content">
                <c:set var="yesterday" value="<%= new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000) %>"/>

                <div class="notification-group">
                    <h3>Hôm nay</h3>
                    <c:forEach var="noti" items="${notifications}">
                        <c:if test="${noti.createdAt.time >= yesterday.time}">
                            <div class="notification-item ${!noti.isRead ? 'unread' : ''}">
                                <div class="notification-avatar"><i class="fas fa-comment"></i></div>
                                <div class="notification-body">
                                    <p class="notification-text"><c:out value="${noti.content}"/></p>
                                    <p class="notification-time"><fmt:formatDate value="${noti.createdAt}" pattern="HH:mm" /></p>
                                </div>
                                <c:if test="${!noti.isRead}">
                                    <div class="notification-dot"></div>
                                </c:if>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>

                <div class="notification-group">
                    <h3>Trước đó</h3>
                    <c:forEach var="noti" items="${notifications}">
                        <c:if test="${noti.createdAt.time < yesterday.time}">
                            <div class="notification-item ${!noti.isRead ? 'unread' : ''}">
                                <div class="notification-avatar"><i class="fas fa-comment"></i></div>
                                <div class="notification-body">
                                    <p class="notification-text"><c:out value="${noti.content}"/></p>
                                    <p class="notification-time"><fmt:formatDate value="${noti.createdAt}" pattern="dd/MM/yyyy" /></p>
                                </div>
                                <c:if test="${!noti.isRead}">
                                    <div class="notification-dot"></div>
                                </c:if>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
    </section>
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        // 1. Tìm nút "Đánh dấu đã đọc"
        const markReadButton = document.querySelector('.mark-read');

        if (markReadButton) {
            // 2. Gắn sự kiện 'click' vào nút
            markReadButton.addEventListener('click', function(event) {
                // Ngăn chặn hành vi mặc định của thẻ <a> (không cho nhảy trang)
                event.preventDefault();

                // 3. Gửi yêu cầu POST tới chính URL hiện tại (/notifications)
                fetch('${pageContext.request.contextPath}/notifications', {
                    method: 'POST',
                    headers: {
                        // Báo cho server biết rằng đây là một yêu cầu AJAX
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                })
                .then(response => {
                    // Nếu phản hồi từ server không ổn (ví dụ: lỗi 500, 401)
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json(); // Chuyển đổi phản hồi thành đối tượng JSON
                })
                .then(data => {
                    // 4. Nếu server trả về { "status": "success" }
                    if (data.status === 'success') {
                        // Lấy tất cả các dấu chấm xanh
                        const notificationDots = document.querySelectorAll('.notification-dot');
                        notificationDots.forEach(dot => {
                            dot.style.display = 'none'; // Ẩn dấu chấm đi
                        });

                        // Bỏ highlight màu nền của các thông báo chưa đọc
                        const unreadItems = document.querySelectorAll('.notification-item.unread');
                        unreadItems.forEach(item => {
                            item.classList.remove('unread');
                        });

                        // Có thể ẩn luôn nút "Đánh dấu đã đọc" đi
                        markReadButton.style.display = 'none';
                    } else {
                        alert('Có lỗi xảy ra: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                    alert('Không thể thực hiện hành động. Vui lòng thử lại.');
                });
            });
        }
    });
</script>
</body>
</html>
