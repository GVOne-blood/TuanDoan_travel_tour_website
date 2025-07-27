package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.javaadvancedg9.JavaAdvancedG9.dto.LoginDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.TokenResponse;
import com.javaadvancedg9.JavaAdvancedG9.entity.Roles;
import com.javaadvancedg9.JavaAdvancedG9.entity.Token;
import com.javaadvancedg9.JavaAdvancedG9.entity.User;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Role;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.TokenType;
import com.javaadvancedg9.JavaAdvancedG9.repository.TokenRepository;
import com.javaadvancedg9.JavaAdvancedG9.repository.UserRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.AuthService;
import com.javaadvancedg9.JavaAdvancedG9.service.JwtService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.PasswordEncoderUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.google_client_id}")
    private String googleClientId;


    public TokenResponse adminAuthenticate(LoginDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }

        User user = userService.findUserByUsername(request.getUsername());

        // Logic kiểm tra quyền ADMIN bằng Enum
        if (user.getRoleName() == null || user.getRoleName().getName() != Role.ADMIN) {
            throw new AccessDeniedException("Bạn không có quyền truy cập vào hệ thống quản trị.");
        }

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return TokenResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse authenticate(LoginDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String access_token = jwtService.generateToken(user);
        String refresh_token = jwtService.generateRefreshToken(user);

        Token userToken = tokenRepository.findByUsername(user.getUsername());
//        if (userToken == null) {
//            userToken = new Token();
//            userToken.setUsername(user.getUsername());
//        }
        userToken.setACCESS_TOKEN(access_token);
        userToken.setREFRESH_TOKEN(refresh_token);
        tokenRepository.save(userToken);


        return TokenResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(access_token)
                .refreshToken(refresh_token)
                .build();
    }

    @Override
    public TokenResponse googleAuthenticate(String token) throws Exception {
        // Validate the Google ID token
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        GoogleIdToken googleToken = verifier.verify(token);
        if (googleToken == null) {
            throw new UsernameNotFoundException("Invalid Google ID token");
        }
        GoogleIdToken.Payload payload = googleToken.getPayload();
        String email = payload.getEmail();
        String name = payload.get("name").toString();
        // Check if the user already exists in the database, if already exists, return the token, else create a new user
        User user = userRepository.findByUsername(email).orElseGet(
                () -> {
                    User newUser = new User();
                    newUser.setUsername(name);
                    newUser.setEmail(email);
                    newUser.setPassword(PasswordEncoderUtil.encode("google_auth_" + email)); // Use a unique password for Google auth
                    newUser.setRole(Role.USER);

                    newUser.setFullname(name);
                    return userRepository.save(newUser);
                });
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Token userToken = tokenRepository.findByUsername(email);
        if (userToken == null) {
            userToken = new Token();
            userToken.setUsername(email);
        }
        userToken.setACCESS_TOKEN(accessToken);
        userToken.setREFRESH_TOKEN(refreshToken);
        tokenRepository.save(userToken);

        return TokenResponse.builder()
                .username(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        String refreshToken = request.getHeader("r_token");
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new UsernameNotFoundException("Refresh token is not provided");
        }
        String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isValid(refreshToken, user, TokenType.REFRESH_TOKEN)) {

        throw new UsernameNotFoundException("Refresh token is expired or invalid");
        }
        String newAccessToken = jwtService.generateToken(user);
        Token userToken = tokenRepository.findByUsername(username);
        userToken.setACCESS_TOKEN(newAccessToken);
        tokenRepository.save(userToken);


        return TokenResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
