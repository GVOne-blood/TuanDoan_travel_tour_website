// File: /public/admin/js/layoutLoader.js

// Hàm xử lý logout
function logout() {
    localStorage.removeItem('accessToken');
    window.location.href = '/login.html'; // Đảm bảo đường dẫn này đúng
}

$(document).ready(function() {
    // Kiểm tra token trước khi tải bất cứ thứ gì
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        // Không dùng alert, chuyển thẳng về trang login
        window.location.href = '/login.html';
        return; // Dừng ngay lập tức
    }

    // Thiết lập header cho tất cả các request ajax
    $.ajaxSetup({
        headers: { 'Authorization': 'Bearer ' + accessToken }
    });

    // Tải header và sidebar
    // Lưu ý: Đường dẫn phải chính xác từ gốc của trang web
    $('#header-placeholder').load('/templates/header-admin.html', function() {
        // Sau khi header đã được tải xong, gắn sự kiện cho nút logout
        $('#logout-button').on('click', function(e) {
            e.preventDefault();
            logout();
        });
    });

    $('#sidebar-placeholder').load('/templates/sidebar-admin.html', function() {
        // Sau khi sidebar tải xong, có thể thực hiện các logic khác nếu cần
        // Ví dụ: Đánh dấu active cho menu item hiện tại
        const currentPage = window.location.pathname;
        $('#sidenavAccordion .nav-link').each(function() {
            if (this.href.includes(currentPage)) {
                $(this).addClass('active');
                // Nếu là menu con, mở menu cha ra
                $(this).closest('.collapse').addClass('show');
                $(this).closest('.nav-item').find('.nav-link[data-bs-toggle="collapse"]').removeClass('collapsed');
            }
        });
    });
});