package com.capcredit.payment.core.exception;

import java.util.UUID;

public class InstallmentNotFoundException extends RuntimeException {
    public InstallmentNotFoundException(UUID id) {
        super("Installment not found with ID: " + id);
    }


}
