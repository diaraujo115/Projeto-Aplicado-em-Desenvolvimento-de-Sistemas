package com.receitasdespensa.receitas_despensa_backend.service;
import com.receitasdespensa.receitas_despensa_backend.model.Classificacao;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.ClassificacaoRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassificacaoService {

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    public Classificacao classificar(Integer receitaId, Integer nota) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        // Verifica se já existe uma classificação para este usuário e receita
        Optional<Classificacao> classificacaoExistente = classificacaoRepository.findByUsuarioAndReceita(usuarioLogado, receita);

        Classificacao classificacao;
        if (classificacaoExistente.isPresent()) {
            // Se já existe, apenas atualiza a nota (RN005)
            classificacao = classificacaoExistente.get();
            classificacao.setNota(nota);
        } else {
            // Se não existe, cria uma nova classificação
            classificacao = new Classificacao();
            classificacao.setUsuario(usuarioLogado);
            classificacao.setReceita(receita);
            classificacao.setNota(nota);
        }

        return classificacaoRepository.save(classificacao);
    }

    public Optional<Classificacao> buscarMinhaClassificacao(Integer receitaId) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Usamos os IDs para a busca
        return classificacaoRepository.findByUsuarioIdAndReceitaId(usuarioLogado.getId(), receitaId);
    }
}