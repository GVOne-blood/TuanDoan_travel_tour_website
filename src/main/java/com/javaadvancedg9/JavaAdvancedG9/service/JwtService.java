package com.javaadvancedg9.JavaAdvancedG9.service;

import com.javaadvancedg9.JavaAdvancedG9.enumtype.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    public String generateToken(UserDetails user);

    public String generateRefreshToken(UserDetails user);

    public String generateResetToken(UserDetails user);

    public String extractUsername(String token, TokenType type);

    public boolean isValid(String token, UserDetails user, TokenType type);


}
