<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Thống kê hệ thống - Admin Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-4">
    <h2 class="text-center mb-4 text-primary">📊 Thống kê hệ thống</h2>

    <!-- 3 thẻ thống kê chính -->
    <div class="row text-center mb-4">
        <div class="col-md-3">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5>Tổng User</h5>
                    <h3 class="text-success fw-bold">${totalUsers}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5>Premium Users</h5>
                    <h3 class="text-warning fw-bold">${premiumUsers}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5>Tổng Recipe</h5>
                    <h3 class="text-info fw-bold">${totalRecipes}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5>Doanh thu</h5>
                    <h3 class="text-danger fw-bold">
                        <fmt:formatNumber value="${totalRevenue}" type="number" groupingUsed="true"/>₫
                    </h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Biểu đồ -->
    <div class="row">
        <div class="col-md-6">
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h5 class="card-title">Biểu đồ người dùng theo tháng</h5>
                    <canvas id="userChart"></canvas>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h5 class="card-title">Doanh thu theo tháng</h5>
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

<script>
const userChart = new Chart(document.getElementById('userChart'), {
    type: 'line',
    data: {
        labels: ${monthLabels}, // Ví dụ ["T1","T2","T3","T4"]
        datasets: [{
            label: 'Người dùng mới',
            data: ${userCounts},
            borderColor: 'blue',
            fill: false,
            tension: 0.3
        }]
    }
}); 

const revenueChart = new Chart(document.getElementById('revenueChart'), {
    type: 'bar',
    data: {
        labels: ${monthLabels},
        datasets: [{
            label: 'Doanh thu (₫)',
            data: ${revenueValues},
            backgroundColor: 'rgba(255,99,132,0.7)',
            borderRadius: 6
        }]
    }
});
</script>

</body>
</html>
