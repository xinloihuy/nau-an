<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu | Cooking Guide</title>
    <style>
        body {
            font-family: "Segoe UI", sans-serif;
            background: linear-gradient(135deg, #fdfcfb, #e2d1c3);
            margin: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .forgot-container {
            background-color: #fff;
            padding: 40px 50px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.15);
            width: 400px;
            text-align: center;
        }

        .forgot-container img {
            width: 80px;
            margin-bottom: 15px;
        }

        h2 {
            color: #ff8c00;
            margin-bottom: 20px;
        }

        .input-group {
            margin-bottom: 18px;
            text-align: left;
        }

        label {
            display: block;
            font-weight: bold;
            color: #333;
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
            background-color: #f5f5f5;
            transition: 0.2s;
        }

        input:focus {
            background-color: #fff;
            border-color: #ff8c00;
            box-shadow: 0 0 3px #ff5722;
            outline: none;
        }

        button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 6px;
            background-color: #ff8c00;
            color: white;
            font-size: 15px;
            cursor: pointer;
            transition: 0.3s;
        }

        button:hover {
            background-color: #ff5722;
        }

        .message {
            margin-top: 10px;
            font-size: 14px;
            color: #ff5722;
        }

        .back-link {
            margin-top: 20px;
        }

        .back-link a {
            color: #ff8c00;
            text-decoration: none;
            font-weight: 500;
        }

        .back-link a:hover {
            color: #ff5722;
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="forgot-container">
    <img src="${pageContext.request.contextPath}/static/cook_icon.png" alt="Cooking Logo">
    <h2>Khôi phục mật khẩu</h2>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
        <div class="input-group">
            <label for="email">Nhập email của bạn</label>
            <input type="email" id="email" name="email" required placeholder="example@gmail.com">
        </div>

        <button type="submit">Gửi mã khôi phục</button>

        <c:if test="${not empty requestScope.message}">
            <div class="message">${requestScope.message}</div>
        </c:if>
        <c:if test="${not empty requestScope.error}">
            <div class="message" style="color:red;">${requestScope.error}</div>
        </c:if>
    </form>

    <div class="back-link">
        <a href="${pageContext.request.contextPath}/login">← Quay lại đăng nhập</a>
    </div>
</div>

</body>
</html>
