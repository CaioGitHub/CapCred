package com.capcredit.authuser.controller;

import com.capcredit.authuser.model.Usuario;
import com.capcredit.authuser.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable UUID id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping()
    public ResponseEntity<List<Usuario>> getAllUsers(){
        List<Usuario> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}/income")
    public Usuario atualizarRenda(@PathVariable UUID id, @RequestParam BigDecimal renda) {
        return usuarioService.atualizarRenda(id, renda);
    }
}