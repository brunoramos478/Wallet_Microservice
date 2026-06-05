package com.fusion.bank.wallet.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.UUID;

@Builder
public record TransferRequestDto(
        @NotNull
        @JdbcTypeCode(Types.BINARY)
        UUID userId,
        @NotNull
        @Positive
        BigDecimal value,
        @NotNull
        @JdbcTypeCode(Types.BINARY)
        UUID recipientUserId
) {

}
