package com.example.webtmdt.controller;

import com.example.webtmdt.dto.request.AssignShipmentRequest;
import com.example.webtmdt.dto.response.ApiResponse;
import com.example.webtmdt.dto.response.ShipmentResponse;
import com.example.webtmdt.enums.ShipmentStatus;
import com.example.webtmdt.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    /**
     * Admin: Phân công nhân viên giao hàng
     */
    @PutMapping("/api/admin/shipments/{orderId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> assignDeliveryStaff(
            @PathVariable Long orderId,
            @Valid @RequestBody AssignShipmentRequest request) {
        ShipmentResponse shipment = shipmentService.assignDeliveryStaff(orderId, request);
        return ResponseEntity.ok(ApiResponse.success("Phân công giao hàng thành công!", shipment));
    }

    /**
     * Admin/Shipper: Cập nhật trạng thái vận chuyển
     */
    @PutMapping("/api/shipments/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY_STAFF')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> updateShipmentStatus(
            @PathVariable Long id,
            @RequestParam ShipmentStatus status) {
        ShipmentResponse shipment = shipmentService.updateShipmentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái giao hàng thành công!", shipment));
    }

    /**
     * Lấy thông tin vận chuyển theo đơn hàng (ADMIN/SALES_STAFF/DELIVERY_STAFF)
     */
    @GetMapping("/api/shipments/order/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SALES_STAFF', 'DELIVERY_STAFF')")
    public ResponseEntity<ApiResponse<ShipmentResponse>> getShipmentByOrder(
            @PathVariable Long orderId) {
        ShipmentResponse shipment = shipmentService.getShipmentByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin vận chuyển thành công!", shipment));
    }

    /**
     * Shipper: Lấy danh sách đơn được phân công
     */
    @GetMapping("/api/shipments/my-assignments")
    @PreAuthorize("hasRole('DELIVERY_STAFF')")
    public ResponseEntity<ApiResponse<Page<ShipmentResponse>>> getMyAssignments(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("assignedAt").descending());
        Page<ShipmentResponse> shipments = shipmentService.getMyAssignedShipments(userDetails.getUsername(), pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn giao hàng thành công!", shipments));
    }
}
