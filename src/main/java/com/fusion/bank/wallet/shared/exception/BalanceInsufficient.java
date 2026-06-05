package com.fusion.bank.wallet.shared.exception;

public class BalanceInsufficient extends RuntimeException {
    public BalanceInsufficient() {
        super("Saldo insuficiente.");
    }

    public BalanceInsufficient(String message) {
        super(message);
    }
}
