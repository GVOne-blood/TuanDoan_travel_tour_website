$(function(){
    // Tải header
    $("#header-placeholder").load("templates/header.html", function() {
        // Sau khi header được tải xong, chạy logic kiểm tra trạng thái đăng nhập

        const accessToken = localStorage.getItem('accessToken');
        const username = localStorage.getItem('username');

        if (accessToken && username) {
            // --- ĐÃ ĐĂNG NHẬP ---
            $('.js-auth-btn').hide(); // Ẩn nút đăng nhập/đăng ký

            // Cập nhật tên và hiển thị khu vực profile
            $('#user-greet-name').text(username);
            $('.js-profile-section').show();

            // Gắn sự kiện cho nút Đăng xuất trong dropdown
            $('#logout-link').on('click', function (e) {
                e.preventDefault();
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('username');
                alert('Bạn đã đăng xuất thành công.');
                window.location.reload();
            });

        } else {
            // --- CHƯA ĐĂNG NHẬP ---
            $('.js-auth-btn').show(); // Hiện các nút đăng nhập/đăng ký
            $('.js-profile-section').hide(); // Ẩn khu vực profile
        }
    });

    // Tải footer
    $("#footer-placeholder").load("templates/footer.html");

    // ===================================================================
// HÀM RENDER TRANG LỖI (THÊM VÀO ĐÂY)
// ===================================================================
    function renderErrorPage(containerSelector, customMessage) {
        const container = $(containerSelector);
        if (!container.length) {
            console.error("Không tìm thấy container để render lỗi:", containerSelector);
            return;
        }

        // Tải template lỗi
        $.get("templates/error_template.html", function (errorHtml) {
            // Thay thế nội dung của container bằng template lỗi
            container.html(errorHtml);

            // Nếu có thông báo tùy chỉnh, cập nhật nó
            if (customMessage) {
                container.find("#error-message-placeholder").text(customMessage);
            }
        });
    }
});