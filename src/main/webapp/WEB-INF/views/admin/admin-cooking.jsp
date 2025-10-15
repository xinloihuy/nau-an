<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
    <title>Quản lý Cooking Tips</title>
    <style>
        body { font-family:'Segoe UI'; background:#fffdf8; margin:0; }
        h2 { background:#f57c00; color:white; padding:15px 25px; margin:0; }
        table { width:90%; margin:20px auto; border-collapse:collapse; background:white; }
        th,td { border:1px solid #ddd; padding:10px; }
        th { background:#f57c00; color:white; }
        tr:hover { background:#fff8e1; }
        .btn { padding:6px 10px; border:none; border-radius:5px; cursor:pointer; }
        .add { background:#f57c00; color:white; }
        .edit { background:#4CAF50; color:white; }
        .delete { background:#d9534f; color:white; }
        input[type=text], textarea { width:95%; padding:6px; margin:4px 0; }
        textarea { height:60px; }
        .back { text-align:center; margin:20px; }
    </style>
</head>
<body>

<h2>💡 Quản lý Cooking Tips</h2>

<!-- Form thêm mới -->
<form action="${pageContext.request.contextPath}/admin/cooking-tips" method="post" style="text-align:center;">
    <input type="hidden" name="action" value="add">
    <input type="text" name="title" placeholder="Tiêu đề mẹo nấu ăn" required><br>
    <textarea name="content" placeholder="Nội dung mẹo" required></textarea><br>
    <button type="submit" class="btn add">➕ Thêm mẹo</button>
</form>

<!-- Danh sách -->
<table>
    <thead>
        <tr><th>ID</th><th>Tiêu đề</th><th>Nội dung</th><th>Hành động</th></tr>
    </thead>
    <tbody>
        <c:forEach var="tip" items="${tips}">
            <tr>
                <td>${tip.id}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/cooking-tips" method="post">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="id" value="${tip.id}">
                        <input type="text" name="title" value="${tip.title}">
                        <textarea name="content">${tip.content}</textarea>
                        <button type="submit" class="btn edit">💾 Lưu</button>
                    </form>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/cooking-tips" method="post">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${tip.id}">
                        <button type="submit" class="btn delete">🗑 Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="back">
    <a href="${pageContext.request.contextPath}/admin/dashboard" style="color:#f57c00;font-weight:bold;">⬅ Quay lại Trang Admin</a>
</div>

</body>
</html>
