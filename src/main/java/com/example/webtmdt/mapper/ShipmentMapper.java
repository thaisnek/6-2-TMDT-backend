package com.example.webtmdt.mapper;

import com.example.webtmdt.dto.response.ShipmentResponse;
import com.example.webtmdt.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.orderCode", target = "orderCode")
    @Mapping(source = "deliveryStaff.id", target = "deliveryStaffId")
    @Mapping(source = "deliveryStaff.fullName", target = "deliveryStaffName")
    ShipmentResponse toResponse(Shipment shipment);
}
