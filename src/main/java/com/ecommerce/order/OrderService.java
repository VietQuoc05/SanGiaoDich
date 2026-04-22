package com.ecommerce.order;

import com.ecommerce.cart.Cart;
import com.ecommerce.cart.CartItem;
import com.ecommerce.cart.CartService;
import com.ecommerce.order.dto.OrderItemResponse;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.payment.*;
import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final UserRepository userRepo;

    public OrderResponse checkout(String username, String address, PaymentMethod method) {

        Cart cart = cartService.getCartEntity(username);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart empty");
        }

        User user = userRepo.findByUsername(username).orElseThrow();

        Order order = Order.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        double total = 0;

        for (CartItem c : cart.getItems()) {

            OrderItem item = OrderItem.builder()
                    .product(c.getProduct())
                    .quantity(c.getQuantity())
                    .price(c.getProduct().getPrice())
                    .order(order)
                    .build();

            total += item.getPrice() * item.getQuantity();
            order.getItems().add(item);
        }

        order.setTotalPrice(total);

        Payment payment = Payment.builder()
                .amount(total)
                .method(method)
                .status(PaymentStatus.PENDING)
                .order(order)
                .build();

        order.setPayment(payment);

        orderRepo.save(order);

        if (method == PaymentMethod.COD) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.CONFIRMED);
        }

        cartService.clearCart(username);

        return map(order);
    }

    public List<OrderResponse> myOrders(String username) {
        return orderRepo.findByUserUsername(username)
                .stream()
                .map(this::map)
                .toList();
    }

    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = orderRepo.findById(id).orElseThrow();
        order.setStatus(status);
        return map(orderRepo.save(order));
    }

    private OrderResponse map(Order o) {

        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> OrderItemResponse.builder()
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(o.getId())
                .totalPrice(o.getTotalPrice())
                .status(o.getStatus().name())
                .address(o.getAddress())
                .items(items)
                .build();
    }
}