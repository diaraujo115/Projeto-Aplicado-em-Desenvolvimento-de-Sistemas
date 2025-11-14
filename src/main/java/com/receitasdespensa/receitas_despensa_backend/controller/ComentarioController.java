package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.model.Comentario;
import com.receitasdespensa.receitas_despensa_backend.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comentarios")

public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;


    @PutMapping("/{id}")
    public ResponseEntity<Comentario> atualizarComentario(
            @PathVariable Integer id,
            @RequestBody Map<String, String> requestBody) {

        try {
            String novoTexto = requestBody.get("texto");
            Comentario comentarioAtualizado = comentarioService.atualizarComentario(id, novoTexto);
            return ResponseEntity.ok(comentarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Integer id) {
        try {
            comentarioService.deletarComentario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}