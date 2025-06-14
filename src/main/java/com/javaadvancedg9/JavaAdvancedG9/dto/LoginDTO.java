package com.javaadvancedg9.JavaAdvancedG9.dto;

import com.javaadvancedg9.JavaAdvancedG9.enumtype.Platform;
import com.javaadvancedg9.JavaAdvancedG9.utilities.EnumPattern;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    @EnumPattern(name = "platform", regexp = "WEB|IOS|ANDROID|DESKTOP|OTHER", message = "Nền tảng phải là một trong các giá trị sau: WEB, IOS, ANDROID, DESKTOP, OTHER")
    private Platform platform;
}

