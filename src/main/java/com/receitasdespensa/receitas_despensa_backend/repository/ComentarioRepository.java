package com.receitasdespensa.receitas_despensa_backend.repository;

import com.receitasdespensa.receitas_despensa_backend.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    List<Comentario> findByReceitaId(Integer receitaId);
}
