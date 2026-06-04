package com.fusion.bank.wallet.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TransferRequestDto(
        @NotNull
        UUID userId,
        @NotNull
        @Positive
        BigDecimal value,
        @NotNull
        UUID recipientUserId
) {

}
