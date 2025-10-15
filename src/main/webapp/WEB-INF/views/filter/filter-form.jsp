<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Bộ Lọc Món Ăn Nâng Cao</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/filter.css">
    <link rel="stylesheet" href="styles/home.css">
</head>
<body>
    <%@ include file="/sidebar.jsp" %>
    
    <section class="main-content">
        <h1>🧂 Bộ Lọc Món Ăn Nâng Cao</h1>
        <p>Chọn các tiêu chí dưới đây để tìm món ăn chính xác hơn.</p>
        
        <form action="${pageContext.request.contextPath}/filter" method="GET" class="filter-form">
            
            <div class="form-group">
                <label for="keyword">Từ khóa (Tên món/Nguyên liệu):</label>
                <input type="text" id="keyword" name="keyword" placeholder="Ví dụ: Gà, Phở, Mật ong">
            </div>

            <div class="form-group">
                <label for="category">Danh mục:</label>
                <select id="category" name="category">
                    <option value="">-- Tất cả Danh mục --</option>
                    <c:forEach var="cat" items="${allCategories}">
                        <option value="${cat.id}">${cat.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="maxTime">Thời gian nấu tối đa (phút):</label>
                <input type="number" id="maxTime" name="maxTime" min="1" placeholder="Ví dụ: 30 (phút)">
            </div>
            
            <div class="form-group">
                <label>Có Video Hướng Dẫn:</label>
                <div>
                    <input type="radio" id="videoYes" name="hasVideo" value="true">
                    <label for="videoYes">Có</label>
                    <input type="radio" id="videoNo" name="hasVideo" value="false">
                    <label for="videoNo">Không</label>
                    <input type="radio" id="videoAny" name="hasVideo" value="">
                    <label for="videoAny">Bất kỳ</label>
                </div>
            </div>
            
            <div class="form-group">
                <label>Loại Món Ăn:</label>
                <div>
                    <input type="radio" id="vipYes" name="isVip" value="true">
                    <label for="vipYes">💎 Món VIP</label> 
                    
                    <input type="radio" id="vipNo" name="isVip" value="false">
                    <label for="vipNo">Món Thường</label>
                    
                    <input type="radio" id="vipAny" name="isVip" value="" checked>
                    <label for="vipAny">Bất kỳ</label>
                </div>
            </div>

            <button type="submit" class="buy-button">Lọc Kết Quả</button>
        </form>
    </section>
</body>
</html>