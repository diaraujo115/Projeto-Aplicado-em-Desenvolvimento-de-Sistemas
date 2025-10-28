package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.dto.LoginRequestDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.LoginResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioUpdateDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
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

    @PutMapping("/meu-perfil")
    public ResponseEntity<UsuarioDTO> atualizarPerfil(@RequestBody UsuarioUpdateDTO dto) {
        Usuario usuarioAtualizado = usuarioService.atualizarPerfil(dto);

        UsuarioDTO responseDto = new UsuarioDTO();
        responseDto.setId(usuarioAtualizado.getId());
        responseDto.setNome(usuarioAtualizado.getNome());

        return ResponseEntity.ok(responseDto);
    }
    @DeleteMapping("/meu-perfil")
    public ResponseEntity<Void> deletarPerfil() {
        usuarioService.deletarPerfil();
        return ResponseEntity.noContent().build(); // 204 No Content é uma resposta padrão para delete bem-sucedido
    }
}