package com.capcredit.authuser.controller;

import com.capcredit.authuser.model.AuthResponseDto;
import com.capcredit.authuser.model.LoginRequestDto;
import com.capcredit.authuser.model.RegisterRequestDto;
import com.capcredit.authuser.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
