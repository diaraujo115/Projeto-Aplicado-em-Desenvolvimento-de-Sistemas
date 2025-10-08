package com.receitasdespensa.receitas_despensa_backend.repository;

import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Integer> {

    @Query("SELECT r FROM Receita r LEFT JOIN FETCH r.ingredientes ri LEFT JOIN FETCH ri.ingrediente WHERE r.id = :id")
    Optional<Receita> findByIdWithIngredientes(@Param("id") Integer id);

}