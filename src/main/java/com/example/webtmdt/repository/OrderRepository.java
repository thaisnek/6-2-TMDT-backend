package com.example.webtmdt.repository;

import com.example.webtmdt.entity.Order;
import com.example.webtmdt.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    Optional<Order> findByOrderCode(String orderCode);

    Page<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus, Pageable pageable);

    boolean existsByOrderCode(String orderCode);
}
