package com.fusion.bank.wallet.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusion.bank.wallet.adapter.in.web.dto.WalletDtoModel;
import com.fusion.bank.wallet.model.mysql.entity.TransactionEntity;
import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import com.fusion.bank.wallet.model.mysql.repository.TransactionRepository;
import com.fusion.bank.wallet.model.mysql.repository.WalletRepository;
import com.fusion.bank.wallet.shared.enums.TransactionType;
import com.fusion.bank.wallet.shared.exception.FailedEncrypto;
import com.fusion.bank.wallet.shared.exception.FailedSendQueue;
import com.fusion.bank.wallet.shared.exception.WalletExists;
import com.fusion.bank.wallet.shared.exception.WalletNotFound;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;
    private final TextEncryptor textEncryptor;
    private final ObjectMapper objectMapper;


    @Transactional
    public void createWalletForUser(WalletEntity wallet) {

        if(repository.findByUserId(wallet.getUserId()).isPresent()) {
            throw new WalletExists();
        }

        wallet.setBalance(BigDecimal.ZERO);
        repository.save(wallet);

    }

    @Transactional
    public BigDecimal getBalanceUser(UUID userId) {
        return repository.findByUserId(userId)
                .map(WalletEntity::getBalance)
                .orElseThrow(() -> new WalletNotFound());
    }

    @Transactional
    public Page<TransactionEntity> getExtract(UUID userId, int page, int size) {
        WalletEntity wallet = repository.findByUserId(userId)
                .orElseThrow(WalletNotFound::new);

        if(page <= 0 || size <= 0) {
            page = 0;
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository.findAllByWalletId(wallet.getId(), pageable);
    }

    public String encryptJson(Object payload) {

        try {
            String json = objectMapper.writeValueAsString(payload);
            String jsonEncrypted = textEncryptor.encrypt(json);
            return jsonEncrypted;
        }
        catch (Exception e) {
            throw new FailedEncrypto();
        }
    }

    public void sendMessageQueue(String exchange, String routingKey, Object delivery) {

        try {
            String jsonEncrypted = encryptJson(delivery);
            rabbitTemplate.convertAndSend(exchange, routingKey, jsonEncrypted);
        }

        catch (Exception e) {
            throw new FailedSendQueue();
        }
    }

    public OffsetDateTime getCurrentDateTime() {
        return OffsetDateTime.now();
    }

    @Transactional
    public void newWithdrawal(UUID userId, BigDecimal value) {
        WalletEntity wallet = repository.findByUserId(userId)
                .orElseThrow(WalletNotFound::new);

        if (wallet.getBalance().compareTo(value) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de saque inválido");
        }

        wallet.setBalance(wallet.getBalance().subtract(value));
        repository.save(wallet);

        var transaction = new TransactionEntity();

        transaction.setWalletId(wallet.getId());
        transaction.setUserId(userId);
        transaction.setValue(value);
        transaction.setType(TransactionType.Withdrawal.name());

        transactionRepository.save(transaction);

    }

    @Transactional
    public void newDeposit(UUID userId, BigDecimal value) {
        WalletEntity wallet = repository.findByUserId(userId)
                .orElseThrow(WalletNotFound::new);

        wallet.setBalance(wallet.getBalance().add(value));
        repository.save(wallet);

        var transaction = new TransactionEntity();

        transaction.setWalletId(wallet.getId());
        transaction.setUserId(userId);
        transaction.setValue(value);
        transaction.setType(TransactionType.Deposit.name());

        transactionRepository.save(transaction);

    }

    @Transactional
    public void newTransfer(UUID userId, BigDecimal value, UUID recipientUserId) {

        WalletEntity senderWallet = repository.findByUserId(userId)
                .orElseThrow(WalletNotFound::new);

        WalletEntity recipientWallet = repository.findByUserId(recipientUserId)
                .orElseThrow(WalletNotFound::new);

        if (senderWallet.getBalance().compareTo(value) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de transferência inválido");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(value));
        recipientWallet.setBalance(recipientWallet.getBalance().add(value));

        repository.save(senderWallet);
        repository.save(recipientWallet);

        var transactionSender = new TransactionEntity();

        transactionSender.setWalletId(senderWallet.getId());
        transactionSender.setUserId(userId);
        transactionSender.setValue(value);
        transactionSender.setType(TransactionType.Transfer.name());

        transactionRepository.save(transactionSender);

        var transactionRecipient = new TransactionEntity();

        transactionRecipient.setWalletId(recipientWallet.getId());
        transactionRecipient.setUserId(recipientUserId);
        transactionRecipient.setValue(value);
        transactionRecipient.setType(TransactionType.Receipt.name());

        transactionRepository.save(transactionRecipient);
    }
}
