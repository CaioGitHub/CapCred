package com.capcredit.ms_notification.application.exception;

public class SMSSendException extends RuntimeException {
    public SMSSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
