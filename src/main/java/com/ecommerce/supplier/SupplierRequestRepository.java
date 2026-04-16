package com.ecommerce.supplier;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRequestRepository extends JpaRepository<SupplierRequest, Long> {

    Optional<SupplierRequest> findByUserId(Long userId);
}