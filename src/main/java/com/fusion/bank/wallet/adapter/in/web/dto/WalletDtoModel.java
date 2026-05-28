package com.fusion.bank.wallet.adapter.in.web.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record WalletDtoModel(
        UUID id,
        BigDecimal balance
) {
}