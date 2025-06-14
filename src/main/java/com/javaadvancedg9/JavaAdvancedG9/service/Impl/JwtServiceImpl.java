package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import com.javaadvancedg9.JavaAdvancedG9.enumtype.TokenType;
import com.javaadvancedg9.JavaAdvancedG9.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expiryTime}")
    private String expiryTime;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiryDay}")
    private String expiryDay;
    @Value("${jwt.refreshKey}")
    private String refreshKey;
    @Value("${jwt.resetKey}")
    private String resetKey;

    @Override
    public String generateToken(UserDetails user) {
        // Thực hiện logic tạo token JWT ở đây
        return generateToken(Map.of(), user);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateRefreshToken(Map.of(), user);
    }

    @Override
    public String generateResetToken(UserDetails user) {
        Map<String, Object> claims = Map.of(

                "name", user.getUsername(),// Giả sử email là tên người dùng
                "type", "reset" // Thêm claim để xác định loại token
        );
        return generateResetToken(claims, user);
    }
    private String generateResetToken(Map<String, Object> claims, @NotNull UserDetails user){
        // Thực hiện logic tạo token JWT với các claims ở đây
        return Jwts.builder()
                .setClaims(claims) // Thay thế bằng các claims thực tế
                .setSubject(user.getUsername()) // Thay thế bằng thông tin người dùng thực tế
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thay thế bằng thời gian phát hành thực tế
                .setExpiration(new Date (System.currentTimeMillis() + 300000 )) // Thay thế bằng thời gian hết hạn thực tế
                .signWith(getKey(TokenType.RESET_TOKEN), SignatureAlgorithm.HS256) // Thay thế bằng thuật toán mã hóa thực tế
                .compact(); // Trả về token JWT đã tạo
    }


    private String generateToken(Map<String, Object> claims, @NotNull UserDetails user){
        // Thực hiện logic tạo token JWT với các claims ở đây
        return Jwts.builder()
                .setClaims(claims) // Thay thế bằng các claims thực tế
                .setSubject(user.getUsername()) // Thay thế bằng thông tin người dùng thực tế
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thay thế bằng thời gian phát hành thực tế
                .setExpiration(new Date (System.currentTimeMillis() + Long.parseLong(expiryTime) )) // Thay thế bằng thời gian hết hạn thực tế
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256) // Thay thế bằng thuật toán mã hóa thực tế
                .compact(); // Trả về token JWT đã tạo
    }

    private String generateRefreshToken(Map<String, Object> claims, @NotNull UserDetails user){
        // Thực hiện logic tạo token JWT với các claims ở đây
        return Jwts.builder()
                .setClaims(claims) // Thay thế bằng các claims thực tế
                .setSubject(user.getUsername()) // Thay thế bằng thông tin người dùng thực tế
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thay thế bằng thời gian phát hành thực tế
                .setExpiration(new Date (System.currentTimeMillis() + Long.parseLong(expiryTime) * Integer.parseInt(expiryDay))) // Thay thế bằng thời gian hết hạn thực tế
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256) // Thay thế bằng thuật toán mã hóa thực tế
                .compact(); // Trả về token JWT đã tạo
    }

    private Key getKey(TokenType tokenType){

        byte[] keyBytes;
        if(tokenType == TokenType.ACCESS_TOKEN)
            keyBytes = Decoders.BASE64.decode(secretKey); // Chuyển đổi chuỗi bí mật thành mảng byte
        else if (tokenType == TokenType.RESET_TOKEN)
            keyBytes = Decoders.BASE64.decode(resetKey); // Chuyển đổi chuỗi bí mật thành mảng byte
        else
            keyBytes = Base64.getDecoder().decode(refreshKey);
        return Keys.hmacShaKeyFor(keyBytes); // Tạo khóa HMAC từ mảng byte
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject); // Trích xuất tên người dùng từ token JWT
    }

    @Override
    public boolean isValid(String token, UserDetails user, TokenType type) {
        String username = extractUsername(token, type); // Trích xuất tên người dùng từ token JWT

        return username.equals(user.getUsername()) && !isTokenExpired(token, type); // Kiểm tra xem tên người dùng trong token có khớp với tên người dùng đã cung cấp và token có hết hạn hay không
    }

    public boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date()); // Kiểm tra xem token đã hết hạn hay chưa
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type,  Claims::getExpiration); // Trích xuất thời gian hết hạn từ token JWT
    }

    private <T> T extractClaim(String token, TokenType type , Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token, type); // Phân tích token JWT để lấy các claims
        return claimsResolver.apply(claims); // Áp dụng hàm claimsResolver vào các claims đã phân tích
    }

    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parserBuilder() // Tạo một trình phân tích token JWT
                .setSigningKey(getKey(type)) // Thiết lập khóa ký
                .build()
                .parseClaimsJws(token) // Phân tích token JWT
                .getBody(); // Lấy phần thân của token
    }
}
