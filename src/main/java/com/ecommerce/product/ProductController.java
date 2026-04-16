package com.ecommerce.product;

import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    @PostMapping
    public Product create(@RequestBody Product product, Authentication auth) {
        User supplier = userRepo.findByUsername(auth.getName()).orElseThrow();

        product.setSupplier(supplier);

        return productRepo.save(product);
    }

    @GetMapping
    public Object getAll() {
        return productRepo.findAll();
    }
}