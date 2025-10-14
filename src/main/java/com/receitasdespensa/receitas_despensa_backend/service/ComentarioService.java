package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.model.Comentario;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.ComentarioRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ReceitaRepository receitaRepository; // Para buscar a receita

    public List<Comentario> listarPorReceita(Integer receitaId) {
        return comentarioRepository.findByReceitaId(receitaId);
    }

    public Comentario adicionarComentario(Integer receitaId, String texto) {
        // Pega o usuário logado
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca a receita pelo ID. Se não encontrar, lança uma exceção.
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        Comentario novoComentario = new Comentario();
        novoComentario.setTexto(texto);
        novoComentario.setReceita(receita);
        novoComentario.setUsuario(usuarioLogado);

        return comentarioRepository.save(novoComentario);
    }
}