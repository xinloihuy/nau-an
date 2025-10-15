<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP | Cooking App</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(135deg, #fdfcfb, #e2d1c3);
            margin: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .otp-container {
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            width: 400px;
            padding: 30px;
            text-align: center;
        }
        h2 {
            color: teal;
        }
        input {
            width: 80%;
            padding: 10px;
            margin: 15px 0;
            border: 1px solid #ccc;
            border-radius: 8px;
            text-align: center;
            font-size: 18px;
            letter-spacing: 3px;
        }
        button {
            width: 85%;
            padding: 10px;
            background-color: teal;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
        }
        button:hover {
            background-color: #007d7d;
        }
        .resend {
            margin-top: 15px;
            display: block;
        }
        .msg {
            margin: 10px 0;
            color: green;
        }
        .error {
            color: tomato;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<div class="otp-container">
    <h2>Xác thực tài khoản</h2>

    <c:if test="${not empty message}">
        <div class="msg">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post">
        <input type="hidden" name="email" value="${email}">
        <input type="text" name="otp" maxlength="6" placeholder="Nhập mã OTP gồm 6 chữ số" required>
        <button type="submit">Xác thực</button>
    </form>

    <form action="${pageContext.request.contextPath}/send-otp" method="post" class="resend">
        <input type="hidden" name="email" value="${email}">
        <button type="submit" style="background:#999;">Gửi lại mã OTP</button>
    </form>
</div>
</body>
</html>
