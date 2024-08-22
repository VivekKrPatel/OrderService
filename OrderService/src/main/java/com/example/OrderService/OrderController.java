package com.example.OrderService;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.api.GlobalOpenTelemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Tracer tracer;

    @GetMapping("/addOrder")
    public String addOrder() {
        Span span = tracer.spanBuilder("OrderService.addOrder")
                .setSpanKind(SpanKind.INTERNAL)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("order.id", "1234");

            // Calls to other services
            callPaymentService();
            callShipmentService();

            span.setStatus(StatusCode.OK, "Order added successfully.");
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, "Order addition failed.");
            span.recordException(e);
            return "Order addition failed.";
        } finally {
            span.end();
        }

        return "Order added successfully.";
    }

    private void callPaymentService() {
        Span span = tracer.spanBuilder("OrderService.callPaymentService")
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            String paymentServiceUrl = "http://payment-service:80/payment";
            HttpHeaders headers = new HttpHeaders();
            Context currentContext = Context.current();

            // Inject the context (traceId, spanId) into the HTTP headers
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(currentContext, headers, setter());

            // Make the HTTP call
            restTemplate.getForObject(paymentServiceUrl, Void.class);
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, "Payment service call failed.");
            span.recordException(e);
        } finally {
            span.end();
        }
    }

    private void callShipmentService() {
        Span span = tracer.spanBuilder("OrderService.callShipmentService")
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            String shipmentServiceUrl = "http://shipment-service:80/shipment";
            HttpHeaders headers = new HttpHeaders();
            Context currentContext = Context.current();

            // Inject the context (traceId, spanId) into the HTTP headers
            GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(currentContext, headers, setter());

            // Make the HTTP call
            restTemplate.getForObject(shipmentServiceUrl, Void.class);
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, "Shipment service call failed.");
            span.recordException(e);
        } finally {
            span.end();
        }
    }

    private TextMapSetter<HttpHeaders> setter() {
        return HttpHeaders::set;
    }
}
