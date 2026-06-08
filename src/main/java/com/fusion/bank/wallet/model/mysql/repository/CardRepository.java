package com.fusion.bank.wallet.model.mysql.repository;

import com.fusion.bank.wallet.model.mysql.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    boolean existsByCardNumber(String cardNumber);

    Optional<CardEntity> findByCardNumber(String cardNumber);
    Optional<CardEntity> findByLimitCard(String cardNumber);
}
