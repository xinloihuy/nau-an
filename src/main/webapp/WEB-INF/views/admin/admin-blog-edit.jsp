<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sửa Blog</title>
    <style>
        body { font-family:'Segoe UI'; background:#fffdf8; padding:40px; }
        form { width:500px; margin:auto; background:white; padding:25px; border-radius:10px; box-shadow:0 4px 8px rgba(0,0,0,0.1); }
        label { font-weight:bold; }
        input, textarea { width:100%; padding:10px; margin-bottom:15px; border:1px solid #ccc; border-radius:6px; }
        button { background:#4CAF50; color:white; padding:10px 20px; border:none; border-radius:6px; cursor:pointer; }
        button:hover { background:#43a047; }
    </style>
</head>
<!-- 🔙 Nút quay lại trang blog -->
<div style="margin-bottom: 20px;">
    <a href="${pageContext.request.contextPath}/admin/blog"
       style="
           display: inline-block;
           background-color: #f57c00;
           color: white;
           font-weight: 500;
           padding: 10px 18px;
           border-radius: 6px;
           text-decoration: none;
           box-shadow: 0 3px 6px rgba(0,0,0,0.2);
           transition: 0.3s;
       "
       onmouseover="this.style.backgroundColor='#ff9800'"
       onmouseout="this.style.backgroundColor='#f57c00'">
       ⬅ Quay lại trang Blog
    </a>
</div>

<body>
    <h2>✏️ Sửa Blog</h2>
    <form method="post" action="${pageContext.request.contextPath}/admin/blog/edit">
        <input type="hidden" name="id" value="${blog.id}">

        <label>Tiêu đề:</label>
        <input type="text" name="title" value="${blog.title}" required>

        <label>Nội dung:</label>
        <textarea name="content" rows="5" required>${blog.content}</textarea>

        <button type="submit">Cập nhật</button>
    </form>
</body>
</html>
