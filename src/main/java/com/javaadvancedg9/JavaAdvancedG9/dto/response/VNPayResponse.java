    package com.javaadvancedg9.JavaAdvancedG9.dto.response;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.NoArgsConstructor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
         public class VNPayResponse {
            public String code;
            public String message;
            public String paymentUrl;

    }