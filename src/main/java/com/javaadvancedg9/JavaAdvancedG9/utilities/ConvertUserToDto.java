package com.javaadvancedg9.JavaAdvancedG9.utilities;

import com.javaadvancedg9.JavaAdvancedG9.dto.UserDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.User;

public class ConvertUserToDto {

    public static UserDTO convertUsertoDto(User user) {
        return new UserDTO(user.getId(),user.getUsername(), user.getFullname(), user.getSdt(), user.getGender(), user.getEmail(),
                user.getAddress(), user.getRole());
    }

}
