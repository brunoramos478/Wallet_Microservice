package com.fusion.bank.wallet.shared.exception;

public class WalletExists extends RuntimeException {

    public WalletExists() {
        super("Wallet already exists");
    }

    public WalletExists(String message) {
        super(message);
    }

}
