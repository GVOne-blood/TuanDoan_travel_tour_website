(function () {
  "use strict";

  console.log("main.js đã được tải và sẵn sàng!");

  // ===================================================================
  // LOGIC REFRESH TOKEN TRUNG TÂM
  // ===================================================================
  let isRefreshing = false;
  let failedQueue = [];

  const processFailedQueue = (error, token) => {
    failedQueue.forEach(prom => {
      if (error) { prom.reject(error); }
      else { prom.resolve(token); }
    });
    failedQueue = [];
  };

  const isTokenExpired = (token) => {
    if (!token) return true;
    try {
      const decoded = jwt_decode(token);
      const currentTime = Date.now() / 1000;
      return decoded.exp < currentTime;
    } catch (e) {
      return true;
    }
  };

  const refreshToken = () => {
    const refreshTokenValue = localStorage.getItem('refreshToken');
    const accessToken = localStorage.getItem('accessToken');

    // Nếu không có refresh token hoặc refresh token đã hết hạn
    if (!refreshTokenValue || isTokenExpired(refreshTokenValue)) {
      logout();
      return $.Deferred().reject({ status: 401, responseText: 'No valid refresh token available' }).promise();
    }

    // Nếu access token vẫn còn hạn, không cần refresh
    if (accessToken && !isTokenExpired(accessToken)) {
      return $.Deferred().resolve({ data: { accessToken } }).promise();
    }

    isRefreshing = true;
    return $.ajax({
      url: 'http://localhost:8085/api/auth/refresh',
      type: 'POST',
      headers: { 'r_token': refreshTokenValue }
    }).done(response => {
      if (response && response.data) {
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);
        processFailedQueue(null, response.data.accessToken);
      }
    }).fail(err => {
      processFailedQueue(err, null);
      logout();
    }).always(() => {
      isRefreshing = false;
    });
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    alert('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
    window.location.href = 'login.html';
  };

  // Thiết lập Interceptor cho TẤT CẢ các yêu cầu AJAX
  // Trong main.js
  $.ajaxSetup({
    beforeSend: function(xhr) {
      const token = localStorage.getItem('accessToken');

      // Kiểm tra token trước khi gửi request
      if (token && isTokenExpired(token)) {
        // Nếu token hết hạn, thử refresh trước khi gửi request
        const refreshPromise = refreshToken();
        return refreshPromise.then(() => {
          const newToken = localStorage.getItem('accessToken');
          if (newToken) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + newToken);
          }
        });
      }

      if (token) {
        xhr.setRequestHeader('Authorization', 'Bearer ' + token);
      }
    },
    error: function(jqXHR) {
      const originalRequest = this;
      if ((jqXHR.status === 401 || jqXHR.status === 403) && !originalRequest._retry) {
        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject });
          }).then(token => {
            originalRequest.headers.Authorization = 'Bearer ' + token;
            return $.ajax(originalRequest);
          });
        }
        originalRequest._retry = true;
        return refreshToken().then(() => {
          originalRequest.headers.Authorization = 'Bearer ' + localStorage.getItem('accessToken');
          return $.ajax(originalRequest);
        });
      }
      return Promise.reject(jqXHR);
    }
  });
  // ===================================================================
  // HÀM QUẢN LÝ GIAO DIỆN (HEADER, PROFILE DROPDOWN)
  // ===================================================================
  function handleNavbarAuthButtons() {
    const authBtns = document.querySelectorAll('.js-auth-btn');
    const profileSection = document.querySelector('.js-profile-section');
    const logoutLink = document.querySelector('#logout-link');
    const greetNameSpan = document.querySelector('#user-greet-name');

    if (localStorage.getItem('accessToken')) {
      if (authBtns) authBtns.forEach(btn => btn.style.display = 'none');
      if (profileSection) profileSection.style.display = 'block';
      if (greetNameSpan) greetNameSpan.textContent = localStorage.getItem('username') || 'bạn';
    } else {
      if (authBtns) authBtns.forEach(btn => btn.style.display = 'inline-block');
      if (profileSection) profileSection.style.display = 'none';
    }

    if (logoutLink) {
      logoutLink.onclick = function (e) {
        e.preventDefault();
        logout();
      };
    }
  }
  // ===================================================================
  // HÀM QUẢN LÝ TRẠNG THÁI ĐĂNG NHẬP
  // ===================================================================
  function handleNavbarAuthButtons() {
    const authBtns = document.querySelectorAll('.js-auth-btn');
    const logoutBtn = document.querySelector('.js-logout-btn');

    if (localStorage.getItem('accessToken')) {
      if (authBtns) authBtns.forEach(btn => btn.style.display = 'none');
      if (logoutBtn) logoutBtn.style.display = 'inline-block';
    } else {
      if (authBtns) authBtns.forEach(btn => btn.style.display = 'inline-block');
      if (logoutBtn) logoutBtn.style.display = 'none';
    }

    if (logoutBtn) {
      logoutBtn.onclick = function (e) {
        e.preventDefault();
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('username');
        alert('Bạn đã đăng xuất thành công.');
        window.location.reload();
      };
    }
  }

  // ===================================================================
  // HÀM KIỂM TRA KẾT QUẢ ĐẶT TOUR KHI QUAY VỀ TỪ VNPAY
  // ===================================================================
  function checkBookingStatus() {
    const urlParams = new URLSearchParams(window.location.search);
    const vnp_ResponseCode = urlParams.get('vnp_ResponseCode');

    if (vnp_ResponseCode) {
      if (vnp_ResponseCode === '00') {
        // Sử dụng một modal tùy chỉnh thay vì alert() để đẹp hơn
        const modal = showModal('Đặt tour và thanh toán thành công! Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.');
        const btns = modal.querySelector('#modal-btns');
        btns.innerHTML = `<button class="btn btn-primary" id="close-modal-btn">OK</button>`;
        btns.querySelector('#close-modal-btn').onclick = function () { modal.remove(); };
      } else {
        const modal = showModal('Thanh toán thất bại hoặc đã bị hủy. Vui lòng thử lại hoặc liên hệ với chúng tôi để được hỗ trợ.');
        const btns = modal.querySelector('#modal-btns');
        btns.innerHTML = `<button class="btn btn-danger" id="close-modal-btn">Đã hiểu</button>`;
        btns.querySelector('#close-modal-btn').onclick = function () { modal.remove(); };
      }
      // Xóa các tham số khỏi URL để không hiển thị lại thông báo khi refresh
      const newUrl = window.location.pathname;
      history.replaceState(null, '', newUrl);
    }
  }

  // Hàm tạo modal (có thể tái sử dụng từ card-1.html nếu muốn, hoặc định nghĩa riêng ở đây)
  function showModal(message) {
    const oldModal = document.getElementById('global-custom-modal');
    if (oldModal) oldModal.remove();
    const modal = document.createElement('div');
    modal.id = 'global-custom-modal';
    modal.style.position = 'fixed';
    modal.style.top = '0';
    modal.style.left = '0';
    modal.style.width = '100vw';
    modal.style.height = '100vh';
    modal.style.background = 'rgba(0,0,0,0.35)';
    modal.style.display = 'flex';
    modal.style.alignItems = 'center';
    modal.style.justifyContent = 'center';
    modal.style.zIndex = '10000';
    modal.innerHTML = `
          <div style="background:#fff;padding:40px 32px 32px 32px;border-radius:18px;min-width:340px;max-width:95vw;text-align:center;box-shadow:0 8px 40px rgba(0,0,0,0.18);position:relative;">
            <div style="margin-bottom:18px;">
              <svg width="48" height="48" fill="none" viewBox="0 0 48 48" style="margin-bottom:8px;"><circle cx="24" cy="24" r="24" fill="#f0f4ff"/><path d="M24 14v12" stroke="#2563eb" stroke-width="2.5" stroke-linecap="round"/><circle cx="24" cy="32" r="2" fill="#2563eb"/></svg>
            </div>
            <div style="margin-bottom:24px;font-size:20px;font-weight:600;color:#222;line-height:1.4;">${message}</div>
            <div id="modal-btns" style="display:flex;justify-content:center;gap:16px;"></div>
          </div>
        `;
    document.body.appendChild(modal);
    return modal;
  }


  // ===================================================================
  // LOGIC CHỈ CHẠY KHI Ở TRANG login.html
  // ===================================================================
  if (window.location.pathname.toLowerCase().includes('login.html')) {
    $(document).ready(function () {
      $('#loginForm button[type="submit"]').on('click', function (e) {
        e.preventDefault();
        const $formMessage = $('#form-message');
        const username = $('#username1212').val();
        const password = $('#password1212').val();
        $formMessage.hide().removeClass('error').empty();
        if (!username || !password) {
          $formMessage.text('Vui lòng nhập đầy đủ thông tin.').addClass('error').slideDown();
          return;
        }
        $.ajax({
          url: 'http://localhost:8085/api/auth/login',
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({ username: username, password: password }),
          success: function (response) {
            if (response && response.data && response.data.accessToken) {
              // SỬA LỖI Ở ĐÂY: LƯU TẤT CẢ DỮ LIỆU CẦN THIẾT
              localStorage.setItem('accessToken', response.data.accessToken);
              localStorage.setItem('refreshToken', response.data.refreshToken);
              localStorage.setItem('username', response.data.username);
              localStorage.setItem('userId', response.data.id); // <-- DÒNG QUAN TRỌNG ĐÃ THÊM

              window.location.href = 'index.html';
            } else {
              $formMessage.text('Đăng nhập thất bại: Dữ liệu không hợp lệ.').addClass('error').slideDown();
            }
          },
          error: function (jqXHR) {
            let errorMessage = jqXHR.responseJSON?.message || 'Tài khoản hoặc mật khẩu không chính xác.';
            $formMessage.text(errorMessage).addClass('error').slideDown();
          }
        });
      });
    });
  }

