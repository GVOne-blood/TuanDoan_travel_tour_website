package com.javaadvancedg9.JavaAdvancedG9.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;
    private String resetToken;

}
