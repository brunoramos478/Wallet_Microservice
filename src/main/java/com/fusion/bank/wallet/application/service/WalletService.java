package com.fusion.bank.wallet.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusion.bank.wallet.adapter.in.web.dto.TransferRequestDto;
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
import java.time.LocalDateTime;
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

    public LocalDateTime getCurrentDateTime() {
        return OffsetDateTime.now().toLocalDateTime();
    }

    @Transactional
    public WalletDtoModel newWithdrawal(WalletDtoModel dto) {
        WalletEntity wallet = repository.findByUserId(dto.id())
                .orElseThrow(WalletNotFound::new);

        if (wallet.getBalance().compareTo(dto.balance()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        if (dto.balance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de saque inválido");
        }

        wallet.setBalance(wallet.getBalance().subtract(dto.balance()));
        repository.save(wallet);

        setTransfer(wallet.getId(), dto.balance(), dto.id(), TransactionType.Withdrawal);

        return WalletDtoModel.builder()
                .id(dto.id())
                .balance(wallet.getBalance())
                .build();

    }

    @Transactional
    public WalletDtoModel newDeposit(WalletDtoModel dto) {
        WalletEntity wallet = repository.findByUserId(dto.id())
                .orElseThrow(WalletNotFound::new);

        wallet.setBalance(wallet.getBalance().add(dto.balance()));
        repository.save(wallet);

        setTransfer(wallet.getId(), dto.balance(), dto.id(), TransactionType.Deposit);

        return WalletDtoModel.builder()
                .id(dto.id())
                .balance(wallet.getBalance())
                .build();

    }


    // A busca é feita por meio do ID do usuário e não pelo ID da carteira.
    @Transactional
    public TransferRequestDto newTransfer(TransferRequestDto dto) {

        LocalDateTime hours = getCurrentDateTime();


        WalletEntity senderWallet = repository.findByUserId(dto.userId())
                .orElseThrow(WalletNotFound::new);

        WalletEntity recipientWallet = repository.findByUserId(dto.recipientUserId())
                .orElseThrow(WalletNotFound::new);

        if (senderWallet.getBalance().compareTo(dto.value()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        if (dto.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor de transferência inválido");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(dto.value()));
        recipientWallet.setBalance(recipientWallet.getBalance().add(dto.value()));

        repository.save(senderWallet);
        repository.save(recipientWallet);

        setTransfer(senderWallet.getId(), dto.value(), dto.userId(), TransactionType.Transfer);
        setTransfer(recipientWallet.getId(), dto.value(), dto.recipientUserId(), TransactionType.Receipt);

        return TransferRequestDto.builder()
                .userId(dto.userId())
                .value(dto.value())
                .recipientUserId(dto.recipientUserId()).build();
    }

    public void setTransfer(UUID walletId, BigDecimal value, UUID userId, TransactionType type) {
        TransactionEntity entity = new TransactionEntity();

        entity.setWalletId(walletId);
        entity.setUserId(userId);
        entity.setValue(value);
        entity.setType(type.name());

        transactionRepository.save(entity);

    }
}
