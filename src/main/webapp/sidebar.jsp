<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
    Dữ liệu cần thiết:
    - sessionScope.userId: ID người dùng (kiểm tra trạng thái đăng nhập)
    - sessionScope.username: Tên người dùng (hiển thị)
    - requestScope.categoriesWithCount: Map<Category, Long> cho Danh mục món ăn
--%>
<head>
    <link rel="stylesheet" href="styles/sidebar.css">
<link rel="stylesheet" href="styles/home.css">
</head>
<button class="toggle-sidebar">☰</button>
<aside class="sidebar">
    <div>
        
        <div class="logo-area">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/home" style="text-decoration: none; color: inherit;">
                    <img src="https://cdn-icons-png.flaticon.com/512/706/706164.png  " alt="logo">
                    Hướng dẫn nấu ăn
                </a>
            </div>
        </div>

        <div class="menu">
            
            <a href="${pageContext.request.contextPath}/home" class="menu-item">🏠 Trang chủ</a>
            
            <%-- <a href="search.jsp" class="menu-item">🔍 Tìm kiếm</a> --%>
           
            <a href="${pageContext.request.contextPath}/blog" class="menu-item">📝 Blog Cộng đồng</a>
            
            <a href="${pageContext.request.contextPath}/filter" class="menu-item">🧂 Bộ lọc Nâng cao</a>
            
            <c:if test="${sessionScope.userId != null}">
                <a href="${pageContext.request.contextPath}/favorites" class="menu-item">❤️ Yêu thích</a>
                <a href="${pageContext.request.contextPath}/user/notifications" class="menu-item">🔔 Thông báo</a>
            </c:if>

            <a href="${pageContext.request.contextPath}/cooking-tips" class="menu-item">👨‍🍳 Cooking Tips</a>
            <a href="${pageContext.request.contextPath}/premium" class="menu-item">💎 Gói Premium</a>
        </div>
        
        <div class="menu-separator"></div> 
        
        <!-- Danh mục món ăn - Có thể thu gọn -->
        <div class="category-section">
            <div class="menu-item category-title collapsible">
                <span class="icon">🍽️</span> <span class="category-label">Danh mục món ăn</span> <span class="collapse-icon">▼</span>
            </div>
            
            <div class="category-content menu">
                <c:choose>
                    <c:when test="${not empty categoriesWithCount}">
                        <c:forEach var="entry" items="${categoriesWithCount}">
                            <a href="${pageContext.request.contextPath}/category?id=${entry.key.id}" class="menu-item category-item">
                                ${entry.key.name} (${entry.value})
                            </a>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="menu-item">Chưa có danh mục nào.</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <!-- End Danh mục -->
    </div>
    
    <c:choose>
        <c:when test="${sessionScope.userId != null}">
            <div class="user-area">
                <a href="${pageContext.request.contextPath}/profile" class="login">
                    👤 ${sessionScope.username != null ? sessionScope.username : 'Tài khoản'}
                </a>
                <br>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="login">
                    🚪 Đăng xuất
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/login" class="login">🚪 Đăng nhập / Đăng ký</a>
        </c:otherwise>
    </c:choose>
</aside>
        
<button class="open-btn">🔔</button>

  <script>
    const toggleButton = document.querySelector('.toggle-sidebar');
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');

    toggleButton.addEventListener('click', () => {
      sidebar.classList.toggle('hidden');
      mainContent.classList.toggle('shifted');
    });
  </script>


<script>
document.addEventListener('DOMContentLoaded', function() {
    // Collapsible category
    const collapsible = document.querySelector('.category-title.collapsible');
    const content = document.querySelector('.category-content');
    const icon = collapsible?.querySelector('.collapse-icon');
    
    if (collapsible && content && icon) {
        content.style.maxHeight = content.scrollHeight + "px";
        collapsible.classList.add('active');
        icon.textContent = '▼';
        
        collapsible.addEventListener('click', function() {
            this.classList.toggle('active');
            if (content.style.maxHeight) {
                content.style.maxHeight = null;
                icon.textContent = '▶';
            } else {
                content.style.maxHeight = content.scrollHeight + "px";
                icon.textContent = '▼';
            }
        });
    }

    // Toggle sidebar
    const toggleBtn = document.querySelector('.toggle-sidebar-btn');
    const sidebar = document.querySelector('.sidebar');
    
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('hidden');
        });
    }
});
</script>