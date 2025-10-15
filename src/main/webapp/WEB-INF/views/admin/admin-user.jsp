<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>👥 Quản lý người dùng</h2>

<!-- Bảng hiển thị -->
<table>
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
                <td>
                    <c:forEach var="role" items="${user.roles}">
                        ${role.name}
                    </c:forEach>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/AdminUserServlet" method="post" onsubmit="return confirm('Xóa người dùng này?')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${user.id}">
                        <button type="submit" style="background:red; color:white; border:none; padding:6px 12px; border-radius:4px; cursor:pointer;">🗑️ Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<!-- Form thêm người dùng (dưới bảng) -->
<form class="add-form" action="${pageContext.request.contextPath}/AdminUserServlet" method="post">
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

<!-- Nút quay lại Trang Admin -->
<div class="back">
    <a href="${pageContext.request.contextPath}/admin/dashboard">⬅ Quay lại Trang Admin</a>
</div>

</body>
</html>