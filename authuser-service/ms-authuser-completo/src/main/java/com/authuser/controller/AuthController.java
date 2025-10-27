package com.authuser.controller;


import com.authuser.model.AuthResponseDto;
import com.authuser.model.LoginRequestDto;
import com.authuser.model.RegisterRequestDto;

import com.authuser.service.AuthService;
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
