<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập | Cooking Guide</title>
    <link rel="stylesheet" href="styles/login.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
</head>
<body>

<div class="login-container">
    <img src="${pageContext.request.contextPath}/static/cook_icon.png" alt="Cooking Logo">
    <h2>Đăng nhập Cooking Guide</h2>

    <!-- Thông báo lỗi -->
    <c:if test="${not empty requestScope.error}">
        <div class="message">${requestScope.error}</div>
    </c:if>

    <!-- Form đăng nhập -->
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="input-group">
            <label for="username">Tên đăng nhập</label>
            <input type="text" id="username" name="username" required placeholder="Nhập tên đăng nhập">
        </div>

        <div class="input-group">
            <label for="password">Mật khẩu</label>
            <input type="password" id="password" name="password" required placeholder="Nhập mật khẩu">
        </div>

        <button type="submit" class="btn-login">Đăng nhập</button>
        <div style="margin-top:12px;">
            <a href="${pageContext.request.contextPath}/forgot-password"
               style="color:#ff5722;text-decoration:none;font-size:14px;font-weight:500;">
                🔑 Quên mật khẩu?
            </a>
        </div>
    </form>

    <div class="register-link">
        Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
    </div>
    <div class="register-link">
        <p>
            Hoặc bạn là quản trị viên?

            <a href="${pageContext.request.contextPath}/admin/login" 
               style="color:#f57c00; font-weight:bold; text-decoration:none; transition:0.2s;">
               Đăng nhập Admin
            </a>
        </p> 
    </div>
    
</div>

</body>
</html>
