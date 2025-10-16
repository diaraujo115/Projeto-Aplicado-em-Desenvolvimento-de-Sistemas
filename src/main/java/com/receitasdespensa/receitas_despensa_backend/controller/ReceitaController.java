package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Classificacao;
import com.receitasdespensa.receitas_despensa_backend.model.Comentario;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.service.ClassificacaoService;
import com.receitasdespensa.receitas_despensa_backend.service.ComentarioService;
import com.receitasdespensa.receitas_despensa_backend.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/receitas") // Todos os endpoints aqui começarão com /api/receitas
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ClassificacaoService classificacaoService;

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
    public ResponseEntity<ReceitaResponseDTO> buscarPorId(@PathVariable Integer id) {
        Optional<ReceitaResponseDTO> receitaDto = receitaService.buscarPorId(id);
        return receitaDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        receitaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{receitaId}/comentarios")
    public ResponseEntity<Comentario> adicionarComentario(
            @PathVariable Integer receitaId,
            @RequestBody Map<String, String> request) {

        String texto = request.get("texto");
        Comentario novoComentario = comentarioService.adicionarComentario(receitaId, texto);
        return new ResponseEntity<>(novoComentario, HttpStatus.CREATED);
    }

    @GetMapping("/{receitaId}/comentarios")
    public ResponseEntity<List<Comentario>> listarComentarios(@PathVariable Integer receitaId) {
        List<Comentario> comentarios = comentarioService.listarPorReceita(receitaId);
        return ResponseEntity.ok(comentarios);
    }

    @PostMapping("/{receitaId}/classificar")
    public ResponseEntity<Classificacao> classificarReceita(
            @PathVariable Integer receitaId,
            @RequestBody Map<String, Integer> request) {

        Integer nota = request.get("nota");
        Classificacao novaClassificacao = classificacaoService.classificar(receitaId, nota);
        return ResponseEntity.ok(novaClassificacao);
    }

    @PostMapping("/{receitaId}/salvar")
    public ResponseEntity<Void> salvarReceita(@PathVariable Integer receitaId) {
        receitaService.salvarReceita(receitaId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{receitaId}/salvar")
    public ResponseEntity<Void> removerReceitaSalva(@PathVariable Integer receitaId) {
        receitaService.removerReceitaSalva(receitaId);
        return ResponseEntity.noContent().build();
    }


}
