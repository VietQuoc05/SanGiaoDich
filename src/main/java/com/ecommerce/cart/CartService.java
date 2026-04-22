package com.ecommerce.cart;

import com.ecommerce.cart.dto.*;
import com.ecommerce.product.Product;
import com.ecommerce.product.ProductRepository;
import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // ================= ENTITY (🔥 FIX CHO ORDER) =================
    public Cart getCartEntity(String username) {
        return cartRepo.findByUserUsername(username)
                .orElseGet(() -> createCart(username));
    }

    // ================= GET CART =================
    public CartResponse getCart(String username) {
        return mapToResponse(getCartEntity(username));
    }

    private Cart createCart(String username) {
        User user = userRepo.findByUsername(username).orElseThrow();

        Cart cart = Cart.builder()
                .user(user)
                .items(new ArrayList<>())
                .build();

        return cartRepo.save(cart);
    }

    // ================= ADD =================
    public CartResponse addToCart(Long productId, Integer quantity, String username) {

        Cart cart = getCartEntity(username);

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return mapToResponse(cartRepo.save(cart));
            }
        }

        CartItem item = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .cart(cart)
                .build();

        cart.getItems().add(item);

        return mapToResponse(cartRepo.save(cart));
    }

    // ================= REMOVE =================
    public void removeItem(Long itemId, String username) {
        Cart cart = getCartEntity(username);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        cartRepo.save(cart);
    }

    // ================= CLEAR =================
    public void clearCart(String username) {
        Cart cart = getCartEntity(username);
        cart.getItems().clear();
        cartRepo.save(cart);
    }

    // ================= MAPPER =================
    private CartResponse mapToResponse(Cart cart) {

        List<CartItemResponse> items = new ArrayList<>();
        double total = 0;

        for (CartItem i : cart.getItems()) {

            double itemTotal = i.getProduct().getPrice() * i.getQuantity();
            total += itemTotal;

            items.add(CartItemResponse.builder()
                    .itemId(i.getId())
                    .productId(i.getProduct().getId())
                    .productName(i.getProduct().getName())
                    .price(i.getProduct().getPrice())
                    .quantity(i.getQuantity())
                    .total(itemTotal)
                    .build());
        }

        return CartResponse.builder()
                .id(cart.getId())
                .username(cart.getUser().getUsername())
                .items(items)
                .totalPrice(total)
                .build();
    }
}