<%-- 
    Document   : blog
    Created on : Oct 15, 2025, 7:39:34 PM
    Author     : Laptop Asus
--%>
<<<<<<< HEAD

<%@page contentType="text/html" pageEncoding="UTF-8"%>
=======
<%@page contentType="text/html" pageEncoding="UTF-8"%>

>>>>>>> f341c34c1fe90cdd19b11ce5d0672527e2c6e7bb
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Blog Cộng đồng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/home.css">
    <style>
body {
    background-color: #fffdf8;
    font-family: 'Segoe UI', sans-serif;
    margin: 0;
}

.main-content {
    margin-left: 250px;
    padding: 40px 60px;
    background: #fff;
    min-height: 100vh;
}

<<<<<<< HEAD
=======
/* --- Nút Follow/Unfollow --- */
.meta .follow-btn-container {
    display: inline-block;
    margin-left: 15px; /* Tăng khoảng cách một chút */
    vertical-align: middle;
}

.meta .btn-follow {
    padding: 4px 10px; /* Tăng padding để nút lớn hơn */
    font-size: 14px;
    line-height: 1.5;
    border-radius: 20px; /* BO TRÒN HOÀN TOÀN */
    font-weight: 600;
    transition: background-color 0.2s, transform 0.1s;
    border: 1px solid transparent;
}

/* Tông màu NÚT FOLLOW (Dùng màu cam/vàng chủ đạo) */
.btn-follow.follow-action {
    background-color: #f57c00; /* Màu cam chủ đạo */
    color: white;
    border-color: #f57c00;
}
.btn-follow.follow-action:hover {
    background-color: #ff9800; /* Màu cam sáng hơn khi hover */
    border-color: #ff9800;
}

/* Tông màu NÚT UNFOLLOW (Dùng tông màu nhẹ, dễ hủy hơn) */
.btn-follow.unfollow-action {
    background-color: transparent;
    color: #7a5a3a; /* Màu chữ nâu */
    border-color: #f0dfc3; /* Viền trùng với màu card */
}
.btn-follow.unfollow-action:hover {
    background-color: #fff4da;
    color: #3b2400;
}

>>>>>>> f341c34c1fe90cdd19b11ce5d0672527e2c6e7bb
h2 {
    color: #4a2c00;
    margin-bottom: 10px;
    font-weight: 700;
    text-align: center;
    font-size: 26px;
}

p.intro {
    color: #555;
    font-size: 16px;
    text-align: center;
    margin-bottom: 40px;
}

/* --- Danh sách blog --- */
.blog-list {
    display: flex;
    flex-direction: column;
    gap: 30px;
    max-width: 950px;
    margin: 0 auto;
}

/* --- Thẻ blog --- */
.blog-card {
    background: #fff8e7;
    border: 1px solid #f0dfc3;
    border-radius: 18px;
    box-shadow: 0 6px 15px rgba(0,0,0,0.08);
    padding: 28px 35px;
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
    justify-content: center;
    min-height: 180px;
    cursor: pointer;
}

.blog-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 20px rgba(0,0,0,0.12);
    background: #fff4da;
}

.blog-card h3 {
    color: #e67e22;
    margin-bottom: 12px;
    font-size: 22px;
    font-weight: 700;
}

.meta {
    color: #7a5a3a;
    font-size: 15px;
    margin-bottom: 12px;
}

.meta b {
    color: #3b2400;
}

.content {
    font-size: 16px;
    color: #333;
    line-height: 1.6;
    overflow-wrap: break-word;
}

/* --- Nút Đăng bài --- */
.add-blog-btn {
    position: fixed;
    bottom: 35px;
    right: 45px;
    background-color: #f57c00;
    color: white;
    font-size: 30px;
    border-radius: 50%;
    width: 70px;
    height: 70px;
    text-align: center;
    line-height: 68px;
    box-shadow: 0 6px 14px rgba(0,0,0,0.25);
    text-decoration: none;
    transition: 0.3s ease;
}

.add-blog-btn:hover {
    background-color: #ff9800;
    transform: scale(1.1);
}

