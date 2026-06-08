package com.fusion.bank.wallet.shared.exception;

public class CardFound extends RuntimeException {
    public CardFound() {
        super("Card already exists");
    }

    public CardFound(String message) {
        super(message);
    }
}
