package com.fusion.bank.wallet.shared.exception;

public class InvalidTransfer extends RuntimeException {
    public InvalidTransfer() {
        super("Transferência inválida");
    }

    public InvalidTransfer(String message) {
        super(message);
    }

}
