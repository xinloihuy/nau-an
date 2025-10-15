<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập Cooking Guide</title>
    <style>
        body {font-family: Arial; background: #f6f6f6;}
        .login-container {
            width: 400px; margin: 60px auto; padding: 30px;
            background: white; border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        input {
            width: 90%; padding: 10px; margin: 8px 0;
            border: 1px solid #ccc; border-radius: 5px;
        }
        button {
            width: 95%; background: orange; color: white;
            border: none; padding: 10px; margin-top: 10px;
            border-radius: 5px; cursor: pointer;
        }
        button:hover { background: darkorange; }
        a { color: orange; text-decoration: none; }
    </style>
</head>
<body>
    <div class="login-container">
        <img src="${pageContext.request.contextPath}/static/logo.png" width="80" alt="Logo">
        <h2>Đăng nhập Cooking Guide</h2>
        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <input type="text" name="username" placeholder="Nhập tên đăng nhập" required><br>
            <input type="password" name="password" placeholder="Nhập mật khẩu" required><br>
            <button type="submit">Đăng nhập</button>
        </form>

        <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a></p>
        <p>
            Hoặc bạn là quản trị viên?
            <a href="${pageContext.request.contextPath}/admin-login" 
               style="color:#f57c00; font-weight:bold; text-decoration:none; transition:0.2s;">
               Đăng nhập Admin
            </a>
        </p> 
    </div>
</body>
</html>
