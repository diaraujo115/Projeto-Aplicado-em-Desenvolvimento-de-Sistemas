package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaIngredienteDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.ReceitaIngrediente;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.ClassificacaoRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    // READ (Listar todas as receitas)
    public List<Receita> listarTodas() {
        return receitaRepository.findAll();
    }

    // READ (Buscar uma receita por ID)
//    public Optional<Receita> buscarPorId(Integer id) {
//        return receitaRepository.findById(id);
//    }

    public Optional<ReceitaResponseDTO> buscarPorId(Integer id) {
        // Usamos o método que já garante o carregamento dos ingredientes
        Optional<Receita> receitaOpt = receitaRepository.findByIdWithIngredientes(id);

        if (receitaOpt.isEmpty()) {
            return Optional.empty();
        }

        Receita receita = receitaOpt.get();

        // Calcula a média de avaliações usando nosso novo método
        Double media = classificacaoRepository.findAverageNotaByReceitaId(id);

        // Mapeia a entidade para o DTO
        ReceitaResponseDTO dto = new ReceitaResponseDTO();
        dto.setId(receita.getId());
        dto.setTitulo(receita.getTitulo());
        dto.setDescricao(receita.getDescricao());
        dto.setModoPreparo(receita.getModoPreparo());
        dto.setCategoria(receita.getCategoria());
        dto.setDieta(receita.getDieta());
        dto.setDataCriacao(receita.getDataCriacao());

        // Seta a média (arredondando para 2 casas decimais, se não for nula)
        if (media != null) {
            dto.setMediaAvaliacoes(Math.round(media * 100.0) / 100.0);
        }

        // Mapeia o usuário
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(receita.getUsuario().getId());
        usuarioDTO.setNome(receita.getUsuario().getNome());
        dto.setUsuario(usuarioDTO);

        // Mapeia a lista de ingredientes
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


    // CREATE (Criar uma nova receita)
    public Receita criar(Receita receita) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        receita.setUsuario(usuarioLogado);

        if (receita.getIngredientes() != null) {
            for (ReceitaIngrediente ingredienteDaReceita : receita.getIngredientes()) {
                ingredienteDaReceita.setReceita(receita);
            }
        }

        Receita receitaSalva = receitaRepository.save(receita);

        return receitaRepository.findByIdWithIngredientes(receitaSalva.getId()).get();


    }

    // DELETE (Deletar uma receita)
    public void deletar(Integer id) {
        // Futuramente, adicionaremos uma verificação para ver se o usuário logado é o dono da receita.
        receitaRepository.deleteById(id);
    }
}