<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Thống kê hệ thống - Admin Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin-user.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Optional: Tăng độ cao card để cân bằng */
        .card { transition: transform 0.2s; }
        .card:hover { transform: translateY(-3px); }
    </style>
</head>
<body class="bg-light">

<div class="container mt-4">
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="back-link">Quay lại Dashboard</a>
    <h2 class="text-center mb-4 text-primary">📊 Thống kê hệ thống</h2>

    <!-- 6 thẻ thống kê -->
    <div class="row g-4 text-center mb-4">
        <div class="col-md-4 col-sm-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <h5 class="card-title">Tổng User</h5>
                    <h3 class="text-success fw-bold">${totalUsers}</h3>
                </div>
            </div>
        </div>
        
        <div class="col-md-4 col-sm-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <h5 class="card-title">Premium Users</h5>
                    <h3 class="text-warning fw-bold">${premiumUsers}</h3>
                </div>
            </div>
        </div>
        
        <div class="col-md-4 col-sm-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <h5 class="card-title">Tổng Recipe</h5>
                    <h3 class="text-info fw-bold">${totalRecipes}</h3>
                </div>
            </div>
        </div>

        <div class="col-md-4 col-sm-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <h5 class="card-title">Doanh thu</h5>
                    <h3 class="text-danger fw-bold">
                        <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true"/>₫
                    </h3>
                </div>
            </div>
        </div>

        <div class="col-md-4 col-sm-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <h5 class="card-title">Cooking Tips</h5>
                    <h3 class="text-secondary fw-bold">${totalTips}</h3>
                </div>
            </div>
        </div>
        
        <div class="col-md-4 col-sm-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <h5 class="card-title">Tổng Blog Posts</h5>
                    <h3 class="text-primary fw-bold">${totalBlogs}</h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Biểu đồ -->
    <div class="row g-4 mb-4">
        <div class="col-md-6">
            <div class="card shadow-sm h-100">
                <div class="card-body" style="height: 350px;"> <h5 class="card-title">Biểu đồ người dùng theo tháng</h5>
                    <canvas id="userChart"></canvas>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow-sm h-100">
                <div class="card-body" style="height: 350px;"> <h5 class="card-title">Doanh thu theo tháng</h5>
                    <canvas id="revenueChart"></canvas>
                </div>
            </div>
        </div>
    </div>

    <!-- Bảng giao dịch gần nhất -->
    <div class="card shadow-sm mt-4">
        <div class="card-body">
            <h5 class="card-title mb-3">🧾 Giao dịch gần nhất</h5>
            <table class="table table-bordered table-striped align-middle">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Người dùng</th>
                        <th>Gói</th>
                        <th>Giá trị</th>
                        <th>Ngày tạo</th>
                        <th>Trạng thái</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="tx" items="${recentTransactions}">
                        <tr>
                            <td>${tx.id}</td>
                            <td>${tx.user.username}</td>
                            <td>${tx.premiumPackage.name}</td>
                            <td><fmt:formatNumber value="${tx.amount}" type="number" groupingUsed="true"/> ₫</td>
                            <td><fmt:formatDate value="${tx.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${tx.status == 'COMPLETED'}">
                                        <span class="badge bg-success">Hoàn tất</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark">${tx.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<!-- Script biểu đồ -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    function parseChartData(dataString) {
        if (!dataString || dataString.trim().length < 2) return [];
        // Xóa dấu ngoặc vuông và chuyển đổi chuỗi thành mảng số/chuỗi JS
        try {
            return JSON.parse(dataString);
        } catch (e) {
            // Nếu không thể parse JSON (do lỗi định dạng từ Java), cố gắng xử lý thô
            return dataString.substring(1, dataString.length - 1).split(',').map(item => item.trim());
        }
    }
    // Đảm bảo dữ liệu từ EL là mảng JS hợp lệ
    const monthLabels = ${monthLabels}; // Giả định Service đã bọc [\"T1\", \"T2\", ...]
    const userCounts = ${userCounts};
    const revenueValues = ${revenueValues};

    // Biểu đồ người dùng
    const userChart = new Chart(document.getElementById('userChart'), {
        type: 'line',
        data: {
            labels: monthLabels,
            datasets: [{
                label: 'Người dùng mới',
                data: userCounts,
                borderColor: '#28a745',
                backgroundColor: 'rgba(40, 167, 69, 0.1)',
                fill: true,
                tension: 0.3
            }]
        },
        options: {
            // Cài đặt responsive
            responsive: true,
            maintainAspectRatio: false, // CỰC KỲ QUAN TRỌNG: Cho phép canvas thay đổi kích thước
            plugins: {
                legend: { position: 'top' }
            },
            scales: {
                y: { 
                    beginAtZero: true, 
                    ticks: { precision: 0 } 
                }
            }
        }
    });

    // Biểu đồ doanh thu — ĐÃ SỬA LỖI CÚ PHÁP
    const revenueChart = new Chart(document.getElementById('revenueChart'), {
        type: 'bar',
        data: {
            labels: ${monthLabels},
            datasets: [{
                label: 'Doanh thu (₫)',
                data: ${revenueValues},
                backgroundColor: 'rgba(220, 53, 69, 0.7)',
                borderColor: 'rgba(220, 53, 69, 1)',
                borderWidth: 1,
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'top' }
            },
            scales: {
                y: { 
                    beginAtZero: true, 
                    ticks: { 
                        callback: function(value) { 
                            return value.toLocaleString() + '₫'; 
                        } 
                    } 
                }
            }
        }
    });
});
</script>

</body>
</html>