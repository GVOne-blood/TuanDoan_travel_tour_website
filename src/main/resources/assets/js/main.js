// (function () {
//   "use strict";



//   // ======= Sticky
//   window.onscroll = function () {
//     const ud_header = document.querySelector(".ud-header");
//     const sticky = ud_header.offsetTop;
//     const logo = document.querySelector(".navbar-brand img");

//     if (window.pageYOffset > sticky) {
//       ud_header.classList.add("sticky");
//     } else {
//       ud_header.classList.remove("sticky");
//     }

//     // === logo change
//     if (ud_header.classList.contains("sticky")) {
//       logo.src = "assets/images/logo/logo-6.png";
//     } else {
//       logo.src = "assets/images/logo/logo-5.png";
//     }

//     // show or hide the back-top-top button
//     const backToTop = document.querySelector(".back-to-top");
//     if (
//       document.body.scrollTop > 50 ||
//       document.documentElement.scrollTop > 50
//     ) {
//       backToTop.style.display = "flex";
//     } else {
//       backToTop.style.display = "none";
//     }
//   };

//   // ===== Redirect to login page
// document.querySelector(".ud-main-btn-sign-up").onclick = function (event) {
//   event.preventDefault(); // Ngăn việc gửi form nếu nút nằm trong form
//   window.location.href = "login.html"; // Đường dẫn đến trang login
// };
//   //===== close navbar-collapse when a  clicked
//   let navbarToggler = document.querySelector(".navbar-toggler");
//   const navbarCollapse = document.querySelector(".navbar-collapse");

//   document.querySelectorAll(".ud-menu-scroll").forEach((e) =>
//     e.addEventListener("click", () => {
//       navbarToggler.classList.remove("active");
//       navbarCollapse.classList.remove("show");
//     })
//   );
//   navbarToggler.addEventListener("click", function () {
//     navbarToggler.classList.toggle("active");
//     navbarCollapse.classList.toggle("show");
//   });

//   // ===== submenu
//   const submenuButton = document.querySelectorAll(".nav-item-has-children");
//   submenuButton.forEach((elem) => {
//     elem.querySelector("a").addEventListener("click", () => {
//       elem.querySelector(".ud-submenu").classList.toggle("show");
//     });
//   });

//   // ===== wow js
//   new WOW().init();

//   // ====== scroll top js
//   function scrollTo(element, to = 0, duration = 500) {
//     const start = element.scrollTop;
//     const change = to - start;
//     const increment = 20;
//     let currentTime = 0;

//     const animateScroll = () => {
//       currentTime += increment;

//       const val = Math.easeInOutQuad(currentTime, start, change, duration);

//       element.scrollTop = val;

//       if (currentTime < duration) {
//         setTimeout(animateScroll, increment);
//       }
//     };

//     animateScroll();
//   }

//   Math.easeInOutQuad = function (t, b, c, d) {
//     t /= d / 2;
//     if (t < 1) return (c / 2) * t * t + b;
//     t--;
//     return (-c / 2) * (t * (t - 2) - 1) + b;
//   };

//   document.querySelector(".back-to-top").onclick = () => {
//     scrollTo(document.documentElement);
//   };

//   console.log("main.js đã được tải!");

//   // Hàm xử lý sự kiện đăng nhập
//   function handleLoginForm() {
//     const loginForm = document.getElementById("loginForm");
//     if (!loginForm) {
//       console.error("Không tìm thấy phần tử #loginForm!");
//       return;
//     }

//     console.log("Đã gắn sự kiện submit cho #loginForm");

//     loginForm.addEventListener("submit", function (event) {
//       event.preventDefault(); // Ngăn hành vi mặc định của biểu mẫu

//       const usernameInput = document.getElementById("username1212");
//       const passwordInput = document.getElementById("password1212");

//       if (!usernameInput || !passwordInput) {
//         console.error("Không tìm thấy #username1212 hoặc #password1212!");
//         alert("Lỗi: Không tìm thấy trường nhập liệu!");
//         return;
//       }

//       const username = usernameInput.value.trim();
//       const password = passwordInput.value.trim();

//       console.log("Username:", username);
//       console.log("Password:", password);

