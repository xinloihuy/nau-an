<%-- 
    Document   : blog-add
    Created on : Oct 15, 2025, 9:05:41 PM
    Author     : Laptop Asus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng bài Blog mới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/home.css">
    <style>
        .main-content {
            margin-left: 250px;
            padding: 40px;
        }

        h2 {
            color: #4a2c00;
        }

        form {
            background: #fffaf0;
            border: 1px solid #f0d8a8;
            border-radius: 15px;
            padding: 25px;
            max-width: 600px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #5a2e00;
        }

        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid #ccc;
            margin-top: 8px;
            font-size: 15px;
        }

        textarea {
            height: 150px;
        }

        button {
            margin-top: 20px;
            background: #f57c00;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            transition: 0.3s;
        }

        button:hover {
            background: #ff9800;
        }
    </style>
</head>
<body>
    <%@ include file="/sidebar.jsp" %>

    <div class="main-content">
        <h2>📝 Đăng bài Blog mới</h2>
        <form action="${pageContext.request.contextPath}/blog/add" method="post">
            <label>Tiêu đề:</label>
            <input type="text" name="title" required>

            <label>Tác giả:</label>
            <input type="text" name="author" required>

            <label>Nội dung:</label>
            <textarea name="content" required></textarea>

            <button type="submit">Đăng bài</button>
        </form>
    </div>
</body>
</html>

