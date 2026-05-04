package com.example.webtmdt.service;

import com.example.webtmdt.dto.request.AssignShipmentRequest;
import com.example.webtmdt.dto.response.ShipmentResponse;
import com.example.webtmdt.enums.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentService {

    ShipmentResponse assignDeliveryStaff(Long orderId, AssignShipmentRequest request);

    ShipmentResponse updateShipmentStatus(Long shipmentId, ShipmentStatus status);

    ShipmentResponse getShipmentByOrderId(Long orderId);

    Page<ShipmentResponse> getMyAssignedShipments(String username, Pageable pageable);
}
