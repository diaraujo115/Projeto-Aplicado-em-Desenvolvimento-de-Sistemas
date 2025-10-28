package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaIngredienteDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.ReceitaResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.ReceitaIngrediente;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.ClassificacaoRepository;
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

    public void salvarReceita(Integer receitaId) {
        // Pega o usuário 'principal' (pode estar detached) do contexto
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // CORREÇÃO: Busca uma instância 'fresca' e 'viva' (managed) do usuário e da receita
        // A anotação @Transactional garante que a sessão do banco estará aberta aqui.
        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        // Agora podemos inicializar e modificar a coleção lazy sem erros
        usuarioLogado.getReceitasSalvas().add(receita);

        // O save não é estritamente necessário aqui dentro de uma transação,
        // mas é uma boa prática para clareza.
        usuarioRepository.save(usuarioLogado);
    }

    public void removerReceitaSalva(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Mesma lógica de busca para garantir que estamos trabalhando com entidades 'vivas'
        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        usuarioLogado.getReceitasSalvas().remove(receita);

        usuarioRepository.save(usuarioLogado);
    }

    public List<Receita> recomendarPorIngredientes(List<Integer> idsIngredientes) {
        if (idsIngredientes == null || idsIngredientes.isEmpty()) {
            return List.of(); // Retorna uma lista vazia se nenhum ingrediente for fornecido
        }
        return receitaRepository.findReceitasByIngredientes(idsIngredientes);
    }

    public boolean isReceitaSalvaPeloUsuarioLogado(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca a instância gerenciada do usuário para acessar a coleção lazy
        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // Verifica se a coleção 'receitasSalvas' contém uma receita com o ID fornecido
        // O '.stream().anyMatch()' é uma forma eficiente de verificar sem carregar todas as receitas salvas
        return usuarioLogado.getReceitasSalvas().stream()
                .anyMatch(receita -> receita.getId().equals(receitaId));
    }
}