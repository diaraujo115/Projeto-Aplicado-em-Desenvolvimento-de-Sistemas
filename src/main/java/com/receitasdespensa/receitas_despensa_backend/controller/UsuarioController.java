package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.dto.LoginRequestDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.LoginResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrar(usuario);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            String token = usuarioService.autenticar(loginRequest);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<String> verPerfil() {
        return ResponseEntity.ok("Este é um endpoint protegido! Você está autenticado.");
    }
}