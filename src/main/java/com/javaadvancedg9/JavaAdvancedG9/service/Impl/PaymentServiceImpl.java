package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import com.javaadvancedg9.JavaAdvancedG9.config.VNPAYConfig;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.VNPayResponse;
import com.javaadvancedg9.JavaAdvancedG9.service.PaymentService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final VNPAYConfig vnPayConfig;

    public VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Long.parseLong(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");

        // Lấy cấu hình cơ bản
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();

        // Tạo mã giao dịch duy nhất và thêm vào map
        String vnp_TxnRef = VNPayUtil.getRandomNumber(8);
        vnpParamsMap.put("vnp_TxnRef", vnp_TxnRef);
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);

        // Thêm các tham số động khác
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }
}