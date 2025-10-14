<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Hướng dẫn nấu ăn - Trang chủ</title>
  <link rel="stylesheet" href="styles/home.css">
</head>

<body>
  <!-- Sidebar -->
  <%@ include file="/sidebar.jsp" %>

  <!-- Nội dung chính -->
  <section class="main-content">
    <button class="toggle-sidebar">☰</button>
    <header>
      <div class="search-bar">
        <input type="text" placeholder="Tìm tên món hay nguyên liệu...">
        <button>Tìm Kiếm</button>
      </div>
    </header>

    <div class="section">
      <h2>🔥 Các món thịnh hành</h2>
      <div class="food-grid">
        <div class="food-item"><img src="https://cdn.tgdd.vn/Files/2021/09/17/1384339/cach-lam-ga-ran-gion-rum-thom-ngon-chuan-vi-nhu-ngoai-hang-202109171012155648.jpg"><span>Gà rán</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/Files/2020/03/18/1240382/cach-lam-banh-mi-viet-nam-thom-ngon-gion-rum-nhu-ngoai-hang-202303220947537331.jpg"><span>Bánh mì</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/2021/07/CookRecipe/GalleryStep/thit-kho-trung-cach-lam-thit-kho-trung-cuc-de-ngon-202107140924242126.jpg"><span>Thịt kho</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/2021/10/CookRecipe/GalleryStep/cach-lam-pho-bo-tai-gion-thom-ngon-dam-da-vi-ha-noi-202110221433264315.jpg"><span>Phở bò</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/2020/10/CookRecipe/GalleryStep/cach-lam-goi-cuon-tom-thit-ngon-ma-de-lam-tai-nha-202010191410198911.jpg"><span>Gỏi cuốn</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/Files/2021/03/11/1332764/cach-lam-lau-thai-chua-cay-ngon-nuc-mui-de-lam-tai-nha-202103111003578476.jpg"><span>Lẩu Thái</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/2020/07/22/1272079/cach-lam-bun-bo-hue-dam-da-chuan-vi-hue-202007220929432303.jpg"><span>Bún bò Huế</span></div>
        <div class="food-item"><img src="https://cdn.tgdd.vn/2021/07/CookRecipe/GalleryStep/cach-lam-com-tam-suon-bi-cha-ngon-nhu-ngoai-hang-202107300950403839.jpg"><span>Cơm tấm sườn bì chả</span></div>
      </div>
    </div>

    <div class="section">
      <h2>💎 Các món ăn cho người dùng premium</h2>
      <div class="premium-grid">
        <div class="premium-item">
          <img src="static/ga-nuong-mat-ong.png" alt="Gà nướng mật ong">
          <h3>Gà nướng mật ong</h3>
          <p>Công thức hấp dẫn dành riêng cho hội viên Premium.</p>
        </div>
        <div class="premium-item">
          <img src="static/mi-y-sot-bo-bam.png" alt="Mì Ý sốt bò bằm">
          <h3>Mì Ý bò bằm</h3>
          <p>Hương vị chuẩn Âu, phù hợp cho bữa tối gia đình.</p>
        </div>
        <div class="premium-item">
          <img src="static/banh-flan-trung-sua.png" alt="Bánh flan trứng sữa">
          <h3>Bánh flan trứng sữa</h3>
          <p>Món tráng miệng mềm mịn, ngọt dịu cực kỳ hấp dẫn.</p>
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