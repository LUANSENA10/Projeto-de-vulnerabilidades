package com.security.security.controller;

import com.security.security.controller.request.Payload;
import com.security.security.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    public String getPaymentIntent(@RequestBody Payload payload) {
        paymentService.getPaymentIntent(payload.event());
        return "Evento recebido com sucesso.";
    }
}

