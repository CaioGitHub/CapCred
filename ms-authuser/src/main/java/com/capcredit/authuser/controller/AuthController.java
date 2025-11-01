package com.capcredit.authuser.controller;

import com.capcredit.authuser.model.AuthResponseDto;
import com.capcredit.authuser.model.LoginRequestDto;
import com.capcredit.authuser.model.RegisterRequestDto;
import com.capcredit.authuser.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registro de novos usuários. Retorna 201 CREATED.
     * @param request DTO com dados de registro, validado pelo @Valid.
     * @return ResponseEntity com AuthResponseDto.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para login de usuário. Retorna 200 OK.
     * @param request DTO com credenciais de login, validado pelo @Valid.
     * @return ResponseEntity com AuthResponseDto.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        AuthResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para renovação de token JWT. Retorna 200 OK.
     * @param refreshToken O token de renovação.
     * @return ResponseEntity com AuthResponseDto.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestParam String refreshToken) {
        AuthResponseDto response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
