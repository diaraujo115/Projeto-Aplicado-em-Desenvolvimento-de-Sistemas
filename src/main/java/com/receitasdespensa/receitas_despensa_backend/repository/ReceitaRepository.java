package com.receitasdespensa.receitas_despensa_backend.repository;

import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Integer> {

    @Query("SELECT r FROM Receita r LEFT JOIN FETCH r.ingredientes ri LEFT JOIN FETCH ri.ingrediente WHERE r.id = :id")
    Optional<Receita> findByIdWithIngredientes(@Param("id") Integer id);


    @Query("SELECT r FROM Receita r JOIN r.ingredientes ri WHERE ri.ingrediente.id IN :idsIngredientes GROUP BY r.id ORDER BY COUNT(r) DESC")
    List<Receita> findReceitasByIngredientes(@Param("idsIngredientes") List<Integer> idsIngredientes);


    @Query("SELECT r FROM Receita r WHERE " +
            "(:categoria IS NULL OR :categoria = '' OR r.categoria = :categoria) AND " +
            "(:dieta IS NULL OR :dieta = '' OR r.dieta = :dieta)")
    List<Receita> findByFilters(
            @Param("categoria") String categoria,
            @Param("dieta") String dieta
    );

    List<Receita> findByUsuarioId(Integer usuarioId);

    List<Receita> findByTituloContainingIgnoreCase(String titulo);
}


