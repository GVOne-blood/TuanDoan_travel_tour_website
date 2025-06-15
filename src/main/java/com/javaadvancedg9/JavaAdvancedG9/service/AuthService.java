package com.javaadvancedg9.JavaAdvancedG9.service;

import com.javaadvancedg9.JavaAdvancedG9.dto.LoginDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponse adminAuthenticate(LoginDTO request);
    
    TokenResponse authenticate(LoginDTO request);

    TokenResponse googleAuthenticate(String token) throws Exception;



    TokenResponse refresh(HttpServletRequest request);
}
