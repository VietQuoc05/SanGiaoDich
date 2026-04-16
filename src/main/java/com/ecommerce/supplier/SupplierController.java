package com.ecommerce.supplier;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SupplierController {

    private final SupplierService supplierService;

    // 👤 USER gửi request
    @PostMapping("/request")
    public String request(Authentication auth) {
        return supplierService.requestBecomeSupplier(auth.getName());
    }

    // 👑 ADMIN xem tất cả request
    @GetMapping("/requests")
    public List<SupplierRequest> getAll() {
        return supplierService.getAllRequests();
    }

    // 👑 ADMIN approve
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        return supplierService.approve(id);
    }

    // 👑 ADMIN reject
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        return supplierService.reject(id);
    }
}