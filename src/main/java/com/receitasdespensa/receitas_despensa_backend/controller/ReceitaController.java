package com.receitasdespensa.receitas_despensa_backend.controller;

import com.receitasdespensa.receitas_despensa_backend.dto.InformacaoNutricionalDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Classificacao;
import com.receitasdespensa.receitas_despensa_backend.model.Comentario;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import com.receitasdespensa.receitas_despensa_backend.service.ClassificacaoService;
import com.receitasdespensa.receitas_despensa_backend.service.ComentarioService;
import com.receitasdespensa.receitas_despensa_backend.service.EdamamService;
import com.receitasdespensa.receitas_despensa_backend.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/receitas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ClassificacaoService classificacaoService;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private EdamamService edamamService;

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

    @GetMapping("/recomendadas")
    public ResponseEntity<List<Receita>> getReceitasRecomendadas(
            @RequestParam List<Integer> ingredientes) {

        List<Receita> receitasRecomendadas = receitaService.recomendarPorIngredientes(ingredientes);
        return ResponseEntity.ok(receitasRecomendadas);
    }

    @GetMapping("/{id}/informacoes-nutricionais")
    public ResponseEntity<InformacaoNutricionalDTO> getInformacoesNutricionais(@PathVariable Integer id) {
        // Busca a receita completa com os ingredientes
        Optional<Receita> receitaOpt = receitaService.buscarPorId(id)
                .map(dto -> receitaRepository.findByIdWithIngredientes(dto.getId()).orElse(null)); // Converte DTO de volta para Entidade para pegar os ingredientes

        if (receitaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Formata a lista de ingredientes para o padrão da Edamam (ex: "1 cup flour")
        List<String> ingredientesParaApi = receitaOpt.get().getIngredientes().stream()
                .map(ri -> ri.getQuantidade() + " " + ri.getUnidade() + " " + ri.getIngrediente().getNome())
                .collect(Collectors.toList());

        if (ingredientesParaApi.isEmpty()) {
            // Retorna um objeto vazio ou uma mensagem de erro apropriada
            return ResponseEntity.badRequest().build();
        }

        InformacaoNutricionalDTO info = edamamService.getInformacoesNutricionais(ingredientesParaApi);

        if (info == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build(); // MSG006
        }

        return ResponseEntity.ok(info);
    }

    @GetMapping("/{receitaId}/minha-classificacao")
    public ResponseEntity<Map<String, Integer>> getMinhaClassificacao(@PathVariable Integer receitaId) {
        Optional<Classificacao> classificacaoOpt = classificacaoService.buscarMinhaClassificacao(receitaId);

        if (classificacaoOpt.isPresent()) {
            // Retorna um JSON simples: { "nota": X }
            return ResponseEntity.ok(Map.of("nota", classificacaoOpt.get().getNota()));
        } else {
            // Se o usuário ainda não classificou, retorna not found ou um objeto vazio
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{receitaId}/is-salva")
    public ResponseEntity<Map<String, Boolean>> isReceitaSalva(@PathVariable Integer receitaId) {
        boolean salva = receitaService.isReceitaSalvaPeloUsuarioLogado(receitaId);
        // Retorna um JSON simples: { "salva": true/false }
        return ResponseEntity.ok(Map.of("salva", salva));
    }
}
