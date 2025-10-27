package com.authuser.service;

import com.authuser.model.Usuario;
import com.authuser.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public Usuario atualizarRenda(UUID id, BigDecimal renda) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario.setMonthlyIncome(renda);
        return usuarioRepository.save(usuario);
    }

}