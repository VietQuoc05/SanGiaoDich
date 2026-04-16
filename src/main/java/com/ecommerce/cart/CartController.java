package com.ecommerce.cart;

import com.ecommerce.product.Product;
import com.ecommerce.product.ProductRepository;
import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    @PostMapping("/{productId}")
    public String addToCart(@PathVariable Long productId, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        CartItem item = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(1)
                .build();

        cartRepo.save(item);

        return "Added to cart";
    }
}