//       if (username === "doan12" && password === "doan12") {
//         console.log("Đăng nhập thành công!");
//         alert("Đăng nhập thành công!");
//         localStorage.setItem("isLoggedIn", "true");
//         window.location.href = "./index.html";
//       } else {
//         console.log("Đăng nhập thất bại!");
//         alert("Tên đăng nhập hoặc mật khẩu không đúng!");
//       }
//     });
//   }

//   // Kiểm tra trang hiện tại
//   const currentPage = window.location.pathname.toLowerCase();
//   console.log("Current page pathname:", currentPage);

//   if (currentPage.includes("login.html")) {
//     handleLoginForm();
//   }
// })();

(function () {
  "use strict";

  console.log("main.js đã được tải!");

  // ===================================================================
  // HÀM QUẢN LÝ TRẠNG THÁI ĐĂNG NHẬP (ĐÃ SỬA)
  // - Kiểm tra 'accessToken' thay vì 'isLoggedIn'.
  // - Xóa đúng các key khi đăng xuất.
  // ===================================================================
  function handleNavbarAuthButtons() {
    const authBtns = document.querySelectorAll('.js-auth-btn');
    const logoutBtn = document.querySelector('.js-logout-btn');

    // Kiểm tra xem có accessToken không
    if (localStorage.getItem('accessToken')) {
      // Đã đăng nhập: Ẩn nút Đăng nhập/Đăng ký, Hiện nút Đăng xuất
      if (authBtns) authBtns.forEach(btn => btn.style.display = 'none');
      if (logoutBtn) logoutBtn.style.display = 'inline-block';
    } else {
      // Chưa đăng nhập: Hiện nút Đăng nhập/Đăng ký, Ẩn nút Đăng xuất
      if (authBtns) authBtns.forEach(btn => btn.style.display = 'inline-block');
      if (logoutBtn) logoutBtn.style.display = 'none';
    }

    // Gắn sự kiện cho nút Đăng xuất
    if (logoutBtn) {
      logoutBtn.onclick = function (e) {
        e.preventDefault();
        // Xóa tất cả các key liên quan đến session
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('username');
        alert('Bạn đã đăng xuất thành công.');
        window.location.reload();
      };
    }
  }

  // ===================================================================
  // LOGIC CHỈ CHẠY KHI Ở TRANG login.html
  // ===================================================================
  if (window.location.pathname.toLowerCase().includes('login.html')) {
    // Đảm bảo code jQuery chỉ chạy khi DOM đã sẵn sàng
    $(document).ready(function () {
      // --- Xử lý hiển thị thông báo xác thực email ---
      const urlParams = new URLSearchParams(window.location.search);
      const confirmSuccess = urlParams.get('confirm_success');
      const confirmError = urlParams.get('confirm_error');
      const $confirmationMessage = $('#confirmation-message');

      if (confirmSuccess) {
        $confirmationMessage
            .text('Xác thực tài khoản thành công! Vui lòng đăng nhập.')
            .addClass('alert alert-success') // Dùng class của Bootstrap
            .show();
      } else if (confirmError) {
        const message = urlParams.get('message') || 'Xác thực thất bại. Vui lòng thử lại.';
        $confirmationMessage
            .text('Lỗi: ' + message)
            .addClass('alert alert-danger')
            .show();
      }
      // --- Xử lý đăng nhập bằng Username/Password ---
      const loginForm = $('#loginForm');
      if (loginForm.length) {
        // Gắn sự kiện vào nút submit thay vì cả form để tránh xung đột
        loginForm.find('button[type="submit"]').on('click', function (e) {
          e.preventDefault();

          const $formMessage = $('#form-message');
          const username = $('#username1212').val();
          const password = $('#password1212').val();

          $formMessage.hide().removeClass('error').empty();

          if (!username || !password) {
            $formMessage.text('Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.').addClass('error').slideDown();
            return;
          }

          $.ajax({
            url: 'http://localhost:8085/api/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username: username, password: password }),
            success: function (response) {
              if (response && response.data && response.data.accessToken) {
                localStorage.setItem('accessToken', response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);
                localStorage.setItem('username', response.data.username);
                window.location.href = 'index.html';
              } else {
                $formMessage.text('Đăng nhập thất bại: Dữ liệu trả về không hợp lệ.').addClass('error').slideDown();
              }
            },
            error: function (jqXHR) {
              let errorMessage = 'Tài khoản hoặc mật khẩu không chính xác.';
              if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
                errorMessage = jqXHR.responseJSON.message;
              }
              $formMessage.text(errorMessage).addClass('error').slideDown();
            }
          });
        });
        console.log("Đã gắn sự kiện click bằng AJAX cho nút submit của #loginForm");
      }
    });
  }

  // ===================================================================
  // LOGIC GOOGLE LOGIN (Tập trung vào đây)
  // Hàm này phải được định nghĩa ở phạm vi toàn cục (window) để Google có thể gọi
  // ===================================================================
  window.handleGoogleCredentialResponse = function(response) {
    const idToken = response.credential;
    const $formMessage = $('#form-message');

    if (!$formMessage.length) {
      console.error("Không tìm thấy #form-message trên trang.");
      return;
    }

    $formMessage.hide().removeClass('error').text('Đang xác thực với Google...').slideDown();

    $.ajax({
      url: 'http://localhost:8085/api/auth/google-login',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ token: idToken }),
      success: function (backendResponse) {
        if (backendResponse && backendResponse.data && backendResponse.data.accessToken) {
          localStorage.setItem('accessToken', backendResponse.data.accessToken);
          localStorage.setItem('refreshToken', backendResponse.data.refreshToken);
          localStorage.setItem('username', backendResponse.data.username);
          window.location.href = 'index.html';
        } else {
          $formMessage.text('Xác thực thất bại từ phía máy chủ.').addClass('error').slideDown();
        }
      },
      error: function (jqXHR) {
        let errorMessage = 'Không thể đăng nhập bằng Google.';
        if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
          errorMessage = jqXHR.responseJSON.message;
        }
        $formMessage.text(errorMessage).addClass('error').slideDown();
      }
    });
  }
