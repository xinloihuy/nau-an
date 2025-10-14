<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Nâng cấp Premium</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles/home.css">
    <link rel="stylesheet" href="styles/premium.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/cook_icon.png">
</head>
<body> 
  <!-- Sidebar -->
  <%@ include file="/sidebar.jsp" %>

  <!-- Nội dung chính -->
  <section class="main-content">
    <button class="toggle-sidebar">☰</button>
    <div class="premium-container">
        <h1>🎉 Nâng cấp tài khoản Premium</h1>

        <div class="premium-container">
        <%
            // Lấy tham số status và msg từ URL (sau khi MoMo redirect về)
            String status = request.getParameter("status");
            String msg = request.getParameter("msg");

            // Kiểm tra và hiển thị thông báo
            if ("success".equals(status)) {
        %>
        <div class="alert alert-success">
            ✅ Thanh toán thành công! Tài khoản của bạn đã được nâng cấp lên Premium.
        </div>
        <%
        } else if ("error".equals(status)) {
        %>
        <div class="alert alert-error">
            ❌ Thanh toán thất bại: <%= msg != null ? msg : "Có lỗi xảy ra" %>
        </div>
        <%
            }
        %>
        </div>

        <div class="pricing-cards">
        
        <div class="pricing-card">
            <h2>Gói Tháng</h2>
            <div class="price">
                99.000₫
                <div class="price-unit">/tháng</div>
            </div>
            <ul class="features">
                <li>✓ Không quảng cáo</li>
                <li>✓ Download không giới hạn</li>
                <li>✓ Chất lượng HD</li>
                <li>✓ Hỗ trợ ưu tiên</li>
            </ul>
            
            <form action="buy-premium" method="post">
                <input type="hidden" name="packageId" value="1">
                <button type="submit" class="buy-button">Mua ngay</button>
            </form>
        </div>

        <div class="pricing-card popular">
            <span class="popular-badge">Phổ biến nhất</span>
            <h2>Gói Năm</h2>
            <div class="price">
                990.000₫
                <div class="price-unit">/năm</div>
            </div>
            <ul class="features">
                <li>✓ Tất cả tính năng gói tháng</li>
                <li>✓ Tiết kiệm 17%</li>
                <li>✓ Tặng 2 tháng</li>
                <li>✓ Badge đặc biệt</li>
            </ul>
            
            <form action="buy-premium" method="post">
                <input type="hidden" name="packageId" value="2">
                <button type="submit" class="buy-button">Mua ngay</button>
            </form>
        </div>
        </div>

        <div class="payment-info">
            <div class="payment-content">
                <p>💳 Thanh toán an toàn qua MoMo Wallet</p>
                <img src="/static/momo.png" alt="MoMo" height="40">
            </div>
        </div>
    </div>
  </section>

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
</body>
</html>