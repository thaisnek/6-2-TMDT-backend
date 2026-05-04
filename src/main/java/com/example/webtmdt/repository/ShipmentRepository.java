package com.example.webtmdt.repository;

import com.example.webtmdt.entity.Shipment;
import com.example.webtmdt.enums.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByOrderId(Long orderId);

    Page<Shipment> findByDeliveryStaffId(Long staffId, Pageable pageable);

    Page<Shipment> findByDeliveryStaffIdAndShipmentStatus(Long staffId, ShipmentStatus status, Pageable pageable);
}
