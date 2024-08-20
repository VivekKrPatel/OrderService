package com.example.ShipmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);

    @GetMapping
    public void processShipment() {
        try {
            // Simulate processing time
            Thread.sleep(1000);
            log.info("shipment Service");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
