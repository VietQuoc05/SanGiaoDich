package com.ecommerce.order.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long id;
    private Double totalPrice;
    private String status;
    private String address;
    private List<OrderItemResponse> items;
}