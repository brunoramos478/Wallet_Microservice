package com.fusion.bank.wallet.shared.exception;

public class CardNotFound extends RuntimeException {
    public CardNotFound() {
        super("Cartão não encontrado.");
    }

    public CardNotFound(String message) {
        super(message);
    }

}