/* --- Responsive --- */
@media (max-width: 768px) {
    .main-content {
        margin-left: 0;
        padding: 20px;
    }

    .blog-card {
        padding: 20px;
        min-height: 150px;
    }

    .blog-card h3 {
        font-size: 18px;
    }
}
</style>
</head>
<body>
<<<<<<< HEAD
    <%@ include file="/sidebar.jsp" %>

    <div class="main-content">
        <h2>📝 Blog Cộng đồng</h2>
        <p class="intro">Đây là nơi người dùng chia sẻ các bài viết, kinh nghiệm và công thức nấu ăn.</p>
        <div class="blog-list">
            <c:choose>
                <c:when test="${not empty blogs}">
                   <c:forEach var="b" items="${blogs}">
                        <div class="blog-card">
                           <h3>📝 ${b.title}</h3>
                            <p class="meta">
                               👤 Người đăng: <b>${b.author.username}</b> &nbsp;|&nbsp;
                                📅 ${b.createdAt}
                            </p>
                           <p class="content">${b.content}</p>
                        </div>
                    </c:forEach>
                </c:when>
            <c:otherwise>
                  <p>Hiện chưa có bài viết nào được đăng.</p>
             </c:otherwise>
            </c:choose>
        </div>

        
        
    </div>
    <!-- Nút Đăng bài -->
<a href="${pageContext.request.contextPath}/blog/add" class="add-blog-btn">✍️</a>

=======
    
    <%-- 1. INCLUDE sidebar.jsp ĐẦU TIÊN (NẾU NÓ LÀ INCLUDE DIRECTIVE) --%>
    <%@ include file="/sidebar.jsp" %> 

    <%-- 2. KHAI BÁO JSTL SAU KHI INCLUDE ĐỂ TRÁNH XUNG ĐỘT PREFIX 'c' --%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %> 
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
    <%-- 3. KHAI BÁO CÁC BIẾN EL SAU KHI KHAI BÁO TAGLIB --%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <c:set var="currentUser" value="${sessionScope.user}"/>
    <c:set var="isLoggedIn" value="${not empty currentUser}"/>
    <c:set var="currentUserId" value="${requestScope.currentUserId}"/> 
    <c:set var="userService" value="${requestScope.userService}"/> 


    <div class="main-content">
        <div class="blog-list">
            <c:choose>
                <c:when test="${not empty blogs}">
                    <c:forEach var="b" items="${blogs}">
                        <div class="blog-card">
                            
                            <a href="${contextPath}/blog/post?id=${b.id}">
                                <h3>📝 ${b.title}</h3>
                            </a>
                            
                            <p class="meta">
                                👤 Người đăng: 
                                
                                <%-- LÔ GIC TÊN VÀ NÚT FOLLOW --%>
                                <c:set var="authorId" value="${b.author.id}"/>
                                <c:set var="isCurrentUserAuthor" value="${currentUserId == authorId}"/>
                                
                                <b>
                                    <a href="${contextPath}/user/profile?id=${authorId}">
                                        ${b.author.username}
                                    </a>
                                </b>
                                
                                    <c:if test="${isLoggedIn && !isCurrentUserAuthor}">

                                         <c:set var="isFollowing" value="${userService.isFollowing(currentUserId, authorId)}"/>

                                         <div class="follow-btn-container">
                                             <form action="${contextPath}/blog/follow" method="post" style="display:inline;">
                                                 <input type="hidden" name="authorId" value="${authorId}">

                                                 <c:choose>
                                                     <c:when test="${isFollowing}">
                                                         <%-- TRẠNG THÁI: ĐANG THEO DÕI (Nút Bỏ theo dõi) --%>
                                                         <button type="submit" name="action" value="unfollow" class="btn btn-sm btn-follow unfollow-action">
                                                             <i class="fas fa-user-check"></i> Đang Theo dõi
                                                         </button>
                                                     </c:when>
                                                     <c:otherwise>
                                                         <%-- TRẠNG THÁI: CHƯA THEO DÕI (Nút Theo dõi) --%>
                                                         <button type="submit" name="action" value="follow" class="btn btn-sm btn-follow follow-action">
                                                             <i class="fas fa-user-plus"></i> Theo dõi
                                                         </button>
                                                     </c:otherwise>
                                                 </c:choose>
                                             </form>
                                         </div>
                                     </c:if>

                                &nbsp;|&nbsp;
                                📅 ${b.createdAt}
                            </p>
                            
                            <p class="content">${b.content}</p>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>Hiện chưa có bài viết nào được đăng.</p>
                </c:otherwise>
            </c:choose>
        </div>
        
    </div>
    
<a href="${contextPath}/blog/add" class="add-blog-btn">✍️</a>

</body>
>>>>>>> f341c34c1fe90cdd19b11ce5d0672527e2c6e7bb
<style>
.add-blog-btn {
    position: fixed;
    bottom: 30px;
    right: 40px;
    background-color: #f57c00;
    color: white;
    font-size: 28px;
    border-radius: 50%;
    width: 60px;
    height: 60px;
    text-align: center;
    line-height: 58px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
    text-decoration: none;
    transition: 0.3s ease;
}
.add-blog-btn:hover {
    background-color: #ff9800;
    transform: scale(1.1);
}
</style>
</body>
</html>