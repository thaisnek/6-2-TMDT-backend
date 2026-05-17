package com.example.webtmdt.service.impl;

import com.example.webtmdt.configuration.MomoConfig;
import com.example.webtmdt.dto.response.PaymentResponse;
import com.example.webtmdt.entity.Order;
import com.example.webtmdt.entity.Payment;
import com.example.webtmdt.enums.PaymentMethod;
import com.example.webtmdt.enums.PaymentStatus;
import com.example.webtmdt.exception.AppException;
import com.example.webtmdt.exception.ResourceNotFoundException;
import com.example.webtmdt.mapper.PaymentMapper;
import com.example.webtmdt.repository.OrderRepository;
import com.example.webtmdt.repository.PaymentRepository;
import com.example.webtmdt.service.PaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final MomoConfig momoConfig;
    private final ObjectMapper objectMapper;

    // ==================== MOMO PAYMENT ====================

    @Override
    public String createMomoPayment(Order order) {
        try {
            String requestId = UUID.randomUUID().toString();
            String orderId = order.getOrderCode();
            String amount = order.getTotalAmount().toBigInteger().toString();
            String orderInfo = "Thanh toán đơn hàng " + order.getOrderCode();
            String requestType = "payWithMethod";
            String extraData = "";

            // Tạo raw signature
            String rawSignature = "accessKey=" + momoConfig.getAccessKey()
                    + "&amount=" + amount
                    + "&extraData=" + extraData
                    + "&ipnUrl=" + momoConfig.getIpnUrl()
                    + "&orderId=" + orderId
                    + "&orderInfo=" + orderInfo
                    + "&partnerCode=" + momoConfig.getPartnerCode()
                    + "&redirectUrl=" + momoConfig.getRedirectUrl()
                    + "&requestId=" + requestId
                    + "&requestType=" + requestType;

            String signature = hmacSHA256(momoConfig.getSecretKey(), rawSignature);

            // Tạo request body
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("partnerCode", momoConfig.getPartnerCode());
            bodyMap.put("partnerName", "WebTMDT");
            bodyMap.put("storeId", "WebTMDTStore");
            bodyMap.put("requestId", requestId);
            bodyMap.put("amount", Long.parseLong(amount));
            bodyMap.put("orderId", orderId);
            bodyMap.put("orderInfo", orderInfo);
            bodyMap.put("redirectUrl", momoConfig.getRedirectUrl());
            bodyMap.put("ipnUrl", momoConfig.getIpnUrl());
            bodyMap.put("lang", "vi");
            bodyMap.put("requestType", requestType);
            bodyMap.put("autoCapture", true);
            bodyMap.put("extraData", extraData);
            bodyMap.put("signature", signature);

            String requestBody = objectMapper.writeValueAsString(bodyMap);

            // Gửi request tới MoMo
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(momoConfig.getEndpoint()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode responseNode = objectMapper.readTree(httpResponse.body());

            int resultCode = responseNode.get("resultCode").asInt();
            if (resultCode == 0) {
                return responseNode.get("payUrl").asText();
            } else {
                log.warn("MoMo sandbox unavailable (resultCode={}). Returning mock URL for local testing.", resultCode);
                return "https://test-payment.momo.vn/mock?orderId=" + orderId + "&amount=" + amount;
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("MoMo payment exception", e);
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi kết nối cổng thanh toán MoMo");
        }
    }

    @Override
    @Transactional
    public void handleMomoCallback(Map<String, String> params) {
        try {
            String orderId = params.get("orderId");
            int resultCode = Integer.parseInt(params.get("resultCode"));
            String transId = params.get("transId");

            Order order = orderRepository.findByOrderCode(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Đơn hàng", "orderCode", orderId));

            Payment payment = paymentRepository.findByOrderId(order.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Thanh toán", "orderId", order.getId()));

            if (resultCode == 0) {
                // Thanh toán thành công
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setPaidAt(LocalDateTime.now());
                payment.setProviderTransactionId(transId);
                payment.setProviderName("MOMO");
                order.setPaymentStatus(PaymentStatus.PAID);
            } else {
                // Thanh toán thất bại
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment.setFailureReason("MoMo resultCode: " + resultCode);
                order.setPaymentStatus(PaymentStatus.FAILED);
            }

            paymentRepository.save(payment);
            orderRepository.save(order);

            log.info("MoMo callback processed for order {}: resultCode={}", orderId, resultCode);
        } catch (Exception e) {
            log.error("Error processing MoMo callback", e);
        }
    }

    // ==================== COD ====================

    @Override
    @Transactional
    public PaymentResponse confirmCodPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Thanh toán", "orderId", orderId));

        if (payment.getPaymentMethod() != PaymentMethod.COD) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng này không phải thanh toán COD");
        }

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Đơn hàng này đã được xác nhận thanh toán");
        }

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        Order order = payment.getOrder();
        order.setPaymentStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);
        orderRepository.save(order);

        return paymentMapper.toResponse(payment);
    }

    // ==================== READ ====================

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId, String username) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Thanh toán", "orderId", orderId));

        // Kiểm tra quyền: chỉ chủ đơn hàng hoặc ADMIN/SALES_STAFF mới được xem
        Order order = payment.getOrder();
        if (!order.getCustomer().getUserName().equals(username)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isStaff = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_SALES_STAFF"));
            if (!isStaff) {
                throw new AppException(HttpStatus.FORBIDDEN, "Không có quyền xem thông tin thanh toán này");
            }
        }

        return paymentMapper.toResponse(payment);
    }

    // ==================== HELPER ====================

    private String hmacSHA256(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
