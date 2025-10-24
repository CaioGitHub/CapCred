package com.capcredit.ms_loan.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.capcredit.ms_loan.application.exceptions.EntityNotFoundException;
import com.capcredit.ms_loan.application.exceptions.LoanException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex){
        log.error("Internal Server Error: ", ex);
        Map<String, Object> body =  new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(body);
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(EntityNotFoundException ex){
        log.error("Not Found: ", ex);
        Map<String, Object> body =  new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).body(body);
    }

    @ExceptionHandler(exception = {LoanException.class, IllegalArgumentException.class, ArithmeticException.class})
    public ResponseEntity<?> handleBadRequestException(Exception ex){
        log.error("Bad Request: ", ex);
        Map<String, Object> body =  new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBadRequestException(MethodArgumentNotValidException ex){
        log.error("Bad Request: ", ex);
        Map<String, String> erros =  new HashMap<>();
        for(FieldError fe : ex.getBindingResult().getFieldErrors()){
            erros.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.status(BAD_REQUEST).body(erros);
    }
}
