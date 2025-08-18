package com.security.security.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    private final Counter paymentIntentSucceeded;
    private final Counter paymentIntentPaymentFailed;

    public PaymentService(MeterRegistry meterRegistry) {
        this.paymentIntentSucceeded = Counter.builder("payment_intent_succeeded")
                .register(meterRegistry);
        this.paymentIntentPaymentFailed = Counter.builder("payment_intent_payment_failed")
                .register(meterRegistry);
    }

    @Async
    public void getPaymentIntent(String event) {
        switch (event) {
            case "payment_intent.succeeded" -> {
                log.debug("LOG DEBUG: payment_intent.succeeded");
                paymentIntentSucceeded.increment();
            }
            case "payment_intent.payment_failed" -> {
                log.debug("LOG DEBUG: payment_intent.payment_failed");
                paymentIntentPaymentFailed.increment();
            }
        }
    }
}
