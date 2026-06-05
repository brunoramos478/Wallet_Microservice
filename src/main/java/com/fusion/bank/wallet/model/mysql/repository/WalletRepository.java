package com.fusion.bank.wallet.model.mysql.repository;

import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByUserId(UUID idWallet);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.userId = :userId OR w.userId = :recipientUserId ORDER BY w.userId ASC")
    List<WalletEntity> findWalletsForTransfer(@Param("userId") UUID userId, @Param("recipientUserId") UUID recipientUserId);
}
