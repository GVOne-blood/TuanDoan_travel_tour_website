package com.javaadvancedg9.JavaAdvancedG9.controller.api;

import com.javaadvancedg9.JavaAdvancedG9.dto.LoginDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.SessionUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseData login(@RequestBody LoginDTO info) {
        if(this.userService.adminLogin(info)) {
            return new ResponseData("Thành công", SessionUtilities.getAdmin());
        }
        return new ResponseData("Thông tin đăng nhập không hợp lệ", null);
    }

    @GetMapping("/logout")
    public ResponseData logout() {
        this.userService.adminLogout();
        return new ResponseData("Đăng xuất thành công",null);
    }

}
