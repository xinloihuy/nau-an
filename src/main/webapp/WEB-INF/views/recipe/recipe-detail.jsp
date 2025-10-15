<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.mycompany.webhuongdannauan.utils.YouTubeUtil" %>
<%@page import="com.mycompany.webhuongdannauan.model.Recipe" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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
            <c:if test="${recipe.isVip}"><span class="badge-vip">💎 VIP</span></c:if>
        </h1>
        <p class="description">${recipe.description}</p>
        
        <%-- PHẦN THÔNG TIN ĐÃ CẬP NHẬT ĐỂ HIỂN THỊ ĐÁNH GIÁ --%>
        <div class="info">
            <span>Tác giả: ${recipe.author.nickname}</span> | 
            <span>Lượt xem: ${recipe.viewCount}</span> |
            <span class="average-rating-display" title="Điểm trung bình: ${recipe.averageRating > 0 ? String.format('%.1f', recipe.averageRating) : 'Chưa có đánh giá'}">
                <c:forEach var="i" begin="1" end="5">
                    <span class="star ${i <= Math.round(recipe.averageRating) ? 'filled' : ''}">★</span>
                </c:forEach>
            </span>
        </div>

        <div class="media-section">
            <img src="${recipe.imageUrl}" alt="${recipe.title}" class="main-image">
            <c:if test="${not empty recipe.videoUrl}">
                <div class="video-embed">
                    <%
                        Recipe recipeObj = (Recipe) request.getAttribute("recipe");
                        String embedUrl = YouTubeUtil.getEmbedUrl(recipeObj.getVideoUrl());
                    %>
                    <iframe src="<%= embedUrl %>" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
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

            <div class="interaction-controls">
                <%-- Nút Yêu thích, trạng thái được quyết định bởi servlet --%>
                <button id="favorite-btn" class="btn favorite-btn ${isFavorited ? 'favorited' : ''}">
                    ${isFavorited ? '❤️ Đã yêu thích' : '♡ Thêm vào yêu thích'}
                </button>
                <%-- Nút Chia sẻ --%>
                <button id="share-btn" class="btn share-btn">📤 Chia sẻ công thức</button>
            </div>
            
            <%-- Chỉ hiển thị form đánh giá và bình luận khi người dùng đã đăng nhập --%>
            <c:if test="${not empty sessionScope.user}">
                <div class="rating-section">
                    <h3>Đánh giá của bạn</h3>
                    <form id="rating-form" class="rating-form">
                        <div class="stars">
                            <input type="radio" name="score" value="5" id="star5"><label for="star5">★</label>
                            <input type="radio" name="score" value="4" id="star4"><label for="star4">★</label>
                            <input type="radio" name="score" value="3" id="star3"><label for="star3">★</label>
                            <input type="radio" name="score" value="2" id="star2"><label for="star2">★</label>
                            <input type="radio" name="score" value="1" id="star1"><label for="star1">★</label>
                        </div>
                        <button type="submit" class="btn rating-btn">Gửi đánh giá</button>
                    </form>
                </div>
                
                <div class="comments-section">
                    <h3>Bình luận</h3>
                    <form id="comment-form" class="comment-form">
                        <textarea name="content" placeholder="Viết bình luận của bạn..." required></textarea>
                        <button type="submit" class="btn comment-btn">Gửi bình luận</button>
                    </form>
                </div>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <p>Vui lòng <a href="${pageContext.request.contextPath}/login">đăng nhập</a> để bình luận và đánh giá.</p>
            </c:if>

            <div class="comments-list">
                <h3>Tất cả bình luận</h3>
                <c:forEach var="comment" items="${recipe.comments}">
                    <div class="comment-item" id="comment-${comment.id}">
                        <div class="comment-header">
                            <strong>${comment.user.nickname}</strong>
                            <span class="comment-date">
                                <fmt:formatDate value="${comment.createdAt}" pattern="HH:mm, dd/MM/yyyy"/>
                            </span>
                        </div>
                        <p class="comment-content">${comment.content}</p>
                        
                        <%-- Form sửa comment, mặc định bị ẩn --%>
                        <div class="comment-edit-form" style="display:none;">
                            <textarea>${comment.content}</textarea>
                            <button class="btn save-edit-btn" data-comment-id="${comment.id}">Lưu</button>
                            <button type="button" class="btn cancel-edit-btn">Hủy</button>
                        </div>
                        
                        <%-- Nút Sửa/Xóa chỉ hiện khi đúng người dùng --%>
                        <c:if test="${sessionScope.user.id == comment.user.id}">
                            <div class="comment-actions">
                                <button class="edit-comment-btn" data-comment-id="${comment.id}">Sửa</button>
                                <button class="delete-comment-btn" data-comment-id="${comment.id}">Xóa</button>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
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

