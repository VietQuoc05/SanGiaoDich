package com.ecommerce.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long id;
    private String username;
    private List<CartItemResponse> items;
    private double totalPrice;
}