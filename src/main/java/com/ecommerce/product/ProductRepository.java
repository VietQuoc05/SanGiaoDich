package com.ecommerce.product;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Product findByIdWithImages(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images")
    List<Product> findAllWithImages();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.supplier.username = :username")
    List<Product> findBySupplierUsernameWithImages(@Param("username") String username);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchWithImages(@Param("keyword") String keyword);
}