package com.fusion.bank.wallet.adapter.in.web.consumer.api;
import com.fusion.bank.wallet.adapter.in.web.dto.WalletDtoModel;
import lombok.Builder;
import java.util.UUID;

@Builder
public record UserWallerDtoModel (

        UUID id,
        String firstName,
        String lastName,
        String email,
        String state,
        String cpf,
        WalletDtoModel wallet
) {
}