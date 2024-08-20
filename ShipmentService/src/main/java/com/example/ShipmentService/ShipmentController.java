package com.example.ShipmentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @GetMapping
    public void processShipment() {
        try {
            // Simulate processing time
            Thread.sleep(1000);
            System.out.println("shipment Service");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
