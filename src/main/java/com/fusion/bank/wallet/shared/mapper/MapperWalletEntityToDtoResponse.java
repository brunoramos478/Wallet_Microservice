package com.fusion.bank.wallet.shared.mapper;


import com.fusion.bank.wallet.adapter.in.web.consumer.api.UserWallerDtoModel;
import com.fusion.bank.wallet.adapter.in.web.dto.WalletDtoModel;
import com.fusion.bank.wallet.model.mysql.entity.WalletEntity;

public class MapperWalletEntityToDtoResponse {

    public static WalletDtoModel toDto(WalletEntity entity) {
        if (entity == null) {
            return null;
        }
        return new WalletDtoModel(
                entity.getUserId(),
                entity.getBalance()
        );
    }

    public static WalletEntity toEntity(UserWallerDtoModel userDto) {
        if (userDto == null) {
            return null;
        }
        WalletEntity entity = new WalletEntity();
        entity.setUserId(userDto.id());
        if (userDto.wallet() != null) {
            entity.setBalance(userDto.wallet().balance());
        }
        return entity;
    }

}
