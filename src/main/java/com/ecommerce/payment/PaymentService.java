package com.ecommerce.payment;

import com.ecommerce.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;

    public void paymentSuccess(String transactionId) {

        Payment payment = paymentRepo.findByTransactionId(transactionId)
                .orElseThrow();

        payment.setStatus(PaymentStatus.SUCCESS);

        Order order = payment.getOrder();
        order.setStatus(com.ecommerce.order.OrderStatus.CONFIRMED);

        paymentRepo.save(payment);
    }
}