<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập Quản trị viên</title>
    <style>
        /* Tổng thể */
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background-color: #fffdf8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* Hộp đăng nhập */
        .login-box {
            width: 380px;
            background: #ffffff;
            padding: 40px 35px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);
            text-align: center;
            transition: 0.3s;
        }

        .login-box:hover {
            box-shadow: 0 4px 18px rgba(0, 0, 0, 0.2);
        }

        /* Tiêu đề */
        h2 {
            margin-bottom: 25px;
            color: #d46b08;
            font-weight: 700;
            font-size: 22px;
        }

        /* Ô nhập */
        input {
            width: 90%;
            padding: 12px 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 15px;
            outline: none;
            transition: 0.2s;
        }

        input:focus {
            border-color: #f57c00;
            box-shadow: 0 0 5px rgba(245, 124, 0, 0.4);
        }

        /* Nút đăng nhập */
        button {
            width: 95%;
            background: #f57c00;
            color: white;
            border: none;
            padding: 12px;
            margin-top: 15px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            transition: 0.25s;
        }

        button:hover {
            background: #e46b00;
        }

        /* Hiển thị lỗi */
        .error {
            color: red;
            margin-top: 10px;
            font-weight: 500;
        }

        /* Liên kết quay lại */
        .back-link {
            display: block;
            margin-top: 20px;
            color: #f57c00;
            text-decoration: none;
            font-weight: bold;
            transition: 0.25s;
        }

        .back-link:hover {
            color: #e46b00;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="login-box">
        <h2>Đăng nhập Quản trị viên</h2>
        <form action="${pageContext.request.contextPath}/admin/login" method="post">
            <input type="text" name="username" placeholder="Tên đăng nhập" required><br>
            <input type="password" name="password" placeholder="Mật khẩu" required><br>
            <button type="submit">Đăng nhập</button>
        </form>

        <p class="error">${error}</p>
        <a href="${pageContext.request.contextPath}/login" class="back-link">⬅ Quay lại trang người dùng</a>
    </div>
</body>
</html>
