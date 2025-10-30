package com.capcredit.authuser.controller;

import com.capcredit.authuser.model.Usuario;
import com.capcredit.authuser.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PutMapping("/{id}/income")
    public Usuario atualizarRenda(@PathVariable UUID id, @RequestParam BigDecimal renda) {
        return usuarioService.atualizarRenda(id, renda);
    }

}