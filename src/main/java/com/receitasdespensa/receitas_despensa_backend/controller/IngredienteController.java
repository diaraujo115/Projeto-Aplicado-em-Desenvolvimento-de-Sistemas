package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.model.Ingrediente;
import com.receitasdespensa.receitas_despensa_backend.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ingredientes")

public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @PostMapping
    public ResponseEntity<Ingrediente> criar(@RequestBody Ingrediente ingrediente) {
        Ingrediente novoIngrediente = ingredienteService.criar(ingrediente);
        return new ResponseEntity<>(novoIngrediente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Ingrediente>> listarTodas() {
        return ResponseEntity.ok(ingredienteService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> buscarPorId(@PathVariable Integer id) {
        Optional<Ingrediente> ingrediente = ingredienteService.buscarPorId(id);
        return ingrediente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        ingredienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
