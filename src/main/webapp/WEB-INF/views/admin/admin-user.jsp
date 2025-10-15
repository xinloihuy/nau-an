<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

        /* ===== Form thêm người dùng ===== */
        .add-form {
            width: 85%;
            margin: 25px auto 40px;
            text-align: center;
            background: #fff8e1;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .add-form input,
        .add-form select {
            padding: 8px 10px;
            margin: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .add-form button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 18px;
            border-radius: 6px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }

        .add-form button:hover {
            background-color: #43a047;
            transform: scale(1.05);
        }

        /* ===== Nút quay lại ===== */
        .back {
            text-align: center;
            margin: 35px 0 45px;
        }

        .back a {
            display: inline-block;
            font-size: 18px;
            background-color: #f57c00;
            color: white;
            padding: 12px 25px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            transition: 0.25s;
        }

        .back a:hover {
            background-color: #e46b00;
            transform: scale(1.05);
        }
    </style>
</head>
<body>

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