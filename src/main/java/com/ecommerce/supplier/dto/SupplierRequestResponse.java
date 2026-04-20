package com.ecommerce.supplier.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierRequestResponse {
    private Long id;
    private String username;
    private String status;
}