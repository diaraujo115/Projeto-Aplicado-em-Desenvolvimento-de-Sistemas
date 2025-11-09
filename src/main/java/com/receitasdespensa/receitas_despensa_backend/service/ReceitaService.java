package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.InformacaoNutricionalDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaIngredienteDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioDTO;
import com.receitasdespensa.receitas_despensa_backend.model.*;
import com.receitasdespensa.receitas_despensa_backend.repository.ClassificacaoRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.IngredienteRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EdamamService edamamService;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<Receita> listarTodas(String categoria, String dieta) {
        return receitaRepository.findByFilters(categoria, dieta);
    }

    public Optional<ReceitaResponseDTO> buscarPorId(Integer id) {
        // Usamos o método que já garante o carregamento dos ingredientes
        Optional<Receita> receitaOpt = receitaRepository.findByIdWithIngredientes(id);

        if (receitaOpt.isEmpty()) {
            return Optional.empty();
        }

        Receita receita = receitaOpt.get();

        Double media = classificacaoRepository.findAverageNotaByReceitaId(id);

        ReceitaResponseDTO dto = new ReceitaResponseDTO();
        dto.setId(receita.getId());
        dto.setTitulo(receita.getTitulo());
        dto.setDescricao(receita.getDescricao());
        dto.setModoPreparo(receita.getModoPreparo());
        dto.setCategoria(receita.getCategoria());
        dto.setDieta(receita.getDieta());
        dto.setDataCriacao(receita.getDataCriacao());
        dto.setInformacaoNutricional(receita.getInformacaoNutricional());

        if (media != null) {
            dto.setMediaAvaliacoes(Math.round(media * 100.0) / 100.0);
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(receita.getUsuario().getId());
        usuarioDTO.setNome(receita.getUsuario().getNome());
        dto.setUsuario(usuarioDTO);

        List<ReceitaIngredienteDTO> ingredientesDTO = receita.getIngredientes().stream().map(ri -> {
            ReceitaIngredienteDTO riDto = new ReceitaIngredienteDTO();
            riDto.setNomeIngrediente(ri.getIngrediente().getNome());
            riDto.setQuantidade(ri.getQuantidade());
            riDto.setUnidade(ri.getUnidade());
            return riDto;
        }).collect(Collectors.toList());
        dto.setIngredientes(ingredientesDTO);

        return Optional.of(dto);
    }

    public Receita criar(Receita receita) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        receita.setUsuario(usuarioLogado);


        List<String> ingredientesParaApi = new ArrayList<>();
        boolean isReceitaVegana = true;
        boolean isReceitaVegetariana = true;

        if (receita.getIngredientes() != null) {
            for (ReceitaIngrediente ingredienteDaReceita : receita.getIngredientes()) {
                ingredienteDaReceita.setReceita(receita);

                Ingrediente ingCompleto = ingredienteRepository.findById(ingredienteDaReceita.getIngrediente().getId())
                        .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado: " + ingredienteDaReceita.getIngrediente().getId()));

                if (!ingCompleto.isVegano()) {
                    isReceitaVegana = false;
                }
                if (!ingCompleto.isVegetariano()) {
                    isReceitaVegetariana = false;
                }

                String qtd = ingredienteDaReceita.getQuantidade();
                String unidadeEn = edamamService.traduzirUnidade(ingredienteDaReceita.getUnidade());
                String nomeEn = (ingCompleto.getNomeEn() != null) ? ingCompleto.getNomeEn() : ingCompleto.getNome(); // Fallback

                ingredientesParaApi.add(qtd + " " + unidadeEn + " " + nomeEn);
            }
        }

        InformacaoNutricionalDTO infoDto = edamamService.getInformacoesNutricionais(ingredientesParaApi);

        List<String> tagsDeDieta = new ArrayList<>();

        if (isReceitaVegana) {
            tagsDeDieta.add("Vegana");
        } else if (isReceitaVegetariana) {
            tagsDeDieta.add("Vegetariana");
        }

        if (infoDto != null) {
            InformacaoNutricional infoEntity = new InformacaoNutricional();

            // Define o número de porções (default = 1)
            double porcoes = (infoDto.getYield() != null && infoDto.getYield() > 0) ? infoDto.getYield() : 1.0;
            infoEntity.setPorcoes((int) porcoes);

            // Salva os totais
            infoEntity.setCalorias(infoDto.getCalorias());
            infoEntity.setProteinas(infoDto.getProteinas());
            infoEntity.setCarboidratos(infoDto.getCarboidratos());
            infoEntity.setGorduras(infoDto.getGorduras());
            infoEntity.setFibra(infoDto.getFibra());
            infoEntity.setAcucar(infoDto.getAcucar());
            infoEntity.setSodio(infoDto.getSodio());
            infoEntity.setGorduraSaturada(infoDto.getGorduraSaturada());

            // --- Lógica de Tags Nutricionais (Baseada em Porção) ---
            // (Estes são valores de exemplo, você pode ajustá-los)
            double caloriasPorPorcao = infoDto.getCalorias() / porcoes;
            double carbsPorPorcao = infoDto.getCarboidratos() / porcoes;
            double sodioPorPorcao = infoDto.getSodio() / porcoes;
            double acucarPorPorcao = infoDto.getAcucar() / porcoes;
            double gorduraPorPorcao = infoDto.getGorduras() / porcoes;

            if (caloriasPorPorcao <= 400) {
                tagsDeDieta.add("Baixo Calórico");
            }
            if (carbsPorPorcao <= 20) {
                tagsDeDieta.add("Low Carb");
            }
            if (sodioPorPorcao <= 500) {
                tagsDeDieta.add("Baixo Sódio");
            }
            if (acucarPorPorcao <= 10) {
                tagsDeDieta.add("Baixo Açúcar");
            }
            if (gorduraPorPorcao <= 15) {
                tagsDeDieta.add("Baixa Gordura");
            }
            // ... (Adicione mais regras se quiser)

            // Faz a ligação bidirecional
            infoEntity.setReceita(receita);
            receita.setInformacaoNutricional(infoEntity);
        }

        // 6. Define o campo 'dieta' da receita com as tags calculadas
        // O usuário ainda seleciona a CATEGORIA, mas nós definimos a DIETA
        if (tagsDeDieta.isEmpty()) {
            tagsDeDieta.add("Nenhuma"); // Valor padrão
        }
        receita.setDieta(String.join(", ", tagsDeDieta)); // Ex: "Vegetariana, Low Carb"

        // 7. Salva a Receita (e o CascadeType.ALL salvará a InfoNutricional junto)
        return receitaRepository.save(receita);
    }

    public void deletar(Integer id) {
        // Futuramente, adicionaremos uma verificação para ver se o usuário logado é o dono da receita.
        receitaRepository.deleteById(id);
    }

    public void salvarReceita(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));


        usuarioLogado.getReceitasSalvas().add(receita);

        usuarioRepository.save(usuarioLogado);
    }

    public void removerReceitaSalva(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        usuarioLogado.getReceitasSalvas().remove(receita);

        usuarioRepository.save(usuarioLogado);
    }

    public List<Receita> recomendarPorIngredientes(List<Integer> idsIngredientes) {
        if (idsIngredientes == null || idsIngredientes.isEmpty()) {
            return List.of();
        }
        return receitaRepository.findReceitasByIngredientes(idsIngredientes);
    }

    public boolean isReceitaSalvaPeloUsuarioLogado(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return usuarioLogado.getReceitasSalvas().stream()
                .anyMatch(receita -> receita.getId().equals(receitaId));
    }

    public List<Receita> listarMinhasReceitas() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return receitaRepository.findByUsuarioId(usuarioLogado.getId());
    }

    public List<Receita> buscarPorTitulo(String termoDeBusca) {
        return receitaRepository.findByTituloContainingIgnoreCase(termoDeBusca);
    }
}