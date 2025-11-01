package com.capcredit.ms_loan.application.exceptions;

public class LoanException extends RuntimeException {
    public LoanException(String message) {
        super(message);
    }
    public LoanException(String message, Throwable cause) {
        super(message, cause);
    }
}
