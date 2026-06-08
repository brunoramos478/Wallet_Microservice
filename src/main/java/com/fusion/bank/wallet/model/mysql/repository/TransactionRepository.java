package com.fusion.bank.wallet.model.mysql.repository;

import com.fusion.bank.wallet.model.mysql.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    Page<TransactionEntity> findAllByWalletIdOrderByCreatedInDesc(UUID walletId, Pageable pageable);
}