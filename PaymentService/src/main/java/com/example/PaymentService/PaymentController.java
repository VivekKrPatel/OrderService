package com.example.PaymentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping
    public void processPayment() {
        try {
            // Simulate processing time
            Thread.sleep(1000);
            log.info("payment Service");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
