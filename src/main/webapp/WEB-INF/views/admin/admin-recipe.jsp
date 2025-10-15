<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Giả định đã kiểm tra quyền Admin ở AdminRecipeServlet --%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Món ăn - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/recipe-admin.css">
    <style>
        /* CSS cụ thể cho bảng */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }
        th, td {
            border: 1px solid #f2e3c5;
            padding: 12px 15px;
            text-align: left;
            font-size: 14px;
            color: #4b2e00;
        }
        th {
            background-color: #fff3e0;
            color: #d46b08;
            font-weight: bold;
        }
        .action-links a, .action-links button {
            text-decoration: none;
            margin-right: 10px;
            padding: 5px 10px;
            border-radius: 5px;
            font-size: 13px;
            cursor: pointer;
            border: none;
        }
        .edit-btn { background-color: #f57c00; color: white; }
        .delete-btn { background-color: #e46b00; color: white; }
        .add-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border-radius: 6px;
            text-decoration: none;
            margin-bottom: 15px;
            display: inline-block;
        }
    </style>
</head>
<body>
    <header>HỆ THỐNG QUẢN TRỊ - MÓN ĂN</header>
    
    <section>
        <h2>Quản lý Món ăn</h2>
        
        <c:if test="${not empty message}">
            <p style="color: green;">${message}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>

        <a href="${pageContext.request.contextPath}/admin/recipes?action=edit" class="add-btn">➕ Thêm Món ăn Mới</a>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên Món Ăn</th>
                    <th>Tác giả</th>
                    <th>Thời gian nấu (phút)</th>
                    <th>VIP</th>
                    <th>Lượt xem</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="recipe" items="${recipeList}">
                    <tr>
                        <td>${recipe.id}</td>
                        <td><a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" target="_blank">${recipe.title}</a></td>
                        <td>${recipe.author.nickname}</td>
                        <td>${recipe.cookingTimeMinutes}</td>
                        <td>
                            <c:if test="${recipe.isVip}">💎 VIP</c:if>
                            <c:if test="${!recipe.isVip}">Thường</c:if>
                        </td>
                        <td>${recipe.viewCount}</td>
                        <td class="action-links">
                            <a href="${pageContext.request.contextPath}/admin/recipes?action=edit&id=${recipe.id}" class="edit-btn">Sửa</a>
                            
                            <form action="${pageContext.request.contextPath}/admin/recipes" method="GET" style="display:inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${recipe.id}">
                                <button type="submit" class="delete-btn" 
                                        onclick="return confirm('Bạn có chắc chắn muốn xóa món ăn [${recipe.title}] không?');">
                                    Xóa
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
    </section>
</body>
</html>