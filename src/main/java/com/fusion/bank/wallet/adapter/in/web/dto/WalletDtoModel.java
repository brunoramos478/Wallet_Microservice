package com.fusion.bank.wallet.adapter.in.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fusion.bank.wallet.infra.config.DeserializerUUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.UUID;

@Builder
public record WalletDtoModel(
        @JdbcTypeCode(Types.BINARY)
        @JsonDeserialize(using = DeserializerUUID.class)
        @NotNull
        UUID id,
        @NotNull
        @Positive
        BigDecimal balance
) {
}