<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cooking Tips - Mẹo nấu ăn</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #fffaf0;
            margin: 0;
        }
        h2 {
            background-color: #f57c00;
            color: white;
            padding: 15px 25px;
            margin: 0;
            font-size: 22px;
        }
        ul {
            list-style: none;
            padding: 20px;
        }
        li {
            background: #fff;
            border-left: 5px solid #f57c00;
            padding: 15px;
            margin-bottom: 10px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: 0.2s;
        }
        li:hover {
            transform: scale(1.02);
            background: #fff8e1;
        }
        .back {
            text-align: center;
            margin: 20px;
        }
        .back a {
            color: #f57c00;
            font-weight: bold;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <h2>🍳 Mẹo nấu ăn hữu ích</h2>

    <ul>
        <c:forEach var="tip" items="${tips}">
            <div class="tip">
                <p><strong>${tip.title}</strong>: ${tip.content}</p>
            </div>
        </c:forEach>
    </ul>

    <div class="back">
        <a href="${pageContext.request.contextPath}/index.jsp">⬅ Quay lại Trang chủ</a>
    </div>
</body>
</html>
