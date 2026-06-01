package com.fusion.bank.wallet.shared.exception;

public class WalletNotFound extends RuntimeException {
    public WalletNotFound() {
        super("Wallet not found");
    }

    public WalletNotFound (String message) {
        super(message);

    }
}
