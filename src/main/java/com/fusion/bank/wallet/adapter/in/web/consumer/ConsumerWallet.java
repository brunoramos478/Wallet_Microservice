package com.fusion.bank.wallet.adapter.in.web.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusion.bank.wallet.adapter.in.web.consumer.api.UserWallerDtoModel;
import com.fusion.bank.wallet.application.service.WalletService;
import com.fusion.bank.wallet.infra.config.DescryptoPayload;
import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ConsumerWallet {

    private final WalletService service;
    private final DescryptoPayload descryptoPayload;
    private final ObjectMapper objectMapper;

    public ConsumerWallet(WalletService service, DescryptoPayload descryptoPayload, ObjectMapper objectMapper) {
        this.service = service;
        this.descryptoPayload = descryptoPayload;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "wallet-queue")
    public void receiverMessage(String payload) {

        UserWallerDtoModel wallerDtoModel;

        try {

            String jsonEncrypted = descryptoPayload.textEncryptor().decrypt(payload);

            wallerDtoModel = descryptoPayload.objectMapper().readValue(jsonEncrypted, UserWallerDtoModel.class);
        }

        catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Received message: " + wallerDtoModel);
        System.out.println("Received message CPF: " + wallerDtoModel.cpf());
        System.out.println("Received message ID: " + wallerDtoModel.id());
        System.out.println("Received message FIRST NAME: " + wallerDtoModel.firstName());
        System.out.println("Received message LAST NAME: " + wallerDtoModel.lastName());
        System.out.println("Received message EMAIL: " + wallerDtoModel.email());
        System.out.println("Received message STATE: " + wallerDtoModel.state());

        var newWallet = new WalletEntity();

        newWallet.setUserId(wallerDtoModel.id());

        BigDecimal initBalance = BigDecimal.ZERO;

        if(wallerDtoModel.wallet() != null) {
            if(wallerDtoModel.wallet().balance() != null) {
                initBalance = wallerDtoModel.wallet().balance();
            }
        }
        newWallet.setBalance(initBalance);


        service.createWalletForUser(newWallet);

    }
}