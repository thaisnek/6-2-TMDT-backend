package com.example.webtmdt.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ShipmentResponse {

    private Long id;
    private Long orderId;
    private String orderCode;
    private String shipmentStatus;
    private Long deliveryStaffId;
    private String deliveryStaffName;
    private LocalDateTime assignedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private String failureReason;
    private LocalDateTime createdAt;
}
