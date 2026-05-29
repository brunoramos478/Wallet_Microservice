package com.fusion.bank.wallet.adapter.in.web.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusion.bank.wallet.adapter.in.web.consumer.api.UserWallerDtoModel;
import com.fusion.bank.wallet.application.service.WalletService;
import com.fusion.bank.wallet.infra.config.DescryptoPayload;
import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class ConsumerWallet {

    private final WalletService service;
    private final DescryptoPayload descryptoPayload;

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

        var newWallet = new WalletEntity();

        newWallet.setUserId(wallerDtoModel.id());
        service.createWalletForUser(newWallet);

    }
}