<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa hồ sơ</title>
    <style>
        body {
            font-family: "Segoe UI", sans-serif;
            background: linear-gradient(135deg, #e2d1c3, #fdfcfb);
        }
        .form-container {
            width: 400px;
            background-color: #fff;
            margin: 80px auto;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        input {
            width: 100%;
            margin: 8px 0;
            padding: 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }
        button {
            width: 100%;
            margin-top: 15px;
            padding: 10px;
            background-color: #6c63ff;
            border: none;
            color: white;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
        }
        button:hover {
            background-color: #5548e5;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Chỉnh sửa hồ sơ</h2>
    <form action="update-profile" method="post">
        <label>Họ tên</label>
        <input type="text" name="fullname" value="${user.fullname}" required>

        <label>Biệt danh</label>
        <input type="text" name="nickname" value="${user.nickname}">

        <label>Email</label>
        <input type="email" name="email" value="${user.email}" required>

        <label>Tuổi</label>
        <input type="number" name="age" value="${user.age}" min="1">

        <button type="submit">Lưu thay đổi</button>
    </form>
</div>
</body>
</html>
