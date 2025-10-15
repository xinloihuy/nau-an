<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.webhuongdannauan.utils.YouTubeUtil" %>
<%@page import="com.mycompany.webhuongdannauan.model.Recipe" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${recipe.title} - Chi tiết công thức</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/recipe.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
</head>
<body>
    <div class="container">
        <p><a href="${pageContext.request.contextPath}/home">← Quay lại trang chủ</a></p>

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
        
        <%-- Hiển thị điểm rating trung bình (Dữ liệu này sẽ được truyền từ RecipeDetailServlet) --%>
        <c:if test="${avgRating > 0}">
             <p class="info">Đánh giá: ${String.format("%.1f", avgRating)} ★</p>
        </c:if>

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

        <div class="interaction-section">
            <h2>Tương tác</h2>
            
            <div class="rating-section">
                <h3>Đánh giá món ăn</h3>
                <div class="star-rating">
                    <form action="${pageContext.request.contextPath}/rate" method="POST">
                        <input type="hidden" name="recipeId" value="${recipe.id}">
                        
                        <div class="stars">
                            <input type="radio" name="ratingValue" value="5" id="star5" required>
                            <label for="star5">★</label>
                            <input type="radio" name="ratingValue" value="4" id="star4">
                            <label for="star4">★</label>
                            <input type="radio" name="ratingValue" value="3" id="star3">
                            <label for="star3">★</label>
                            <input type="radio" name="ratingValue" value="2" id="star2">
                            <label for="star2">★</label>
                            <input type="radio" name="ratingValue" value="1" id="star1">
                            <label for="star1">★</label>
                        </div>
                        <button type="submit" class="rating-btn">Đánh giá</button>
                    </form>
                </div>
            </div>
            
            <div class="favorite-section">
                <form action="${pageContext.request.contextPath}/favorite" method="POST">
                    <input type="hidden" name="recipeId" value="${recipe.id}">
                    
                    <c:choose>
                        <c:when test="${isFavorited}">
                            <input type="hidden" name="action" value="remove">
                            <button type="submit" class="favorite-btn favorited">💔 Xóa khỏi yêu thích</button>
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="action" value="add">
                            <button type="submit" class="favorite-btn">❤️ Thêm vào yêu thích</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
            
            <div class="share-section">
                <p>Để chia sẻ, hãy sao chép đường dẫn sau:</p>
                <input type="text" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/recipe?id=${recipe.id}" readonly style="width: 100%; padding: 5px;">
            </div>
            
            <div class="comments-section">
                <h3>Bình luận</h3>
                
                <form action="${pageContext.request.contextPath}/comment" method="POST" class="comment-form">
                    <input type="hidden" name="recipeId" value="${recipe.id}">
                    <input type="hidden" name="action" value="add">
                    <textarea name="commentText" placeholder="Viết bình luận của bạn..." required></textarea>
                    <button type="submit" class="comment-btn">Gửi bình luận</button>
                </form>
                
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
                                    <form action="${pageContext.request.contextPath}/comment" method="POST" style="display: inline;">
                                        <input type="hidden" name="recipeId" value="${recipe.id}"> <input type="hidden" name="commentId" value="${comment.id}">
                                        <input type="hidden" name="action" value="delete">
                                        <button type="submit" class="link-button" onclick="return confirm('Bạn chắc chắn muốn xóa bình luận này?')">Xóa</button>
                                    </form>
                                    <%-- <form action="${pageContext.request.contextPath}/edit-comment" method="GET" style="display: inline;">
                                        <input type="hidden" name="commentId" value="${comment.id}">
                                        <button type="submit" class="link-button">Sửa</button>
                                    </form> --%>
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