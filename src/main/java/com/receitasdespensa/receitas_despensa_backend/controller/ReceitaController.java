package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receitas") // Todos os endpoints aqui começarão com /api/receitas
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<Receita> criar(@RequestBody Receita receita) {
        Receita novaReceita = receitaService.criar(receita);
        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Receita>> listarTodas() {
        return ResponseEntity.ok(receitaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receita> buscarPorId(@PathVariable Integer id) {
        Optional<Receita> receita = receitaService.buscarPorId(id);
        return receita.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        receitaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
