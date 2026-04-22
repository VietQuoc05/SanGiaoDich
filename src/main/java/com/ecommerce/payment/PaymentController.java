package com.ecommerce.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/callback")
    public String callback(@RequestParam String transactionId) {
        paymentService.paymentSuccess(transactionId);
        return "OK";
    }
}