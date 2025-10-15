<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.webhuongdannauan.utils.YouTubeUtil" %> 
<%@page import="com.mycompany.webhuongdannauan.model.Recipe" %>
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

        <!-- Thông báo share -->
        <c:if test="${param.shared == 'true'}">
            <div class="share-notification">
                <p>🎉 Cảm ơn bạn đã chia sẻ công thức này!</p>
            </div>
        </c:if>

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
                    <%
                        // 1. Ép kiểu (cast) đối tượng về Recipe
                        Recipe recipeObj = (Recipe) request.getAttribute("recipe"); 

                        // 2. Lấy URL video an toàn
                        String videoUrl = recipeObj.getVideoUrl();

                        // 3. Chuyển đổi sang link nhúng
                        String embedUrl = YouTubeUtil.getEmbedUrl(videoUrl);
                    %>

                    <iframe 
                        src="<%= embedUrl %>" 
                        frameborder="0" 
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                        allowfullscreen>
                    </iframe>
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

        <!-- Phần tương tác mới -->
        <div class="interaction-section">
            <h2>Tương tác</h2>
            
            <!-- Rating Section -->
            <div class="rating-section">
                <h3>Đánh giá món ăn</h3>
                <div class="star-rating">
                    <form action="${pageContext.request.contextPath}/api/recipes/${recipe.id}/rate" method="POST">
                        <input type="hidden" name="userId" value="${sessionScope.user.id}">
                        <div class="stars">
                            <input type="radio" name="score" value="1" id="star1">
                            <label for="star1">★</label>
                            <input type="radio" name="score" value="2" id="star2">
                            <label for="star2">★</label>
                            <input type="radio" name="score" value="3" id="star3">
                            <label for="star3">★</label>
                            <input type="radio" name="score" value="4" id="star4">
                            <label for="star4">★</label>
                            <input type="radio" name="score" value="5" id="star5">
                            <label for="star5">★</label>
                        </div>
                        <button type="submit" class="rating-btn">Đánh giá</button>
                    </form>
                </div>
            </div>
            
            <!-- Favorite Section -->
            <div class="favorite-section">
                <form action="${pageContext.request.contextPath}/api/users/${sessionScope.user.id}/favorites" method="POST">
                    <input type="hidden" name="recipeId" value="${recipe.id}">
                    <button type="submit" class="favorite-btn">❤️ Thêm vào yêu thích</button>
                </form>
            </div>
            
            <!-- Share Section -->
            <div class="share-section">
                <a href="${pageContext.request.contextPath}/share/recipe/${recipe.id}" class="share-btn">📤 Chia sẻ</a>
            </div>
            
            <!-- Comments Section -->
            <div class="comments-section">
                <h3>Bình luận</h3>
                
                <!-- Add Comment Form -->
                <form action="${pageContext.request.contextPath}/api/recipes/${recipe.id}/comments" method="POST" class="comment-form">
                    <input type="hidden" name="userId" value="${sessionScope.user.id}">
                    <textarea name="content" placeholder="Viết bình luận của bạn..." required></textarea>
                    <button type="submit" class="comment-btn">Gửi bình luận</button>
                </form>
                
                <!-- Comments List -->
                <div class="comments-list">
                    <c:forEach var="comment" items="${comments}">
                        <div class="comment-item">
                            <div class="comment-header">
                                <strong>${comment.user.nickname}</strong>
                                <span class="comment-date">${comment.createdAt}</span>
                            </div>
                            <div class="comment-content">${comment.content}</div>
                            <c:if test="${sessionScope.user.id == comment.user.id}">
                                <div class="comment-actions">
                                    <a href="${pageContext.request.contextPath}/api/comments/${comment.id}" 
                                       class="edit-comment" onclick="return confirm('Sửa bình luận?')">Sửa</a>
                                    <a href="${pageContext.request.contextPath}/api/comments/${comment.id}" 
                                       class="delete-comment" onclick="return confirm('Xóa bình luận?')">Xóa</a>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
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
