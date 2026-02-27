package com.chursin.fraud;

import feign.RequestInterceptor;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignTracingConfig {

    private final Tracer tracer;

    @Bean
    public RequestInterceptor tracingInterceptor() {
        return requestTemplate -> {
            var currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                var traceContext = currentSpan.context();
                requestTemplate.header("X-B3-TraceId", traceContext.traceId());
                requestTemplate.header("X-B3-SpanId", traceContext.spanId());
            }
        };
    }
}
