package com.example.ShipmentService;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    private static final Logger log = LoggerFactory.getLogger(ShipmentController.class);

    @Autowired
    private Tracer tracer;

    @GetMapping
    public void processShipment() {
        Span span = tracer.spanBuilder("ShipmentService.processShipment")
                .setSpanKind(SpanKind.INTERNAL)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            // Simulate processing time
            Thread.sleep(1000);
            log.info("Shipment Service");
            span.setStatus(StatusCode.OK, "Shipment processed successfully.");
        } catch (InterruptedException e) {
            span.setStatus(StatusCode.ERROR, "Shipment processing interrupted.");
            span.recordException(e);
            Thread.currentThread().interrupt();
        } finally {
            span.end();
        }
    }
}
