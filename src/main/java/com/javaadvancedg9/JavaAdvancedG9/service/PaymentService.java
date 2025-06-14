package com.javaadvancedg9.JavaAdvancedG9.service;

import com.javaadvancedg9.JavaAdvancedG9.dto.response.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {

    VNPayResponse createVnPayPayment(HttpServletRequest request);
}
