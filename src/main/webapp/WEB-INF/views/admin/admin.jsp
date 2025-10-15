<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    // KIỂM TRA ĐÚNG KEY ĐƯỢC ĐẶT TRONG ADMINLOGINSERVLET
    // VÀ ĐẢM BẢO GIÁ TRỊ CỦA NÓ LÀ TRUE
    Boolean isAdmin = (Boolean) session.getAttribute("isAdmin"); 
    
    // Nếu session là null HOẶC không có key "isAdmin" HOẶC isAdmin = false
    if (isAdmin == null || !isAdmin) { 
        response.sendRedirect(request.getContextPath() + "/admin/login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang quản trị hệ thống</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
    <style>
        /* ====== Giao diện tổng thể ====== */
        body {
            margin: 0;
            font-family: 'Segoe UI', sans-serif;
            background-color: #fffdf8;
        }

        header {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 65px;
            background-color: #f57c00;
            color: white;
            display: flex;
            align-items: center;
            padding-left: 250px; /* chừa khoảng trống cho sidebar */
            font-size: 22px;
            font-weight: bold;
            letter-spacing: 0.5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            z-index: 1000; /* luôn nằm trên cùng */
            }

        /* ====== Sidebar ====== */
        nav {
            width: 230px;
            background-color: #fff3e0;
            border-right: 2px solid #f5cc8d;
            position: fixed;
            top: 65px; /* đẩy xuống dưới header */
            left: 0;
            bottom: 0;
            padding-top: 10px;
            overflow-y: auto;
            z-index: 999;
        }

        nav ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        nav ul li {
            border-bottom: 1px solid #f2e3c5;
        }

        nav ul li button {
            background: none;
            border: none;
            width: 100%;
            text-align: left;
            font-size: 16px;
            color: #d46b08;
            font-weight: 600;
            padding: 14px 20px;
            cursor: pointer;
            transition: 0.2s;
        }

        nav ul li button:hover {
            background-color: #ffe0b2;
            color: #a85503;
        }

        nav ul li a {
            display: block;
            text-decoration: none;
            color: #5a2d00;
            padding: 14px 22px;
            transition: 0.2s;
        }

        nav ul li a:hover {
            background-color: #ffe0b2;
            border-radius: 6px;
            color: #a85503;
        }

        /* ====== Menu con (submenu) ====== */
        .submenu {
            display: none;
            background-color: #fffaf3;
            border-left: 3px solid #f5cc8d;
        }

        .submenu li a {
            padding-left: 35px;
            font-size: 15px;
        }

        /* ====== Nút đăng xuất ====== */
        .logout {
            display: block;
            width: calc(100% - 40px);
            margin: 25px auto;
            text-align: center;
            background-color: #f57c00;
            color: white;
            text-decoration: none;
            padding: 10px 0;
            border-radius: 6px;
            font-weight: bold;
            transition: 0.2s;
        }

        .logout:hover {
            background-color: #e46b00;
        }

        /* ====== Nội dung chính ====== */
        section {
            margin-left: 230px;
            padding: 90px 40px 40px 40px; /* thêm 90px top để tránh header */
            background-color: #fffdf8;
        }

        h2 {
            color: #d46b08;
            font-weight: bold;
            font-size: 26px;
            margin-bottom: 5px;
        }

        p {
            color: #4b2e00;
            font-size: 16px;
            margin-bottom: 30px;
        }

        /* ====== Dashboard cards ====== */
        .feature-box {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 25px;
            max-width: 850px;
        }

        .box {
            background: #ffffff;
            border: 1px solid #f4e6b3;
            border-radius: 12px;
            padding: 25px;
            text-align: center;
            font-size: 18px;
            color: #5a2d00;
            font-weight: bold;
            box-shadow: 0 2px 5px rgba(0,0,0,0.08);
            transition: all 0.2s;
            cursor: pointer;
        }

        .box:hover {
            background-color: #fff8e1;
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

    <header>HỆ THỐNG QUẢN TRỊ - ADMIN</header>

    <nav>
        <ul>
            <li>
                <button onclick="toggleSubmenu()">📊 Trang chủ / Dashboard ▼</button>
                <ul class="submenu" id="submenu">
                    <li><a href="#">👥 Quản lý người dùng</a></li>
                    <li><a href="#">🍲 Quản lý món ăn</a></li>
                    <li><a href="#">📝 Quản lý blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/cooking-tips">💡 Cooking Tip</a></li>
                     
                    <li><a href="${pageContext.request.contextPath}/admin/statistics">📈 Thống kê</a></li>
                    <li><a href="#">💬 ChatBox hỗ trợ</a></li>
                </ul>
            </li>
        </ul>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="logout">🚪 Đăng xuất</a>
        <a href="${pageContext.request.contextPath}/home" class="logout">🚪 Home</a>
    </nav>

    <section>
        <h2>Xin chào, Admin!</h2>
        <p>Chào mừng bạn đến với trang quản trị hệ thống hướng dẫn nấu ăn.</p>

        <div class="feature-box">
            <div class="box" onclick="location.href='${pageContext.request.contextPath}/admin/users'">👥 Quản lý người dùng</div>
            <div class="box" onclick="location.href='${pageContext.request.contextPath}/admin/recipes'">🍲 Quản lý món ăn</div>
            <div class="box">📝 Quản lý blog</div>
            <div class="box" onclick="location.href='${pageContext.request.contextPath}/admin/cooking-tips'">💡 Cooking Tip</div>
            <div class="box" onclick="location.href='${pageContext.request.contextPath}/admin/chat'">💬 ChatBox hỗ trợ</div>
            <div class="box" onclick="location.href='${pageContext.request.contextPath}/admin/statistics'">📈 Thống kê</div>
        </div>
    </section>

    <script>
        // Toggle hiển thị menu con
        function toggleSubmenu() {
            const submenu = document.getElementById("submenu");
            submenu.style.display = submenu.style.display === "block" ? "none" : "block";
        }
    </script>

</body>
</html>
