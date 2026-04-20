package com.ecommerce.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;

    // 🔥 danh sách ảnh
    private List<String> imageUrls;

    private String supplierName;
}