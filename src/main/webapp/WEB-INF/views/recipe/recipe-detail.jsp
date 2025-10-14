<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${recipe.title} - Chi tiết công thức</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/recipe.css">
</head>
<body>
    <div class="container">
        <p><a href="${pageContext.request.contextPath}/home">← Quay lại trang chủ</a></p>

        <h1>${recipe.title} 
            <c:if test="${recipe.isVip}">
                <span class="badge-vip">💎 VIP</span>
            </c:if>
        </h1>
        <p class="description">${recipe.description}</p>
        <p class="info">Tác giả: ${recipe.author.nickname} | Lượt xem: ${recipe.viewCount}</p>

        <div class="media-section">
            <img src="${recipe.imageUrl}" alt="${recipe.title}" class="main-image">
            <c:if test="${recipe.videoUrl != null}">
                <div class="video-embed">
                    <iframe width="560" height="315" src="${recipe.videoUrl}" frameborder="0" allowfullscreen></iframe>
                </div>
            </c:if>
        </div>
        
        <div class="ingredients-section">
            <h2>Nguyên liệu</h2>
            <pre>${recipe.ingredients}</pre>
        </div>

        <div class="steps-section">
            <h2>Các bước thực hiện</h2>
            <pre>${recipe.steps}</pre>
        </div>

        <div class="related-section">
            <h2>Món ăn liên quan</h2>
            <div class="food-grid">
                <c:forEach var="related" items="${relatedRecipes}">
                    <a href="${pageContext.request.contextPath}/recipe?id=${related.id}" class="food-item">
                        <img src="${related.imageUrl}" alt="${related.title}">
                        <span>${related.title}</span>
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>
</body>
</html>