// ===================================================================
  // LOGIC GOOGLE LOGIN
  // ===================================================================
  window.handleGoogleCredentialResponse = function(response) {
    const idToken = response.credential;
    const $formMessage = $('#form-message');
    if (!$formMessage.length) return;
    $formMessage.hide().removeClass('error').text('Đang xác thực với Google...').slideDown();
    $.ajax({
      url: 'http://localhost:8085/api/auth/google-login',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ token: idToken }),
      success: function (backendResponse) {
        if (backendResponse && backendResponse.data && backendResponse.data.accessToken) {
          // SỬA LỖI Ở ĐÂY: LƯU TẤT CẢ DỮ LIỆU CẦN THIẾT
          localStorage.setItem('accessToken', backendResponse.data.accessToken);
          localStorage.setItem('refreshToken', backendResponse.data.refreshToken);
          localStorage.setItem('username', backendResponse.data.username);
          localStorage.setItem('userId', backendResponse.data.id); // <-- DÒNG QUAN TRỌNG ĐÃ THÊM

          window.location.href = 'index.html';
        } else {
          $formMessage.text('Xác thực thất bại từ máy chủ.').addClass('error').slideDown();
        }
      },
      error: function (jqXHR) {
        let errorMessage = jqXHR.responseJSON?.message || 'Không thể đăng nhập bằng Google.';
        $formMessage.text(errorMessage).addClass('error').slideDown();
      }
    });
  }

  // ===================================================================
  // LOGIC CHỈ CHẠY KHI Ở TRANG signup.html
  // ===================================================================
  if (window.location.pathname.toLowerCase().includes('signup.html')) {
    $(document).ready(function() {
      $('#signupForm').on('submit', function(e) {
        e.preventDefault();
        const password = $('input[name="password"]').val();
        const confirmPassword = $('input[name="confirmPassword"]').val();
        if (password !== confirmPassword) {
          alert('Mật khẩu và xác nhận mật khẩu không khớp!');
          return;
        }
        const registerData = {
          fullname: $('input[name="fullName"]').val(),
          username: $('input[name="username"]').val(),
          email: $('input[name="email"]').val(),
          phone: $('input[name="phone"]').val(),
          password: password
        };
        const $submitButton = $(this).find('button[type="submit"]');
        $submitButton.prop('disabled', true).text('Đang xử lý...');
        $.ajax({
          url: 'http://localhost:8085/api/user/register',
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify(registerData),
          success: function(response) {
            if (response.data === true) {
              $('.ud-login-wrapper').html(`<div class="text-center"><i class="lni lni-envelope lni-5x text-primary mb-4"></i><h2 class="mb-3">Đăng ký thành công!</h2><p class="text-muted">Chúng tôi đã gửi một email xác thực đến <strong>${registerData.email}</strong>.<br>Vui lòng kiểm tra hộp thư và nhấn vào link để hoàn tất.</p><p class="mt-4"><a href="login.html" class="ud-main-btn">Quay lại trang đăng nhập</a></p></div>`);
            } else {
              alert(response.message || 'Đăng ký thất bại.');
              $submitButton.prop('disabled', false).text('Đăng ký');
            }
          },
          error: function(jqXHR) {
            alert('Lỗi: ' + (jqXHR.responseJSON?.message || "Đã có lỗi xảy ra."));
            $submitButton.prop('disabled', false).text('Đăng ký');
          }
        });
      });
    });
  }

  // ===================================================================
  // CÁC LOGIC KHÁC CỦA TEMPLATE
  // ===================================================================
  document.addEventListener('DOMContentLoaded', () => {
    handleNavbarAuthButtons();
    checkBookingStatus(); // <-- GỌI HÀM KIỂM TRA KHI TRANG TẢI XONG
  });

  window.onscroll = function () {
    const ud_header = document.querySelector(".ud-header");
    const logo = document.querySelector(".navbar-brand img");
    const backToTop = document.querySelector(".back-to-top");

    if (ud_header) {
      const sticky = ud_header.offsetTop;
      if (window.pageYOffset > sticky) {
        ud_header.classList.add("sticky");
      } else {
        ud_header.classList.remove("sticky");
      }
    }

    if (logo && ud_header) {
      if (ud_header.classList.contains("sticky")) {
        logo.src = "assets/images/logo/logo-6.png";
      } else {
        logo.src = "assets/images/logo/logo-5.png";
      }
    }

    if (backToTop) {
      if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        backToTop.style.display = "flex";
      } else {
        backToTop.style.display = "none";
      }
    }
  };

  const signUpBtn = document.querySelector(".ud-main-btn-sign-up");
  if (signUpBtn) {
    signUpBtn.onclick = function (event) {
      event.preventDefault();
      window.location.href = "login.html";
    };
  }

  const navbarToggler = document.querySelector(".navbar-toggler");
  const navbarCollapse = document.querySelector(".navbar-collapse");
  if (navbarToggler && navbarCollapse) {
    document.querySelectorAll(".ud-menu-scroll").forEach((e) =>
        e.addEventListener("click", () => {
          navbarToggler.classList.remove("active");
          navbarCollapse.classList.remove("show");
        })
    );
    navbarToggler.addEventListener("click", function () {
      navbarToggler.classList.toggle("active");
      navbarCollapse.classList.toggle("show");
    });
  }

  const submenuButton = document.querySelectorAll(".nav-item-has-children");
  submenuButton.forEach((elem) => {
    const link = elem.querySelector("a");
    const submenu = elem.querySelector(".ud-submenu");
    if (link && submenu) {
      link.addEventListener("click", () => {
        submenu.classList.toggle("show");
      });
    }
  });

  if (typeof WOW !== "undefined") {
    new WOW().init();
  }

  function scrollTo(element, to = 0, duration = 500) {
    const start = element.scrollTop;
    const change = to - start;
    const increment = 20;
    let currentTime = 0;
    const animateScroll = () => {
      currentTime += increment;
      const val = Math.easeInOutQuad(currentTime, start, change, duration);
      element.scrollTop = val;
      if (currentTime < duration) {
        setTimeout(animateScroll, increment);
      }
    };
    animateScroll();
  }

  Math.easeInOutQuad = function (t, b, c, d) {
    t /= d / 2;
    if (t < 1) return (c / 2) * t * t + b;
    t--;
    return (-c / 2) * (t * (t - 2) - 1) + b;
  };

  const backToTop = document.querySelector(".back-to-top");
  if (backToTop) {
    backToTop.onclick = () => {
      scrollTo(document.documentElement);
    };
  }
})();