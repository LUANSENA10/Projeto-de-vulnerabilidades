package com.security.security.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MeterRegistry meterRegistry;
    private Counter paymentIntentSucceeded;
    private Counter paymentIntentPaymentFailed;

    @PostConstruct
    public void init() {
        paymentIntentSucceeded = Counter.builder("payment_intent_succeeded")
                .register(meterRegistry);
        paymentIntentPaymentFailed = Counter.builder("payment_intent_payment_failed")
                .register(meterRegistry);
    }

    @Async
    public void getPaymentIntent(String event) {
        switch (event) {
            case "payment_intent.succeeded" -> {
                log.info("payment_intent.succeeded");
                paymentIntentSucceeded.increment();
            }
            case "payment_intent.payment_failed" -> {
                log.info("payment_intent.payment_failed");
                paymentIntentPaymentFailed.increment();
            }
        }
    }
}
