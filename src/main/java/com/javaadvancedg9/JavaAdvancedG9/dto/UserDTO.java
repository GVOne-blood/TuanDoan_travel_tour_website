package com.javaadvancedg9.JavaAdvancedG9.dto;

import com.javaadvancedg9.JavaAdvancedG9.enumtype.Gender;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Role;
import com.javaadvancedg9.JavaAdvancedG9.utilities.EnumPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    @NotBlank(message = "Username is required")
    private String username;
    @NotNull(message = "Fullname is required")
    private String fullname;

    private String phone;

    @EnumPattern(name = "gender", regexp = "MALE|FEMALE|OTHER")
    private Gender gender;
    @NotBlank(message = "Email is required")
    private String email;

    private String address;

    @EnumPattern(name = "role", regexp = "USER|ADMIN|MANAGER", message = "Roles must be USER, ADMIN or MANAGER")
    private Role role;
}
