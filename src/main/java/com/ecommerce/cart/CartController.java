package com.ecommerce.cart;

import com.ecommerce.cart.dto.CartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse get(Authentication auth) {
        return cartService.getCart(auth.getName());
    }

    @PostMapping("/add")
    public CartResponse add(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            Authentication auth
    ) {
        return cartService.addToCart(productId, quantity, auth.getName());
    }

    @DeleteMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId, Authentication auth) {
        cartService.removeItem(itemId, auth.getName());
        return "Removed";
    }

    @DeleteMapping("/clear")
    public String clear(Authentication auth) {
        cartService.clearCart(auth.getName());
        return "Cleared";
    }
}