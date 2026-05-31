package com.fusion.bank.wallet.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusion.bank.wallet.adapter.in.web.dto.WalletDtoModel;
import com.fusion.bank.wallet.model.mysql.entity.TransactionEntity;
import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import com.fusion.bank.wallet.model.mysql.repository.TransactionRepository;
import com.fusion.bank.wallet.model.mysql.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;
    private final TextEncryptor textEncryptor;
    private final ObjectMapper objectMapper;



    public void createWalletForUser(WalletEntity wallet) {

        if(repository.findByUserId(wallet.getUserId()).isPresent()) {
            throw new RuntimeException("Wallet already exists");
        }

        wallet.setBalance(BigDecimal.ZERO);
        repository.save(wallet);

    }

    public BigDecimal getBalanceUser(UUID userId) {
        return repository.findByUserId(userId)
                .map(WalletEntity::getBalance)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

    }

    public Page<TransactionEntity> getExtract(UUID userId, int page, int size) {
        WalletEntity wallet = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        Pageable pageable = Pageable.ofSize(size).withPage(page);


        return transactionRepository.findAllByWalletId(wallet.getUserId(), pageable);
    }

    public void encryptJson(Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);
            String jsonEncrypted = textEncryptor.encrypt(json);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageQueue(String exchange, String routingKey, Object delivery) {
        encryptJson(delivery);
        rabbitTemplate.convertAndSend(exchange, routingKey, delivery);
    }

}
