package com.ecommerce.order;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderResponse checkout(
            @RequestParam String address,
            @RequestParam PaymentMethod method,
            Authentication auth
    ) {
        return orderService.checkout(auth.getName(), address, method);
    }

    @GetMapping("/my")
    public List<OrderResponse> myOrders(Authentication auth) {
        return orderService.myOrders(auth.getName());
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {
        return orderService.updateStatus(id, status);
    }
}