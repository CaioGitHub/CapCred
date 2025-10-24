package com.capcredit.payment.core.exception;

import java.util.UUID;

public class InstallmentAlreadyPaidException extends RuntimeException {
    public InstallmentAlreadyPaidException(UUID id) {
        super("Installment " + id + " is already paid.");
    }
}