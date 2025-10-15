<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hướng dẫn nấu ăn - Trang chủ</title>
    <link rel="stylesheet" href="styles/home.css">
    <link rel="stylesheet" href="styles/sidebar.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
    
    
</head>

<body>
    <!-- Sidebar -->
    
    <%@ include file="/sidebar.jsp" %>

    <section class="main-content">
<!--        <button class="toggle-sidebar">☰</button>-->
        <header>
            <form action="home" method="GET" class="search-bar">
                <input type="text" name="keyword" placeholder="Tìm tên món hay nguyên liệu..." value="${searchQuery != null ? searchQuery : ''}">
                <button type="submit">Tìm Kiếm</button>
            </form>
        </header>

        <div class="section">
            <h2>
                <c:choose>
                    <c:when test="${categoryTitle != null}">
                        Danh mục: ${categoryTitle}
                    </c:when>
                    <c:when test="${searchQuery != null}">
                        Kết quả tìm kiếm cho: "${searchQuery}" (${featuredRecipes.size()} món)
                    </c:when>
                    <c:otherwise>
                        🔥 Các món thịnh hành
                    </c:otherwise>
                </c:choose>
            </h2>
            
            
            <div class="food-grid">
                <c:if test="${empty featuredRecipes}">
                    <p>Không tìm thấy món ăn nào phù hợp.</p>
                </c:if>
                <c:forEach var="recipe" items="${featuredRecipes}">
                    <a href="recipe?id=${recipe.id}" class="food-item">
                        <img src="${recipe.imageUrl != null ? recipe.imageUrl : 'default_food.jpg'}" alt="${recipe.title}">
                        <span>${recipe.title}</span>
                    </a>
                </c:forEach>
            </div>
        </div>

        <div class="section">
            <h2>💎 Các món ăn cho người dùng premium</h2>
            <div class="premium-grid">
                <c:forEach var="vipRecipe" items="${premiumRecipes}">
                    <c:choose>
                        <c:when test="${isPremiumUser}">
                            <a href="recipe?id=${vipRecipe.id}" class="premium-item">
                                <img src="${vipRecipe.imageUrl != null ? vipRecipe.imageUrl : 'default_vip.jpg'}" alt="${vipRecipe.title}">
                                <h3>${vipRecipe.title}</h3>
                                <p>Công thức hấp dẫn dành riêng cho hội viên Premium.</p>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <div class="premium-item locked">
                                <img src="https://tse3.mm.bing.net/th/id/OIP.7brgm5QsB1_1evbf7eFczgHaHa?w=203&h=203&c=7&r=0&o=7&cb=12&dpr=1.6&pid=1.7&rm=3" alt="Món VIP bị khóa">
                                <h3>[Đã Khóa]</h3>
                                <p>Nâng cấp Premium để xem công thức này!</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
        
    </section>
                
        <!--<button class="open-btn">🔔</button>-->

        <script>
          const toggleButton = document.querySelector('.toggle-sidebar');
          const sidebar = document.querySelector('.sidebar');
          const mainContent = document.querySelector('.main-content');

          toggleButton.addEventListener('click', () => {
            sidebar.classList.toggle('hidden');
            mainContent.classList.toggle('shifted');
          });
        </script>
    </body>
    
</html>