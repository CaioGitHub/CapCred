package com.capcredit.payment.adapters.in.exception;

import com.capcredit.payment.core.exception.InstallmentAlreadyPaidException;
import com.capcredit.payment.core.exception.InstallmentNotFoundException;
import com.capcredit.payment.core.exception.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());

        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        body.put("error", status != null ? status.getReasonPhrase() : "Error");

        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(exception = {InstallmentNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<?> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InstallmentAlreadyPaidException.class)
    public ResponseEntity<?> handleAlreadyPaid(InstallmentAlreadyPaidException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleForbiddenException(AccessDeniedException ex){
        log.error("Forbidden: ", ex);
        Map<String, Object> body =  new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(FORBIDDEN).body(body);
    }

}
