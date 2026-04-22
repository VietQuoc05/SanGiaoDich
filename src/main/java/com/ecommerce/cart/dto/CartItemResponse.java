package com.ecommerce.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double total;
}