<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${mode == 'EDIT' ? 'Sửa' : 'Thêm'} Món ăn - Admin</title> 
    <!--<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">-->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/recipe-admin.css">
</head>
<body>
    <header>HỆ THỐNG QUẢN TRỊ - MÓN ĂN</header>
    
    <section>
        <h2>${mode == 'EDIT' ? 'Chỉnh Sửa Món ăn' : 'Thêm Món ăn Mới'}</h2>
        
        <a href="${pageContext.request.contextPath}/admin/recipes" class="back-link">Quay lại danh sách</a>

        <c:if test="${not empty error}">
            <p class="message-error">${error}</p>
        </c:if>

        <div class="form-container">
            <form action="${pageContext.request.contextPath}/admin/recipes" method="POST">
                
                <c:if test="${recipe != null}">
                    <input type="hidden" name="id" value="${recipe.id}">
                    <p>Đang chỉnh sửa món ăn: <strong>${recipe.title} (ID: ${recipe.id})</strong></p>
                </c:if>

                <div class="form-group">
                    <label for="title">Tên Món Ăn (*)</label>
                    <input type="text" id="title" name="title" value="${recipe.title}" required>
                </div>

                <div class="form-group">
                    <label for="description">Mô tả tóm tắt</label>
                    <textarea id="description" name="description">${recipe.description}</textarea>
                </div>

                <div class="form-group">
                    <label for="ingredients">Nguyên liệu (*)</label>
                    <textarea id="ingredients" name="ingredients" required>${recipe.ingredients}</textarea>
                </div>

                <div class="form-group">
                    <label for="steps">Các bước thực hiện (*)</label>
                    <textarea id="steps" name="steps" required>${recipe.steps}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="imageUrl">URL Ảnh Món Ăn</label>
                    <input type="text" id="imageUrl" name="imageUrl" value="${recipe.imageUrl}">
                </div>

                <div class="form-group">
                    <label for="videoUrl">URL Video Hướng Dẫn</label>
                    <input type="text" id="videoUrl" name="videoUrl" value="${recipe.videoUrl}">
                </div>
                
                <div class="form-group">
                    <label for="cookingTimeMinutes">Thời gian nấu (phút)</label>
                    <input type="number" id="cookingTimeMinutes" name="cookingTimeMinutes" value="${recipe.cookingTimeMinutes}" min="1">
                </div>
                
                <div class="form-group">
                    <label>Loại:</label>
                    <div class="checkbox-group">
                        <input type="checkbox" id="isVip" name="isVip" 
                               <c:if test="${recipe != null && recipe.isVip}">checked</c:if>>
                        <label for="isVip">💎 Món VIP (Chỉ cho Premium)</label>
                    </div>
                </div>

                <div class="form-group">
                    <label>Danh mục (*)</label>
                    <select name="categories" multiple required size="5">
                        <c:forEach var="cat" items="${allCategories}">
                            <c:set var="isSelected" value="false"/>
                            <c:if test="${recipe != null}">
                                <c:forEach var="recipeCat" items="${recipe.categories}">
                                    <c:if test="${cat.id == recipeCat.id}">
                                        <c:set var="isSelected" value="true"/>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <option value="${cat.id}" <c:if test="${isSelected}">selected</c:if>>
                                ${cat.name}
                            </option>
                        </c:forEach>
                    </select>
                    <small>Giữ Ctrl/Cmd để chọn nhiều danh mục.</small>
                </div>

                <button type="submit" class="save-button">
                    ${mode == 'EDIT' ? 'LƯU CHỈNH SỬA' : 'THÊM MÓN ĂN'}
                </button>
            </form>
        </div>
    </section>
</body>
</html>