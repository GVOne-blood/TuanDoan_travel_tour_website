package com.javaadvancedg9.JavaAdvancedG9.config;

import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN_ALLOW_POPUPS;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final String[] WHITELIST = {"/api/auth/**", "/api/tintuc/**", "/api/tour/**", "/tours/**", "/api/user/register", "api/booking/vn-pay-callback/**", "/api/user/send-mail"};
    private final UserService userService;
    private final PreFilter preFilter;

    @Bean
    public String appName() {
        return "Java Advanced G9";
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // THAY ĐỔI LỚN: XÓA BỎ WebMvcConfigurer VÀ THAY BẰNG CorsConfigurationSource
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Đây là danh sách các origin được phép.
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:63342", "http://localhost:8085"));
        // Các phương thức được phép
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Các header được phép
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Cho phép gửi cookie và thông tin xác thực
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình này cho tất cả các đường dẫn
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain configure(@Nonnull HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                // Yêu cầu Spring Security sử dụng bean CorsConfigurationSource mà chúng ta đã định nghĩa ở trên
                .cors(Customizer.withDefaults())
                // THÊM CẤU HÌNH HEADER CHO PHÉP TƯƠNG TÁC VỚI POP-UP
                .headers(headers ->
                        headers.crossOriginOpenerPolicy(policy ->
                                policy
                                        .policy(SAME_ORIGIN_ALLOW_POPUPS) // Cho phép pop-up từ cùng origin
                        )
                )

                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        // Quy tắc này VẪN RẤT QUAN TRỌNG để cho phép preflight request đi qua
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(provider()).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userService.userDetailsService());
        return provider;
    }

    @Bean
    public WebSecurityCustomizer customize(){
        return (web) -> web.ignoring().requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authen) throws Exception{
        return authen.getAuthenticationManager();
    }
}