package com.capcredit.authuser.service;

import com.capcredit.authuser.model.*;

import com.capcredit.authuser.model.AuthResponseDto;
import com.capcredit.authuser.model.LoginRequestDto;
import com.capcredit.authuser.model.RegisterRequestDto;
import com.capcredit.authuser.model.Usuario;
import com.capcredit.authuser.repository.UsuarioRepository;
import com.capcredit.authuser.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponseDto register(RegisterRequestDto request) {
        Usuario usuario = new Usuario();
        usuario.setName(request.getName());
        usuario.setCpf(request.getCpf());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setMonthlyIncome(request.getMonthlyIncome());
        usuario.setRole(Role.CLIENT);
        usuarioRepository.save(usuario);

        String accessToken = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(usuario.getId()).getToken();

        return new AuthResponseDto(accessToken, refreshToken, usuario.getRole().name());
    }


    @Transactional
    public AuthResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new BadCredentialsException("Senha inválida");
        }

        String accessToken = jwtService.generateToken(usuario.getId(),  usuario.getEmail(), usuario.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(usuario.getId()).getToken();

        return new AuthResponseDto(accessToken, refreshToken, usuario.getRole().name());
    }

    @Transactional
    public AuthResponseDto refreshToken(String refreshToken) {
        var token = refreshTokenService.validateRefreshToken(refreshToken);
        String accessToken = jwtService.generateToken(token.getUserId(), token.getUserId().toString(), "CLIENT");
        return new AuthResponseDto(accessToken, token.getToken(), "CLIENT");
    }
}

