<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Công thức yêu thích - Nấu Ngon</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/recipe.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/favorites.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
            <div class="header-content">
                <h1 class="logo">
                    <i class="fas fa-utensils"></i> Nấu<span>Ngon</span>
                </h1>
                <nav class="nav">
                    <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                    <!-- ✅ SỬA: Gọi servlet /favorites thay vì .jsp -->
                    <a href="${pageContext.request.contextPath}/favorites" class="active">Yêu thích</a>
                    <a href="${pageContext.request.contextPath}/profile">Tài khoản</a>
                </nav>
            </div>
        </div>
    </header>

    <!-- Main Content -->
    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <h2><i class="fas fa-heart"></i> Công thức yêu thích của tôi</h2>
                <p class="subtitle">
                    Tổng cộng 
                    <span>
                        <c:choose>
                            <c:when test="${empty favorites}">0</c:when>
                            <c:otherwise>${favorites.size()}</c:otherwise>
                        </c:choose>
                    </span> 
                    công thức
                </p>
            </div>

            <!-- Favorites Grid -->
            <div class="favorites-grid">
                <c:choose>
                    <c:when test="${empty favorites}">
                        <!-- Empty State -->
                        <div class="empty-favorites">
                            <i class="fas fa-heart-broken"></i>
                            <h3>Chưa có công thức yêu thích</h3>
                            <p>Hãy khám phá các món ăn ngon và thêm vào danh sách yêu thích của bạn!</p>
                            <a href="${pageContext.request.contextPath}/home" class="btn">
                                <i class="fas fa-search"></i>
                                Khám phá món ăn
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Favorites List -->
                        <c:forEach var="recipe" items="${favorites}">
                            <div class="recipe-card">
                                <div class="recipe-card-image">
                                    <img src="${recipe.imageUrl}" alt="${recipe.title}">
                                    <c:if test="${recipe.isVip}">
                                        <div class="recipe-card-badge">
                                            <i class="fas fa-crown"></i>
                                            VIP
                                        </div>
                                    </c:if>
                                </div>
                                
                                <div class="recipe-card-content">
                                    <h3 class="recipe-card-title">${recipe.title}</h3>
                                    
                                    <div class="recipe-card-meta">
                                        <span>
                                            <i class="fas fa-user"></i>
                                            ${recipe.author.nickname}
                                        </span>
                                        <span>
                                            <i class="fas fa-eye"></i>
                                            ${recipe.viewCount}
                                        </span>
                                    </div>
                                    
                                    <div class="recipe-card-rating">
                                        <div class="recipe-card-stars">
                                            <i class="fas fa-star"></i>
                                            <i class="fas fa-star"></i>
                                            <i class="fas fa-star"></i>
                                            <i class="fas fa-star"></i>
                                            <i class="fas fa-star"></i>
                                        </div>
                                        <span>4 (5 đánh giá)</span>
                                    </div>
                                    
                                    <div class="recipe-card-actions">
                                        <a href="${pageContext.request.contextPath}/recipe?id=${recipe.id}" 
                                           class="recipe-card-btn btn-view">
                                            <i class="fas fa-eye"></i>
                                            Xem chi tiết
                                        </a>
                                        <form action="${pageContext.request.contextPath}/favorite" method="POST" class="unfavorite-form">
                                            <%-- Gửi ID của công thức cần bỏ yêu thích --%>
                                            <input type="hidden" name="recipeId" value="${recipe.id}">

                                            <%-- Gửi hành động là "remove" để FavoriteServlet biết phải làm gì --%>
                                            <input type="hidden" name="action" value="remove">

                                            <%-- Thêm một input ẩn để báo cho Servlet biết cần quay lại trang favorites --%>
                                            <input type="hidden" name="redirect_to" value="favorites">

                                            <%-- Nút submit được style để trông giống một link/button bình thường --%>
                                            <button type="submit" class="unfavorite-btn">
                                                ❤︎ Bỏ yêu thích
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <p>&copy; 2024 Nấu Ngon. Tất cả quyền được bảo lưu.</p>
        </div>
    </footer>
</body>
</html>