    package com.javaadvancedg9.JavaAdvancedG9.controller.api;

    import com.javaadvancedg9.JavaAdvancedG9.dto.GoogleTokenDTO;
    import com.javaadvancedg9.JavaAdvancedG9.dto.LoginDTO;
    import com.javaadvancedg9.JavaAdvancedG9.dto.RegisterDTO;
    import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
    import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseError;
    import com.javaadvancedg9.JavaAdvancedG9.dto.response.TokenResponse;
    import com.javaadvancedg9.JavaAdvancedG9.entity.User;
    import com.javaadvancedg9.JavaAdvancedG9.service.AuthService;
    import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.validation.constraints.Min;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.*;

    import java.io.IOException;

    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final AuthService authService;
        private final UserService userService;

        // Trong AuthController.java

        @GetMapping("confirm/{userId}")
        public void confirmMail(@Min(1) @PathVariable Long userId, @RequestParam(required = true) String token, HttpServletResponse response) throws IOException {
            String redirectUrl = "http://localhost:63342/TuanDoan_travel_tour_website/src/main/resources/login.html"; // Thay bằng đường dẫn đúng
            try {
                userService.confirmUser(userId, token);
                // Nếu thành công, chuyển hướng với tham số success
                redirectUrl += "?confirm_success=true";
            } catch (Exception e) {
                // Nếu thất bại, chuyển hướng với tham số error
                redirectUrl += "?confirm_error=true&message=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8");
            }
            // Luôn thực hiện chuyển hướng
            response.sendRedirect(redirectUrl);
        }


        @PostMapping("/login")
        public ResponseData<TokenResponse> login (@RequestBody LoginDTO request){
            TokenResponse tokenResponse = null;
            try {
                 tokenResponse = this.authService.authenticate(request);
            }
            catch(Exception e){
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            }
            return new ResponseData<>(HttpStatus.OK.value(), "Đăng nhập thành công", tokenResponse);
        }
        @PostMapping("/google-login")
        public ResponseData<TokenResponse> googleLogin(@RequestBody  GoogleTokenDTO google){
           TokenResponse response;
            try{
                response = authService.googleAuthenticate(google.getToken());
            }catch (Exception e) {
                return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            }
            return new ResponseData<>(HttpStatus.OK.value(), "Đăng nhập bằng Google thành công", response);
        }

//        @PostMapping("/register")
//        public ResponseData<?> register(@RequestBody RegisterDTO request) {
//            User user = new User();
//            try {
//
//                user.setUsername(request.getUsername());
//                user.setPassword(request.getPassword());
//                user.setEmail(request.getEmail());
//                user.setPhone(request.getPhone());
//                user.setFullname(request.getFullname());
//                userService.saveUser(user);
//            } catch (Exception e) {
//                return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//            }
//            return new ResponseData<>(HttpStatus.CREATED.value(), "Đăng ký thành công", user);
//        }

        @PostMapping("/refresh")
        public ResponseData<TokenResponse> refreshToken(HttpServletRequest request){
        return new ResponseData<>(HttpStatus.OK.value(), "Làm mới token thành công", this.authService.refresh(request));
        }
        @GetMapping("/logout")
        public ResponseData<?> logout() {
            this.userService.adminLogout();
            return new ResponseData<>("Đăng xuất thành công", null);
        }

        @GetMapping("/encode")
        public String encodeAllPasswords() {
           // this.userService.encodeAllPassword();
            return "Đã mã hóa tất cả mật khẩu";
        }
    }