// ===================================================================
  // LOGIC CHỈ CHẠY KHI Ở TRANG signup.html (MỚI)
  // ===================================================================
  if (window.location.pathname.toLowerCase().includes('signup.html')) {
    $(document).ready(function() {
      const $signupForm = $('#signupForm');
      const $formWrapper = $('.ud-login-wrapper'); // Div chứa toàn bộ form

      $signupForm.on('submit', function(e) {
        e.preventDefault();

        // --- Thu thập dữ liệu từ form ---
        const fullName = $('input[name="fullName"]').val();
        const username = $('input[name="username"]').val();
        const email = $('input[name="email"]').val();
        const phone = $('input[name="phone"]').val();
        const password = $('input[name="password"]').val();
        const confirmPassword = $('input[name="confirmPassword"]').val();

        // --- Validate cơ bản ---
        if (password !== confirmPassword) {
          alert('Mật khẩu và xác nhận mật khẩu không khớp!');
          return;
        }
        // (Thêm các validate khác nếu cần)

        const registerData = {
          fullname: fullName,
          username: username,
          email: email,
          phone: phone,
          password: password
        };

        // Vô hiệu hóa nút và hiển thị loading
        const $submitButton = $(this).find('button[type="submit"]');
        $submitButton.prop('disabled', true).text('Đang xử lý...');

        // --- Gọi API đăng ký ---
        $.ajax({
          url: 'http://localhost:8085/api/user/register', // API trong UserController
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify(registerData),
          success: function(response) {
            if (response.data === true) {
              // Đăng ký thành công, hiển thị giao diện chờ
              $formWrapper.html(`
                            <div class="text-center">
                                <i class="lni lni-envelope lni-5x text-primary mb-4"></i>
                                <h2 class="mb-3">Đăng ký thành công!</h2>
                                <p class="text-muted">
                                    Chúng tôi đã gửi một email xác thực đến <strong>${email}</strong>.
                                    <br>
                                    Vui lòng kiểm tra hộp thư của bạn và nhấn vào đường link để hoàn tất đăng ký.
                                </p>
                                <p class="mt-4">
                                    <a href="login.html" class="ud-main-btn">Quay lại trang đăng nhập</a>
                                </p>
                            </div>
                        `);
            } else {
              // Xử lý lỗi từ backend (ví dụ: username đã tồn tại)
              alert(response.message || 'Đăng ký thất bại. Vui lòng thử lại.');
              $submitButton.prop('disabled', false).text('Đăng ký');
            }
          },
          error: function(jqXHR) {
            const errorMsg = jqXHR.responseJSON ? jqXHR.responseJSON.message : "Đã có lỗi không xác định xảy ra.";
            alert('Lỗi: ' + errorMsg);
            $submitButton.prop('disabled', false).text('Đăng ký');
          }
        });
      });
    });
  }
  // ===================================================================
  // CÁC LOGIC KHÁC CỦA TEMPLATE (Giữ nguyên)
  // ===================================================================

  // Gọi hàm kiểm tra trạng thái đăng nhập trên mọi trang
  document.addEventListener('DOMContentLoaded', handleNavbarAuthButtons);

  // Sticky Header, Scroll Top, v.v...
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

  // Redirect to login page
  const signUpBtn = document.querySelector(".ud-main-btn-sign-up");
  if (signUpBtn) {
    signUpBtn.onclick = function (event) {
      event.preventDefault();
      window.location.href = "login.html";
    };
  }

  // Close navbar-collapse when a clicked
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

  // Submenu
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

  // WOW.js
  if (typeof WOW !== "undefined") {
    new WOW().init();
  } else {
    console.warn("WOW.js không được tải!");
  }

  // Scroll top
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
//
//   // ===== TOUR SEARCH, FILTER, SORT, PAGINATION LOGIC FOR pricing.html =====
//   if (window.location.pathname.includes('pricing.html')) {
//     // Ẩn nút đăng nhập/đăng ký nếu đã đăng nhập
//     if (localStorage.getItem('isLoggedIn') === 'true') {
//       document.querySelectorAll('.js-auth-btn').forEach(function (btn) {
//         btn.style.display = 'none';
//       });
//     }
//
//     const TOURS_PER_PAGE = 9;
//     let currentPage = 1;
//     let tours = [];
//     let filteredTours = [];
//     const searchForm = document.querySelector('.ud-section-title-pricing-1 form');
//
//     function filterTours() {
//       const dateInput = searchForm.querySelector('input[placeholder="Ngày khởi hành"]').value.trim().toLowerCase();
//       const titleInput = searchForm.querySelector('input[placeholder="Điểm đến"]').value.trim().toLowerCase();
//       const priceInput = searchForm.querySelector('.search-form-select').value;
//       filteredTours = tours.filter(tour => {
//         let match = true;
//         if (dateInput) {
//           match = match && (tour.date && tour.date.toLowerCase().includes(dateInput));
//         }
//         if (titleInput) {
//           // Only search in title, partial match
//           const title = (tour.title || '').toLowerCase();
//           match = match && title.includes(titleInput);
//         }
//         if (priceInput) {
//           const price = parseInt(tour.price.replace(/[^\d]/g, ''));
//           if (priceInput === 'low') match = match && price < 500000;
//           else if (priceInput === 'medium') match = match && price >= 500000 && price <= 1000000;
//           else if (priceInput === 'high') match = match && price > 1000000;
//         }
//         return match;
//       });
//     }
//
//     function sortTours(arr) {
//       const sortValue = document.querySelector('.sort-select').value;
//       let sorted = [...arr];
//       if (sortValue === 'recommended') {
//         sorted.sort((a, b) => (b.rating || 0) - (a.rating || 0));
//       } else if (sortValue === 'price-asc') {
//         sorted.sort((a, b) => {
//           const pa = parseInt((a.price||'').replace(/[^\d]/g, '')) || 0;
//           const pb = parseInt((b.price||'').replace(/[^\d]/g, '')) || 0;
//           return pa - pb;
//         });
//       } else if (sortValue === 'price-desc') {
//         sorted.sort((a, b) => {
//           const pa = parseInt((a.price||'').replace(/[^\d]/g, '')) || 0;
//           const pb = parseInt((b.price||'').replace(/[^\d]/g, '')) || 0;
//           return pb - pa;
//         });
//       }
//       return sorted;
//     }
//
//     function renderTours(page, arr) {
//       const tourList = document.getElementById('tour-list');
//       const data = arr || tours;
//       const sortedData = sortTours(data);
//       const start = (page - 1) * TOURS_PER_PAGE;
//       const end = start + TOURS_PER_PAGE;
//       const pageTours = sortedData.slice(start, end);
//       tourList.innerHTML = pageTours.map(tour => `
//         <div class="col">
//           <div class="card h-100 shadow-sm rounded tour-card">
//             <img src="${tour.main_image || 'assets/images/card/card-1.jpg'}" class="card-img-top mb-2" alt="${tour.title}">
//             <div class="d-flex gap-2 mb-2">
//               <img src="${tour.sub_image_1 || ''}" class="img-card-2" style="width:48px;height:48px;object-fit:cover;border-radius:6px;" alt="sub1">
//               <img src="${tour.sub_image_2 || ''}" class="img-card-3" style="width:48px;height:48px;object-fit:cover;border-radius:6px;" alt="sub2">
//             </div>
//             <div class="card-body">
//               <h5 class="card-title">${tour.title}</h5>
//               <p class="card-text text-muted">${tour.description}</p>
//               <p class="card-price"><strong>${tour.price}</strong></p>
//               <p class="card-rating">Đánh giá: ${tour.rating} (${tour.reviews} lượt)</p>
//               <a href="${tour.details_link}" class="btn btn-outline-primary btn-sm" target="_blank">Xem thêm</a>
//             </div>
//           </div>
//         </div>
//       `).join('');
//     }
//
//     function renderPagination(total, page) {
//       const totalPages = Math.ceil(total / TOURS_PER_PAGE);
//       const pagination = document.querySelector('.pagination');
//       if (!pagination) return;
//       let html = '';
//       html += `<li class="page-item${page === 1 ? ' disabled' : ''}"><a class="page-link" href="#" data-page="prev" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>`;
//       for (let i = 1; i <= totalPages; i++) {
//         html += `<li class="page-item${i === page ? ' active' : ''}"><a class="page-link" href="#" data-page="${i}">${i}</a></li>`;
//       }
//       html += `<li class="page-item${page === totalPages ? ' disabled' : ''}"><a class="page-link" href="#" data-page="next" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>`;
//       pagination.innerHTML = html;
//     }
//
//     function onPageClick(e) {
//       const pageLink = e.target.closest('.page-link');
//       if (!pageLink) return;
//       e.preventDefault();
//       const data = (filteredTours.length > 0 || searchForm.querySelector('input[placeholder="Ngày khởi hành"]').value || searchForm.querySelector('input[placeholder="Điểm đến"]').value || searchForm.querySelector('select').value) ? filteredTours : tours;
//       const totalPages = Math.ceil(data.length / TOURS_PER_PAGE);
//       let page = currentPage;
//       const pageAttr = pageLink.getAttribute('data-page');
//       if (pageAttr === 'prev' && page > 1) page--;
//       else if (pageAttr === 'next' && page < totalPages) page++;
//       else if (!isNaN(Number(pageAttr))) page = Number(pageAttr);
//       if (page !== currentPage) {
//         currentPage = page;
//         renderTours(currentPage, data);
//         renderPagination(data.length, currentPage);
//       }
//     }
//
//     // Gán sự kiện click cho phân trang
//     const pagination = document.querySelector('.pagination');
//     if (pagination) {
//       pagination.addEventListener('click', onPageClick);
//     } else {
//       console.error("Không tìm thấy phần tử phân trang! Kiểm tra HTML trong pricing.html");
//     }
//
//     // Gán sự kiện submit cho form tìm kiếm
//     if (searchForm) {
//       searchForm.addEventListener('submit', function(e) {
//         e.preventDefault();
//         filterTours();
//         renderTours(1, filteredTours);
//         renderPagination(filteredTours.length, 1);
//       });
//     } else {
//       console.error("Không tìm thấy phần tử form trong .ud-section-title-pricing-1! Kiểm tra HTML trong pricing.html");
//     }
//
//     // Lấy dữ liệu tour mẫu (chỉ chạy một lần)
//     if (tours.length === 0) {
//       fetch('assets/data/tours.json')
//         .then(response => response.json())
//         .then(data => {
//           tours = data;
//           renderTours(currentPage, tours);
//           renderPagination(tours.length, currentPage);
//         })
//         .catch(error => console.error('Lỗi khi tải dữ liệu tour:', error));
//     }
//   }
})();