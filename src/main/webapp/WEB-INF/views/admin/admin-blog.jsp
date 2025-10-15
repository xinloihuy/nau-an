<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Blog</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #fffdf8;
            margin: 0;
        }

        h2 {
            background-color: #f57c00;
            color: white;
            padding: 18px 30px;
            margin: 0;
            font-size: 22px;
            font-weight: 600;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
        }

        table {
            width: 95%;
            margin: 30px auto;
            border-collapse: collapse;
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }

        th, td {
            border-bottom: 1px solid #eee;
            text-align: left;
            padding: 14px 18px;
        }

        th {
            background: #fff2e0;
            color: #4a2c00;
            font-weight: 700;
        }

        tr:hover {
            background: #fff7eb;
        }

        .btn {
            padding: 8px 12px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            color: white;
            text-decoration: none;
            font-size: 14px;
        }

        .btn-edit {
            background: #4CAF50;
        }

        .btn-delete {
            background: #e74c3c;
        }

        .btn:hover {
            opacity: 0.9;
        }

        .back-link {
            margin: 20px 30px;
            display: inline-block;
            text-decoration: none;
            color: #f57c00;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h2>📜 Quản lý các bài Blog</h2>

    <a href="${pageContext.request.contextPath}/admin/dashboard" class="back-link">← Quay lại trang Admin</a>

    <table>
        <thead>
            <tr>
                <th>STT</th>
                <th>Tiêu đề</th>
                <th>Tác giả</th>
                <th>Ngày tạo</th>
                <th>Nội dung</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="b" items="${blogs}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${b.title}</td>
                <td>${b.author.username}</td>
                <td>${b.createdAt}</td>
                <td style="max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                    ${b.content}
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/blog/edit?id=${b.id}" class="btn btn-edit">Sửa</a>
                    <a href="${pageContext.request.contextPath}/admin/blog/delete?id=${b.id}" class="btn btn-delete"
                       onclick="return confirm('Bạn có chắc chắn muốn xóa bài viết này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <!-- ✅ Nút Thêm Blog nằm sau bảng -->
    <div style="text-align: center; margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/admin/blog/add"
           style="
            display: inline-block;
            background-color: #f57c00;
            color: white;
            font-weight: bold;
            padding: 12px 28px;
            border-radius: 8px;
            text-decoration: none;
            box-shadow: 0 3px 6px rgba(0,0,0,0.2);
            transition: 0.3s;
           "
           onmouseover="this.style.backgroundColor='#ff9800'"
           onmouseout="this.style.backgroundColor='#f57c00'">
            ➕ Thêm Blog  
        </a>
    </div>
</body>
</html>
