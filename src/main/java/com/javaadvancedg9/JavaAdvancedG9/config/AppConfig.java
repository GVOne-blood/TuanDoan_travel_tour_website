package com.javaadvancedg9.JavaAdvancedG9.config;

import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final String[] WHITELIST = {"api/auth/**", "/user/**", "/admin/**", "/api/**"};
    private final PreFilter preFilter;
    private final UserService userService;

    @Bean
    public String appName() {
        return "Java Advanced G9";
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
        @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry){
                registry.addMapping("**") // Cho phép tất cả các đường dẫn
                        .allowedOrigins("http://localhost:8085")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các phương thức HTTP
                        .allowedHeaders("*") // Cho phép tất cả các header
                        .allowCredentials(false) // Cho phép cookie
                        .maxAge(3600); // Thời gian cache CORS
            }
        };
    }

    @Bean
    public SecurityFilterChain configure(@Nonnull HttpSecurity http) throws Exception{
http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequest -> authorizeRequest.requestMatchers(WHITELIST).permitAll().anyRequest().authenticated())
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(provider()).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(getPasswordEncoder());
                provider.setUserDetailsService(userService.getUserDetailsService());
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
