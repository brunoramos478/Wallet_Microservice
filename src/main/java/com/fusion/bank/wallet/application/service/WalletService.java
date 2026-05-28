package com.fusion.bank.wallet.application.service;

import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import com.fusion.bank.wallet.model.mysql.repository.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository repository;

    public void createWalletForUser(WalletEntity wallet) {
        repository.save(wallet);
    }
}
