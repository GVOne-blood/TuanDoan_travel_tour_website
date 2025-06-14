package com.javaadvancedg9.JavaAdvancedG9.dto;


import com.javaadvancedg9.JavaAdvancedG9.enumtype.Gender;
import com.javaadvancedg9.JavaAdvancedG9.utilities.EnumPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    private String username;

    private String fullname;

    private String password;

//    @EnumPattern(name = "gender", regexp = "MALE|FEMALE|OTHER")
//    private Gender gender;

    private String phone;

    private String email;

    //private String address;

   // private Integer role;


}
