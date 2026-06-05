package com.fusion.bank.wallet.shared.exception;

public class WithdrawalInvalid extends RuntimeException {
    public WithdrawalInvalid() {
        super("Saque inválido");
    }

    public WithdrawalInvalid(String message) {
        super(message);
    }
}
