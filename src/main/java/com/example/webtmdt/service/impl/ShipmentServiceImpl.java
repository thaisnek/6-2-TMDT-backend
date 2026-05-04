package com.example.webtmdt.service.impl;

import com.example.webtmdt.dto.request.AssignShipmentRequest;
import com.example.webtmdt.dto.response.ShipmentResponse;
import com.example.webtmdt.entity.Order;
import com.example.webtmdt.entity.Shipment;
import com.example.webtmdt.entity.User;
import com.example.webtmdt.enums.OrderStatus;
import com.example.webtmdt.enums.PaymentMethod;
import com.example.webtmdt.enums.PaymentStatus;
import com.example.webtmdt.enums.ShipmentStatus;
import com.example.webtmdt.exception.AppException;
import com.example.webtmdt.exception.ResourceNotFoundException;
import com.example.webtmdt.mapper.ShipmentMapper;
import com.example.webtmdt.repository.OrderRepository;
import com.example.webtmdt.repository.ShipmentRepository;
import com.example.webtmdt.repository.UserRepository;
import com.example.webtmdt.service.PaymentService;
import com.example.webtmdt.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShipmentMapper shipmentMapper;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public ShipmentResponse assignDeliveryStaff(Long orderId, AssignShipmentRequest request) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Vận đơn", "orderId", orderId));

        User staff = userRepository.findById(request.getDeliveryStaffId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên", "id", request.getDeliveryStaffId()));

        shipment.setDeliveryStaff(staff);
        shipment.setShipmentStatus(ShipmentStatus.ASSIGNED);
        shipment.setAssignedAt(LocalDateTime.now());

        Order order = shipment.getOrder();
        order.setShippingStatus(ShipmentStatus.ASSIGNED);
        orderRepository.save(order);

        shipment = shipmentRepository.save(shipment);
        return shipmentMapper.toResponse(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponse updateShipmentStatus(Long shipmentId, ShipmentStatus status) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Vận đơn", "id", shipmentId));

        Order order = shipment.getOrder();

        switch (status) {
            case SHIPPING:
                shipment.setShippedAt(LocalDateTime.now());
                order.setOrderStatus(OrderStatus.SHIPPING);
                break;
            case DELIVERED:
                shipment.setDeliveredAt(LocalDateTime.now());
                order.setOrderStatus(OrderStatus.DELIVERED);
                // COD → tự động xác nhận thanh toán
                if (order.getPaymentStatus() == PaymentStatus.PENDING
                        && order.getOrderItems().get(0) != null) {
                    try {
                        paymentService.confirmCodPayment(order.getId());
                    } catch (Exception e) {
                        // ignore nếu không phải COD
                    }
                }
                break;
            case FAILED:
                shipment.setFailureReason("Giao hàng thất bại");
                break;
            default:
                break;
        }

        shipment.setShipmentStatus(status);
        order.setShippingStatus(status);

        orderRepository.save(order);
        shipment = shipmentRepository.save(shipment);
        return shipmentMapper.toResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentByOrderId(Long orderId) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Vận đơn", "orderId", orderId));
        return shipmentMapper.toResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShipmentResponse> getMyAssignedShipments(String username, Pageable pageable) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng", "username", username));
        return shipmentRepository.findByDeliveryStaffId(user.getId(), pageable)
                .map(shipmentMapper::toResponse);
    }
}
