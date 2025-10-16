<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>👥 Quản lý người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin-user.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
</head>
<body>
    <h2 class="page-title">👥 Quản lý người dùng</h2>

    <!-- Bảng hiển thị -->
    <table class="user-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên đăng nhập</th>
                <th>Email</th>
                <th>Vai trò</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td class="roles">
                        <c:forEach var="role" items="${user.roles}">
                            ${role.name}
                        </c:forEach>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/AdminUserServlet" method="post" style="display:inline;" onsubmit="return confirm('Xóa người dùng này?')">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${user.id}">
                            <button type="submit" class="delete-btn">🗑️ Xóa</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Form thêm người dùng -->
    <form class="add-user-form" action="${pageContext.request.contextPath}/AdminUserServlet" method="post">
        <input type="hidden" name="action" value="add">
        <input type="text" name="username" placeholder="Tên đăng nhập" required>
        <input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Mật khẩu" required>
        <select name="role">
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
        </select>
        <button type="submit">➕ Thêm người dùng</button>
    </form>

    <!-- Nút quay lại -->
    <div class="back-link-container">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="back-link">⬅ Quay lại Trang Admin</a>
    </div>
</body>
</html>