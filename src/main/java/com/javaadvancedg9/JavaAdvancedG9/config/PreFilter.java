package com.javaadvancedg9.JavaAdvancedG9.config;

import com.javaadvancedg9.JavaAdvancedG9.enumtype.TokenType;
import com.javaadvancedg9.JavaAdvancedG9.service.JwtService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("------------------ PreFilter -----------------");
        String authHeader = request.getHeader(AUTHORIZATION); // lấy header Authorization từ request
        log.info("Authentication header: {}", authHeader ); // lấy header Authentication từ request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // nếu không có header thì gọi filter tiếp theo trong chuỗi filter
            return;
        }
        final String token = authHeader.substring("Bearer ".length()); // lấy token từ header
        //log.info("Token: {}", token);
        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN); // lấy username từ token

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null){ // check username không rỗng và chưa có quthen trong SecurityContext
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username); // lấy thông tin người dùng từ UserService
            if (jwtService.isValid(token, userDetails, TokenType.ACCESS_TOKEN)){ // kiểm tra token hết hạn chưa
                SecurityContext context = SecurityContextHolder.createEmptyContext(); // tạo một SecurityContext rỗng
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // tạo một đối tượng UsernamePasswordAuthenticationToken với thông tin người dùng
                authentication.setDetails(userDetails); // thiết lập chi tiết cho đối tượng authentication
                context.setAuthentication(authentication); // thiết lập authentication cho SecurityContext
                SecurityContextHolder.setContext(context); // đặt SecurityContext vào SecurityContextHolder
            }
        }
        filterChain.doFilter(request, response); // gọi filter tiếp theo trong chuỗi filter
    }

}
