package com.example.PaymentService;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private Tracer tracer;

    @GetMapping
    public void processPayment(@RequestHeader HttpHeaders headers) {
        // Extract context from incoming headers
        TextMapPropagator propagator = GlobalOpenTelemetry.getPropagators().getTextMapPropagator();
        Context context = propagator.extract(Context.current(), headers, getter());

        // Create a span for processing the payment
        Span span = tracer.spanBuilder("PaymentService.processPayment")
                .setParent(context)
                .setSpanKind(SpanKind.SERVER)
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            // Simulate processing time
            Thread.sleep(1000);
            log.info("Payment processed successfully.");
            span.setStatus(io.opentelemetry.api.trace.StatusCode.OK);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, "Payment processing failed.");
            span.recordException(e);
        } finally {
            span.end();
        }
    }

    private TextMapGetter<HttpHeaders> getter() {
        return new TextMapGetter<>() {
            @Override
            public Iterable<String> keys(HttpHeaders carrier) {
                return carrier.keySet();
            }

            @Override
            public String get(HttpHeaders carrier, String key) {
                return carrier.getFirst(key);
            }
        };
    }
}
