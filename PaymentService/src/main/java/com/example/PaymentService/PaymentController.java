package com.example.PaymentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @GetMapping
    public void processPayment() {
        try {
            // Simulate processing time
            Thread.sleep(1000);
            System.out.println("payment Service");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
