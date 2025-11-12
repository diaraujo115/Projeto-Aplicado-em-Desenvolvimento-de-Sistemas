package com.receitasdespensa.receitas_despensa_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.receitasdespensa.receitas_despensa_backend.dto.InformacaoNutricionalDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaUpdateDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/receitas")

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


    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<Receita> criar(
            @RequestParam("receita") String receitaJson,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        Receita receita = objectMapper.readValue(receitaJson, Receita.class);

        Receita novaReceita = receitaService.criar(receita, imagem);
        return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Receita>> listarTodas(
            // @RequestParam(required = false) torna os parâmetros opcionais
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String dieta) {

        List<Receita> receitas = receitaService.listarTodas(categoria, dieta);
        return ResponseEntity.ok(receitas);
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


    @GetMapping("/{receitaId}/minha-classificacao")
    public ResponseEntity<Map<String, Integer>> getMinhaClassificacao(@PathVariable Integer receitaId) {
        Optional<Classificacao> classificacaoOpt = classificacaoService.buscarMinhaClassificacao(receitaId);

        if (classificacaoOpt.isPresent()) {

            return ResponseEntity.ok(Map.of("nota", classificacaoOpt.get().getNota()));
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{receitaId}/is-salva")
    public ResponseEntity<Map<String, Boolean>> isReceitaSalva(@PathVariable Integer receitaId) {
        boolean salva = receitaService.isReceitaSalvaPeloUsuarioLogado(receitaId);
        // Retorna um JSON simples: { "salva": true/false }
        return ResponseEntity.ok(Map.of("salva", salva));
    }

    @GetMapping("/minhas-receitas")
    public ResponseEntity<List<Receita>> getMinhasReceitas() {
        List<Receita> receitas = receitaService.listarMinhasReceitas();
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Receita>> buscarReceitas(
            @RequestParam("q") String query) {

        List<Receita> receitas = receitaService.buscarPorTitulo(query);
        return ResponseEntity.ok(receitas);
    }

    @GetMapping("/{id}/informacoes-nutricionais")
    public ResponseEntity<InformacaoNutricionalDTO> getInformacoesNutricionais(@PathVariable Integer id) {

        Optional<Receita> receitaOpt = receitaRepository.findByIdWithIngredientes(id); // Busca a receita com todos os dados

        if (receitaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<String> ingredientesParaApi = receitaOpt.get().getIngredientes().stream()
                .map(ri -> {
                    String qtd = ri.getQuantidade();
                    String unidadeEn = edamamService.traduzirUnidade(ri.getUnidade());
                    String nomeEn = ri.getIngrediente().getNomeEn();

                    if (nomeEn == null || nomeEn.trim().isEmpty()) {
                        nomeEn = ri.getIngrediente().getNome();
                    }

                    return qtd + " " + unidadeEn + " " + nomeEn;
                })
                .collect(Collectors.toList());

        if (ingredientesParaApi.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        InformacaoNutricionalDTO info = edamamService.getInformacoesNutricionais(ingredientesParaApi);

        if (info == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.ok(info);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Receita> atualizar(
            @PathVariable Integer id,
            @RequestParam("receita") String receitaUpdateDTOJson,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        ReceitaUpdateDTO dto = objectMapper.readValue(receitaUpdateDTOJson, ReceitaUpdateDTO.class);

        try {
            Receita receitaAtualizada = receitaService.atualizar(id, dto, imagem);
            return ResponseEntity.ok(receitaAtualizada);
        } catch (RuntimeException e) {

            if (e.getMessage().contains("Acesso negado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            if (e.getMessage().contains("Receita não encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dietas")
    public ResponseEntity<Set<String>> getDietasDisponiveis() {
        Set<String> dietas = receitaService.getTagsDeDietaDisponiveis();
        return ResponseEntity.ok(dietas);
    }
}
