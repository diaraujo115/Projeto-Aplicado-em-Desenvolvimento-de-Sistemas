package com.receitasdespensa.receitas_despensa_backend.repository;
import com.receitasdespensa.receitas_despensa_backend.model.Classificacao;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Integer> {

    Optional<Classificacao> findByUsuarioAndReceita(Usuario usuario, Receita receita);

    @Query("SELECT AVG(c.nota) FROM Classificacao c WHERE c.receita.id = :receitaId")
    Double findAverageNotaByReceitaId(@Param("receitaId") Integer receitaId);

    Optional<Classificacao> findByUsuarioIdAndReceitaId(Integer usuarioId, Integer receitaId);
}
