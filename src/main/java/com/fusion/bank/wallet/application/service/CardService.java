package com.fusion.bank.wallet.application.service;

import com.fusion.bank.wallet.model.mysql.entity.CardEntity;
import com.fusion.bank.wallet.model.mysql.repository.CardRepository;
import com.fusion.bank.wallet.shared.exception.CardFound;
import com.fusion.bank.wallet.shared.exception.CardNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository repository;

    public void createCard(CardEntity card) {
        if (repository.existsByCardNumber(card.getCardNumber())){
            throw new CardFound();
        }
        repository.save(card);
    }

    public BigDecimal getCardLimit(String cardNumber) {
        return repository.findByCardNumber(cardNumber)
                .orElseThrow(CardNotFound::new)
                .getLimitCard();
    }
}
