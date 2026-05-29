package com.fusion.bank.wallet.application.service;

import com.fusion.bank.wallet.model.mysql.entity.TransactionEntity;
import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import com.fusion.bank.wallet.model.mysql.repository.TransactionRepository;
import com.fusion.bank.wallet.model.mysql.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final TransactionRepository transactionRepository;


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

}
