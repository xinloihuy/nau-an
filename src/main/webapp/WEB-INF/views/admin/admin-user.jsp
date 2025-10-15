<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách người dùng</title>
    <style>
        /* ===== Tổng thể ===== */
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #fffdf8;
            margin: 0;
        }

        /* ===== Thanh tiêu đề ===== */
        h2 {
            background-color: #f57c00; /* Cam đồng bộ với admin.jsp */
            color: white;
            padding: 18px 30px;
            margin: 0;
            font-size: 22px;
            display: flex;
            align-items: center;
            gap: 10px;
            font-weight: 600;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        /* ===== Bảng dữ liệu ===== */
        table {
            width: 90%;
            margin: 35px auto;
            border-collapse: collapse;
            background: white;
            border-radius: 6px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        th, td {
            padding: 12px 16px;
            border: 1px solid #ddd;
            text-align: left;
            font-size: 15px;
        }

        th {
            background-color: #f57c00;
            color: white;
            font-weight: bold;
        }

        tr:hover {
            background-color: #fff8e1;
        }

        /* ===== Cột Vai trò ===== */
        td.roles {
            color: #a14a00;
            font-weight: 500;
        }

        /* ===== Nút quay lại ===== */
        .back {
            text-align: center;
            margin: 25px 0 45px;
        }

        .back a {
            text-decoration: none;
            color: #f57c00;
            font-weight: bold;
            transition: 0.25s;
        }

        .back a:hover {
            color: #e46b00;
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <h2>👥 Danh sách người dùng</h2>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên đăng nhập</th>
                <th>Email</th>
                <th>Vai trò</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td class="roles">
                        <c:forEach var="r" items="${user.roles}">
                            ${r.name}<br>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="back">
        <a href="${pageContext.request.contextPath}/admin/dashboard">⬅ Quay lại trang Admin</a>
    </div>

</body>
</html>
