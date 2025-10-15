<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
    <style>
        :root {
            --primary: teal;
            --primary-light: #009c9c;
            --bg: #f6f9fc;
            --card-bg: #ffffff;
            --text-dark: #333;
            --text-muted: #777;
            --danger: #e74c3c;
            --success: #2ecc71;
        }

        body {
            font-family: 'Segoe UI', sans-serif;
            background: var(--bg);
            margin: 0;
            padding: 0;
        }

        .profile-container {
            max-width: 800px;
            margin: 60px auto;
            background: var(--card-bg);
            border-radius: 20px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 40px 50px;
            transition: 0.3s;
        }

        .profile-container:hover {
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }

        .avatar {
            width: 110px;
            height: 110px;
            border-radius: 50%;
            background: var(--bg);
            border: 4px solid var(--primary);
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 45px;
            color: var(--primary);
            margin-bottom: 15px;
        }

        h2 {
            color: var(--text-dark);
            margin: 5px 0;
        }

        p.username {
            color: var(--text-muted);
            margin-bottom: 30px;
        }

        .form-grid {
            width: 100%;
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: 600;
            color: var(--text-dark);
            margin-bottom: 6px;
        }

        input {
            padding: 10px 12px;
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 15px;
            transition: border-color 0.2s;
        }

        input:focus {
            border-color: var(--primary);
            outline: none;
        }

        .btn-group {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-top: 30px;
        }

        .btn {
            border: none;
            border-radius: 8px;
            padding: 10px 20px;
            font-size: 15px;
            cursor: pointer;
            color: white;
            font-weight: 600;
            transition: background-color 0.2s;
        }

        .btn-primary {
            background-color: var(--primary);
        }

        .btn-primary:hover {
            background-color: var(--primary-light);
        }

        .btn-danger {
            background-color: var(--danger);
        }

        .btn-danger:hover {
            background-color: #c0392b;
        }

        .btn-secondary {
            background-color: #aaa;
        }

        .btn-secondary:hover {
            background-color: #888;
        }

        .divider {
            margin: 30px 0;
            height: 1px;
            background-color: #ddd;
            width: 100%;
        }

        .alert {
            background-color: #dff0d8;
            color: #3c763d;
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            width: 100%;
            text-align: center;
        }

        .error {
            background-color: #f2dede;
            color: #a94442;
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            width: 100%;
            text-align: center;
        }

        @media (max-width: 700px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>

    <script>
        function confirmDelete() {
            const box = confirm("⚠️ Bạn có chắc chắn muốn xóa tài khoản? Hành động này không thể hoàn tác!");
            if (box) document.getElementById("deleteForm").submit();
        }
    </script>
</head>
<body>
<c:import url="../includes/header.jsp"/>
<div class="profile-container">
    <div class="avatar">🍳</div>
    <h2>${user.username}</h2>
    <p class="username">@${user.nickname}</p>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form action="update-profile" method="post" style="width:100%;">
        <div class="form-grid">
            <div class="form-group">
                <label>Email</label>
                <input type="email" name="email" value="${user.email}" required>
            </div>

            <div class="form-group">
                <label>Biệt danh (Nickname)</label>
                <input type="text" name="nickname" value="${user.nickname}">
            </div>

            <div class="form-group">
                <label>Mật khẩu mới</label>
                <input type="password" name="newPassword" placeholder="Để trống nếu không đổi">
            </div>

            <div class="form-group">
                <label>Nhập lại mật khẩu mới</label>
                <input type="password" name="confirmPassword" placeholder="Xác nhận lại mật khẩu">
            </div>
        </div>

        <div class="btn-group">
            <button type="submit" class="btn btn-primary">💾 Lưu thay đổi</button>
            <button type="reset" class="btn btn-secondary">↩️ Hủy</button>
        </div>
    </form>

    <div class="divider"></div>

    <form id="deleteForm" action="delete-account" method="post">
        <button type="button" class="btn btn-danger" onclick="confirmDelete()">🗑️ Xóa tài khoản</button>
    </form>
</div>
</body>
</html>
