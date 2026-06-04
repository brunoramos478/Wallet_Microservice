package com.fusion.bank.wallet.adapter.in.web.controller;

import com.fusion.bank.wallet.adapter.in.web.dto.TransferRequestDto;
import com.fusion.bank.wallet.adapter.in.web.dto.WalletDtoModel;
import com.fusion.bank.wallet.adapter.out.web.SendMessageQueue;
import com.fusion.bank.wallet.application.service.WalletService;
import com.fusion.bank.wallet.model.mysql.entity.TransactionEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    private final SendMessageQueue sendQueue;

    // @RabbitListener
    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletDtoModel> getBalance(@PathVariable UUID userId) {
        var balance = walletService.getBalanceUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(WalletDtoModel.builder()
                .id(userId)
                .balance(balance)
                .build()
        );

       // sendQueue.sendMessageQueue();
    }

    // @RabbitListener
    @GetMapping("/extract/user/{userId}/page/{page}/size/{size}")
    public ResponseEntity<List<TransactionEntity>> getExtractController(@PathVariable UUID userId, @PathVariable int page, @PathVariable int size) {
       Page<TransactionEntity> extract = walletService.getExtract(userId, page, size);

       return ResponseEntity.status(HttpStatus.OK).body(extract.getContent());

       // sendQueue.sendMessageQueue();
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransferRequestDto> newTransfer(@Valid @RequestBody TransferRequestDto requestDto) {
        walletService.newTransfer(requestDto.userId(), requestDto.value(), requestDto.recipientUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TransferRequestDto.builder()
                .userId(requestDto.userId())
                .value(requestDto.value())
                .recipientUserId(requestDto.recipientUserId())
                .build()

        );
    }
    @PostMapping("/deposit")
    public ResponseEntity<WalletDtoModel> newDeposit(@Valid @RequestBody WalletDtoModel requestDto) {
        walletService.newDeposit(requestDto.id(), requestDto.balance());
        return ResponseEntity.status(HttpStatus.CREATED).body(WalletDtoModel.builder()
                .id(requestDto.id())
                .balance(requestDto.balance())
                .build()
        );
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<WalletDtoModel> newWithdrawal(@Valid @RequestBody WalletDtoModel requestDto) {
        walletService.newWithdrawal(requestDto.id(), requestDto.balance());
        return ResponseEntity.status(HttpStatus.CREATED).body(WalletDtoModel.builder()
                .id(requestDto.id())
                .balance(requestDto.balance())
                .build()
        );
    }
}
