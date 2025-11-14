package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.model.Comentario;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.ComentarioRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ReceitaRepository receitaRepository; // Para buscar a receita

    public List<Comentario> listarPorReceita(Integer receitaId) {
        return comentarioRepository.findByReceitaId(receitaId);
    }

    public Comentario adicionarComentario(Integer receitaId, String texto) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        Comentario novoComentario = new Comentario();
        novoComentario.setTexto(texto);
        novoComentario.setReceita(receita);
        novoComentario.setUsuario(usuarioLogado);

        return comentarioRepository.save(novoComentario);
    }

    private void verificarDono(Comentario comentario) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!comentario.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Acesso negado: Usuário não é o dono do comentário.");
        }
    }

    public Comentario atualizarComentario(Integer comentarioId, String novoTexto) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado!"));

        verificarDono(comentario);

        comentario.setTexto(novoTexto);
        return comentarioRepository.save(comentario);
    }


    public void deletarComentario(Integer comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado!"));

        verificarDono(comentario);

        comentarioRepository.delete(comentario);
    }
}