<script>
document.addEventListener('DOMContentLoaded', function() {
    const contextPath = "${pageContext.request.contextPath}";
    const recipeId = "${recipe.id}";
    const currentUserId = "${sessionScope.user.id}";

    // --- FAVORITE ---
    const favoriteBtn = document.getElementById('favorite-btn');
    if (favoriteBtn) {
        favoriteBtn.addEventListener('click', async function() {
            if (!currentUserId) {
                window.location.href = `${contextPath}/login`;
                return;
            }
            const isFavorited = this.classList.contains('favorited');
            const action = isFavorited ? 'remove' : 'add';

            try {
                const response = await fetch(`${contextPath}/favorite`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: `recipeId=${recipeId}&action=${action}`
                });
                if (!response.ok) throw new Error('Yêu cầu thất bại.');
                const result = await response.json();
                if (result.status === 'success') {
                    this.classList.toggle('favorited');
                    this.textContent = isFavorited ? '♡ Thêm vào yêu thích' : '❤️ Đã yêu thích';
                } else { alert(result.message); }
            } catch (error) { alert('Đã xảy ra lỗi. Vui lòng thử lại.'); }
        });
    }

    // --- SHARE ---
    const shareBtn = document.getElementById('share-btn');
    if (shareBtn) {
        shareBtn.addEventListener('click', function() {
            navigator.clipboard.writeText(window.location.href)
                .then(() => alert('Đã sao chép link công thức!'))
                .catch(err => alert('Không thể tự động sao chép link.'));
        });
    }
    
    // --- RATING ---
    const ratingForm = document.getElementById('rating-form');
    if (ratingForm) {
        ratingForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const scoreInput = this.querySelector('input[name="score"]:checked');
            if (!scoreInput) {
                alert('Vui lòng chọn số sao để đánh giá.');
                return;
            }

            try {
                const response = await fetch(`${contextPath}/api/recipes/${recipeId}/rate`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: `score=${scoreInput.value}`
                });
                if (!response.ok) throw new Error('Yêu cầu thất bại.');
                const result = await response.json();
                if (result.status === 'success') {
                    alert('Cảm ơn bạn đã đánh giá!');
                    window.location.reload(); // Tải lại trang để cập nhật điểm trung bình
                } else { alert(result.message); }
            } catch (error) { alert('Đã xảy ra lỗi khi gửi đánh giá.'); }
        });
    }

    // --- COMMENTS (SỬ DỤNG EVENT DELEGATION) ---
    const commentsContainer = document.querySelector('.comments-section');

    // Thêm comment
    const commentForm = document.getElementById('comment-form');
    if (commentForm) {
        commentForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const contentArea = this.querySelector('textarea[name="content"]');
            const content = contentArea.value.trim();
            if (!content) return;

            try {
                const response = await fetch(`${contextPath}/comment?action=add&recipeId=${recipeId}`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: `content=${encodeURIComponent(content)}`
                });
                if (!response.ok) throw new Error('Yêu cầu thất bại.');
                const newComment = await response.json();
                if (newComment) {
                    contentArea.value = ''; // Xóa textarea
                    // Tải lại trang để hiển thị bình luận mới nhất và chính xác nhất
                    window.location.reload();
                }
            } catch (error) { alert('Đã xảy ra lỗi khi thêm bình luận.'); }
        });
    }
    
    // Xử lý Sửa, Xóa, Lưu, Hủy comment
    const commentsList = document.querySelector('.comments-list');
    if(commentsList) {
        commentsList.addEventListener('click', async function(e) {
            const target = e.target;
            const commentId = target.dataset.commentId;
            if (!commentId) return;

            const commentItem = document.getElementById(`comment-${commentId}`);
            
            // Nút Xóa
            if (target.classList.contains('delete-comment-btn')) {
                if (!confirm('Bạn có chắc chắn muốn xóa bình luận này?')) return;
                try {
                    const response = await fetch(`${contextPath}/comment?action=delete&commentId=${commentId}`, { method: 'POST' });
                    if (!response.ok) throw new Error('Yêu cầu xóa thất bại.');
                    const result = await response.json();
                    if (result.status === 'success') {
                        commentItem.remove();
                    } else { alert(result.message); }
                } catch (error) { alert('Lỗi khi xóa bình luận.'); }
            }

            // Nút Sửa (hiển thị form)
            if (target.classList.contains('edit-comment-btn')) {
                commentItem.querySelector('.comment-content').style.display = 'none';
                commentItem.querySelector('.comment-edit-form').style.display = 'block';
                target.style.display = 'none'; // Ẩn nút "Sửa"
            }

            // Nút Hủy
            if (target.classList.contains('cancel-edit-btn')) {
                commentItem.querySelector('.comment-content').style.display = 'block';
                commentItem.querySelector('.comment-edit-form').style.display = 'none';
                commentItem.querySelector('.edit-comment-btn').style.display = 'inline-block'; // Hiện lại nút "Sửa"
            }

            // Nút Lưu
            if (target.classList.contains('save-edit-btn')) {
                const newContent = commentItem.querySelector('.comment-edit-form textarea').value.trim();
                if (!newContent) return;

                try {
                    const response = await fetch(`${contextPath}/comment?action=edit&commentId=${commentId}`, {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: `content=${encodeURIComponent(newContent)}`
                    });
                    if (!response.ok) throw new Error('Yêu cầu sửa thất bại.');
                    const result = await response.json();
                    if (result.status === 'success') {
                        commentItem.querySelector('.comment-content').textContent = newContent;
                        commentItem.querySelector('.comment-content').style.display = 'block';
                        commentItem.querySelector('.comment-edit-form').style.display = 'none';
                        commentItem.querySelector('.edit-comment-btn').style.display = 'inline-block';
                    } else { alert(result.message); }
                } catch (error) { alert('Lỗi khi lưu bình luận.'); }
            }
        });
    }
});
</script>
</body>
</html>