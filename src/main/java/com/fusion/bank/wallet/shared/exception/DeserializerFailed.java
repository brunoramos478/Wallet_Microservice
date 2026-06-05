package com.fusion.bank.wallet.shared.exception;

public class DeserializerFailed extends RuntimeException {
    public DeserializerFailed() {
        super("Failed to deserialize UUID");
    }

    public DeserializerFailed(String message) {
        super(message);
    }
}
