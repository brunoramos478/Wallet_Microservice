package com.fusion.bank.wallet.adapter.out.web;

import com.fusion.bank.wallet.application.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SendMessageQueue {

    private final WalletService service;

    public void sendMessageQueue(String exchange, String routingKey, Object delivery) {
        service.sendMessageQueue(exchange, routingKey, delivery);
    }

}
