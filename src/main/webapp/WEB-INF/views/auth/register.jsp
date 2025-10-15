<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký | Cooking Guide</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
    <style>
        :root {
            --primary-color: #ff8c00;
            --secondary-color: #ff5722;
            --dark-color: #333333;
            --light-color: #ffffff;
            --gray-color: #666666;
            --light-gray: #f5f5f5;
            --border-color: #e0e0e0;
            --success-color: #4caf50;
        }

        body {
            font-family: "Segoe UI", sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, var(--light-gray), var(--light-color));
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .register-container {
            background-color: var(--light-color);
            padding: 40px 50px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
            width: 400px;
            text-align: center;
            border: 1px solid var(--border-color);
        }

        .register-container img {
            width: 80px;
            margin-bottom: 15px;
        }

        h2 {
            color: var(--primary-color);
            margin-bottom: 25px;
        }

        .input-group {
            margin-bottom: 18px;
            text-align: left;
        }

        .input-group label {
            display: block;
            font-weight: bold;
            color: var(--dark-color);
            margin-bottom: 5px;
        }

        .input-group input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid var(--border-color);
            border-radius: 6px;
            font-size: 14px;
            background-color: var(--light-gray);
            transition: 0.2s;
        }

        .input-group input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 3px var(--secondary-color);
            background-color: var(--light-color);
        }

        .btn-register {
            width: 100%;
            background-color: var(--primary-color);
            color: var(--light-color);
            border: none;
            padding: 12px;
            border-radius: 6px;
            font-size: 15px;
            cursor: pointer;
            transition: background 0.3s;
        }

        .btn-register:hover {
            background-color: var(--secondary-color);
        }

        .message {
            margin-top: 15px;
            color: var(--secondary-color);
            font-size: 14px;
        }

        .login-link {
            margin-top: 20px;
            font-size: 14px;
            color: var(--gray-color);
        }

        .login-link a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
        }

        .login-link a:hover {
            text-decoration: underline;
            color: var(--secondary-color);
        }
    </style>
</head>
<body>

<div class="register-container">
    <img src="${pageContext.request.contextPath}/static/cook_icon.png" alt="Cooking Logo">
    <h2>Tạo tài khoản Cooking Guide</h2>

    <!-- Thông báo lỗi -->
    <c:if test="${not empty requestScope.error}">
        <div class="message">${requestScope.error}</div>
    </c:if>

    <!-- Thông báo thành công -->
    <c:if test="${not empty requestScope.success}">
        <div class="message" style="color: var(--success-color);">${requestScope.success}</div>
    </c:if>

    <!-- Form đăng ký -->
    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="input-group">
            <label for="username">Tên đăng nhập *</label>
            <input type="text" id="username" name="username" required placeholder="Nhập tên đăng nhập">
        </div>

        <div class="input-group">
            <label for="password">Mật khẩu *</label>
            <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu">
        </div>

        <div class="input-group">
            <label for="email">Email *</label>
            <input type="email" id="email" name="email" required placeholder="Nhập email hợp lệ">
        </div>

        <div class="input-group">
            <label for="nickname">Biệt danh</label>
            <input type="text" id="nickname" name="nickname" placeholder="Nhập tên hiển thị">
        </div>

        <div class="input-group">
            <label for="age">Tuổi</label>
            <input type="number" id="age" name="age" min="1" max="120" placeholder="Nhập tuổi của bạn">
        </div>

        <button type="submit" class="btn-register">Đăng ký</button>
    </form>

    <div class="login-link">
        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
    </div>
</div>

</body>
</html>
