package com.fusion.bank.wallet.shared.exception;

public class FailedEncrypto extends RuntimeException {
    public FailedEncrypto() {
        super("Failed to encrypt data");
    }

    public FailedEncrypto(String message) {
        super(message);
    }

    public FailedEncrypto(String message, Throwable cause) {
        super(message, cause);
    }
}
