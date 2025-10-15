<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Chat Hỗ trợ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <header>HỆ THỐNG QUẢN TRỊ - CHATBOX</header>
    <nav>
        <%-- include admin-sidebar --%>
    </nav>
    <section class="container mt-4">
        <h2>💬 Các Phiên Chat Đang Mở</h2>
        <p>Tổng số phiên đang chờ: <span class="badge bg-danger">${openSessions.size()}</span></p>

        <c:if test="${not empty openSessions}">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID Phiên</th>
                        <th>User</th>
                        <th>Trạng thái</th>
                        <th>Tin nhắn cuối</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="session" items="${openSessions}">
                        <tr>
                            <td>${session.id}</td>
                            <td>${session.user.username}</td>
                            <td><span class="badge bg-success">${session.status}</span></td>
                            <td><fmt:formatDate value="${session.lastMessageAt}" pattern="dd/MM HH:mm"/></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/chat?sessionId=${session.id}" class="btn btn-sm btn-primary">
                                    Trả lời
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty openSessions}">
            <div class="alert alert-info">Hiện không có phiên chat nào đang chờ hỗ trợ.</div>
        </c:if>
    </section>
</body>
</html>