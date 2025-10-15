<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css" type="text/css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
</head>
<body>
<header style="display:flex; align-items:center; justify-content:space-between; padding:10px 20px; border-bottom:1px solid #ccc;">
    <div style="display:flex; align-items:center;">
        <img src="${pageContext.request.contextPath}/static/cook_icon.png" alt="Cook Logo" style="width:60px; margin-right:15px;">
        <h2 style="margin:0; color:teal;">Cooking</h2>
    </div>

    <div>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login"
                   style="text-decoration:none; color:white; background-color:teal; padding:8px 15px; border-radius:5px;">
                    Đăng nhập
                </a>
            </c:when>

            <c:otherwise>
                <a href="${pageContext.request.contextPath}/profile"
                   style="text-decoration:none; color:white; background-color:teal; padding:8px 15px; border-radius:5px; margin-right:10px;">
                    🧑‍🍳 Tài khoản
                </a>

                <form action="${pageContext.request.contextPath}/logout" method="post" style="display:inline;">
                    <button type="submit"
                            style="background-color:tomato; color:white; border:none; padding:8px 15px; border-radius:5px; cursor:pointer;">
                        Đăng xuất (${sessionScope.user.username})
                    </button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</header>
