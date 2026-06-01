package com.fusion.bank.wallet.shared.exception;

public class FailedSendQueue extends RuntimeException {

    public FailedSendQueue() {
        super("Failed to send message to queue");
    }

    public FailedSendQueue(String message) {
        super(message);
    }

    public FailedSendQueue(String message, Throwable cause) {
        super(message, cause);
    }
}
