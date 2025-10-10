<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nâng cấp Premium</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .pricing-cards {
            display: flex;
            gap: 30px;
            margin-top: 40px;
            justify-content: center;
        }
        .pricing-card {
            flex: 1;
            max-width: 350px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            padding: 30px;
            text-align: center;
            transition: transform 0.3s;
        }
        .pricing-card:hover {
            transform: translateY(-5px);
            border-color: #d71a65;
        }
        .pricing-card.popular {
            border-color: #d71a65;
            position: relative;
        }
        .popular-badge {
            position: absolute;
            top: -15px;
            right: 20px;
            background: #d71a65;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
        }
        .price {
            font-size: 48px;
            font-weight: bold;
            color: #d71a65;
            margin: 20px 0;
        }
        .price-unit {
            font-size: 18px;
            color: #666;
        }
        .features {
            list-style: none;
            padding: 0;
            margin: 30px 0;
        }
        .features li {
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .buy-button {
            width: 100%;
            padding: 15px;
            background: #d71a65;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }
        .buy-button:hover {
            background: #b01552;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            text-align: center;
        }
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>🎉 Nâng cấp tài khoản Premium</h1>

    <%
        String status = request.getParameter("status");
        String msg = request.getParameter("msg");

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

    <div class="pricing-cards">
        <!-- Gói Monthly -->
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
                <input type="hidden" name="packageType" value="monthly">
                <button type="submit" class="buy-button">
                    Mua ngay
                </button>
            </form>
        </div>

        <!-- Gói Yearly -->
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
                <input type="hidden" name="packageType" value="yearly">
                <button type="submit" class="buy-button">
                    Mua ngay
                </button>
            </form>
        </div>
    </div>

    <div style="margin-top: 40px; text-align: center; color: #666;">
        <p>💳 Thanh toán an toàn qua MoMo Wallet</p>
        <img src="https://developers.momo.vn/v3/img/logo.png" alt="MoMo" height="40">
    </div>
</div>
</body>
</html>