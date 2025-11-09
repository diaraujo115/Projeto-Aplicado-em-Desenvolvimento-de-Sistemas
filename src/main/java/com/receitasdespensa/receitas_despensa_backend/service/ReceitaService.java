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
        if (receita.getIngredientes() != null) {
            for (ReceitaIngrediente ingredienteDaReceita : receita.getIngredientes()) {
                ingredienteDaReceita.setReceita(receita);
            }
        }

        List<String> ingredientesParaApi = receita.getIngredientes().stream()
                .map(ri -> {
                    String qtd = ri.getQuantidade();
                    String unidadeEn = edamamService.traduzirUnidade(ri.getUnidade());
                    Ingrediente ing = ingredienteRepository.findById(ri.getIngrediente().getId()).orElse(null);
                    String nomeEn = (ing != null && ing.getNomeEn() != null) ? ing.getNomeEn() : ri.getIngrediente().getNome(); // Fallback

                    return qtd + " " + unidadeEn + " " + nomeEn;
                })
                .collect(Collectors.toList());

        InformacaoNutricionalDTO infoDto = edamamService.getInformacoesNutricionais(ingredientesParaApi);

        if (infoDto != null) {
            InformacaoNutricional infoEntity = new InformacaoNutricional();
            infoEntity.setCalorias(infoDto.getCalorias());
            infoEntity.setProteinas(infoDto.getProteinas());
            infoEntity.setCarboidratos(infoDto.getCarboidratos());
            infoEntity.setGorduras(infoDto.getGorduras());
            infoEntity.setFibra(infoDto.getFibra());
            infoEntity.setAcucar(infoDto.getAcucar());
            infoEntity.setSodio(infoDto.getSodio());
            infoEntity.setGorduraSaturada(infoDto.getGorduraSaturada());

            infoEntity.setReceita(receita);
            receita.setInformacaoNutricional(infoEntity);
        }

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