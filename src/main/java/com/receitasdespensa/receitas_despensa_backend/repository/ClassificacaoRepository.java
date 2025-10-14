package com.receitasdespensa.receitas_despensa_backend.repository;
import com.receitasdespensa.receitas_despensa_backend.model.Classificacao;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Integer> {

    Optional<Classificacao> findByUsuarioAndReceita(Usuario usuario, Receita receita);
}
