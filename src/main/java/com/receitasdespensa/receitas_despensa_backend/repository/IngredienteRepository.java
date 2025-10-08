package com.receitasdespensa.receitas_despensa_backend.repository;
import com.receitasdespensa.receitas_despensa_backend.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {
}