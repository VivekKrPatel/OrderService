package com.example.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/addOrder")
    public String addOrder() {

        String paymentServiceUrl = "http://payment-service:80";
        restTemplate.getForObject(paymentServiceUrl, Void.class);

        String shipmentServiceUrl = "http://shipment-service:80";
        restTemplate.getForObject(shipmentServiceUrl, Void.class);

        return "Order added successfully.";
    }
}
