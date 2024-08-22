package com.example.ShipmentService;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class ShipmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentServiceApplication.class, args);
	}

	@Bean
	public Tracer tracer() {
		return GlobalOpenTelemetry.getTracer("ShipmentServiceTracer");
	}

	@PostConstruct
	public void init() {
		// Configure OTLP exporter
		OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
				.setEndpoint("http://localhost:4318")
				.build();

		// Configure Tracer Provider
		SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
				.build();

		// Register the OpenTelemetry SDK as the global tracer provider
		OpenTelemetrySdk.builder()
				.setTracerProvider(tracerProvider)
				.buildAndRegisterGlobal();
	}
}
