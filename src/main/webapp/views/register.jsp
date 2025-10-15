<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký | Cooking Guide</title>
    <style>
        :root {
            --primary-color: #ff8c00;
            --secondary-color: #ff5722;
            --dark-color: #333;
            --light-color: #fff;
            --gray-color: #666;
            --light-gray: #f5f5f5;
            --border-color: #e0e0e0;
            --success-color: #4caf50;
            --danger-color: #e53935;
        }

        body {
            font-family: "Segoe UI", sans-serif;
            margin: 0;
            background: linear-gradient(135deg, var(--light-gray), var(--light-color));
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .register-container {
            background: var(--light-color);
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

        label {
            display: block;
            font-weight: bold;
            color: var(--dark-color);
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid var(--border-color);
            border-radius: 6px;
            font-size: 14px;
            background: var(--light-gray);
            transition: 0.2s;
        }

        input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 3px var(--secondary-color);
            background: var(--light-color);
        }

        button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 6px;
            font-size: 15px;
            cursor: pointer;
            transition: 0.3s;
        }

        .btn-send {
            background-color: var(--gray-color);
            color: white;
            font-size: 14px;
            margin-top: 8px;
        }

        .btn-send:hover {
            background-color: var(--secondary-color);
        }

        .btn-register {
            background-color: var(--primary-color);
            color: var(--light-color);
            margin-top: 10px;
        }

        .btn-register:hover {
            background-color: var(--secondary-color);
        }

        .message {
            margin: 10px 0;
            color: var(--secondary-color);
            font-size: 14px;
        }

        .success {
            color: var(--success-color);
        }

        .error {
            color: var(--danger-color);
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

        #otpSection {
            display: none;
        }
    </style>
</head>
<body>

<div class="register-container">
    <img src="${pageContext.request.contextPath}/static/cook_icon.png" alt="Cooking Logo">
    <h2>Tạo tài khoản Cooking Guide</h2>

    <c:if test="${not empty requestScope.error}">
        <div class="message error">${requestScope.error}</div>
    </c:if>

    <c:if test="${not empty requestScope.success}">
        <div class="message success">${requestScope.success}</div>
    </c:if>

    <!-- Form đăng ký -->
    <form id="registerForm" action="${pageContext.request.contextPath}/register" method="post">
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
            <button type="button" class="btn-send" onclick="sendOtp()">Gửi mã OTP</button>
            <div id="emailMsg" class="message"></div>
        </div>

        <div id="otpSection" class="input-group">
            <label for="otp">Mã OTP</label>
            <input type="text" id="otp" maxlength="6" placeholder="Nhập mã gồm 6 chữ số">
            <button type="button" class="btn-send" onclick="verifyOtp()">Xác thực mã OTP</button>
            <div id="otpMsg" class="message"></div>
        </div>

        <div class="input-group">
            <label for="nickname">Biệt danh</label>
            <input type="text" id="nickname" name="nickname" placeholder="Nhập tên hiển thị">
        </div>

        <div class="input-group">
            <label for="age">Tuổi</label>
            <input type="number" id="age" name="age" min="1" max="120" placeholder="Nhập tuổi của bạn">
        </div>
        <button type="submit" id="registerBtn" class="btn-register" disabled>Đăng ký</button>
    </form>

    <div class="login-link">
        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
    </div>
</div>

<script>
    let verifiedEmail = false;

    function sendOtp() {
        const email = document.getElementById("email").value.trim();
        const emailMsg = document.getElementById("emailMsg");

        if (!email) {
            emailMsg.textContent = "Vui lòng nhập email hợp lệ!";
            emailMsg.className = "message error";
            return;
        }

        emailMsg.textContent = "⏳ Đang gửi mã OTP...";
        emailMsg.className = "message";

        fetch("${pageContext.request.contextPath}/send-otp", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: "email=" + encodeURIComponent(email)
        })
            .then(r => r.text())
            .then(text => {
                // Kiểm tra nội dung phản hồi
                if (text.startsWith("SUCCESS")) {
                    emailMsg.textContent = text;
                    emailMsg.className = "message success";
                    document.getElementById("otpSection").style.display = "block";
                } else {
                    emailMsg.textContent = text;
                    emailMsg.className = "message error";
                }
            })
            .catch(() => {
                emailMsg.textContent = "❌ Không thể gửi OTP, thử lại.";
                emailMsg.className = "message error";
            });
    }

    function verifyOtp() {
        const otp = document.getElementById("otp").value.trim();
        const email = document.getElementById("email").value.trim();
        const otpMsg = document.getElementById("otpMsg");
        if (!otp) {
            otpMsg.textContent = "Vui lòng nhập mã OTP!";
            otpMsg.className = "message error";
            return;
        }

        fetch("${pageContext.request.contextPath}/verify-otp", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: "email=" + encodeURIComponent(email) + "&otp=" + encodeURIComponent(otp)
        })
            .then(r => r.text())
            .then(resp => {
                if (resp.includes("SUCCESS:") || resp.toLowerCase().includes("thành công")) {
                    otpMsg.textContent = "✅ Xác thực thành công!";
                    otpMsg.className = "message success";
                    verifiedEmail = true;
                    document.getElementById("registerBtn").disabled = false;
                } else {
                    otpMsg.textContent = "❌ Mã OTP không hợp lệ hoặc đã hết hạn!";
                    otpMsg.className = "message error";
                }
            })
            .catch(() => {
                otpMsg.textContent = "❌ Không thể xác thực OTP, thử lại.";
                otpMsg.className = "message error";
            });
    }
</script>
</body>
</html>
