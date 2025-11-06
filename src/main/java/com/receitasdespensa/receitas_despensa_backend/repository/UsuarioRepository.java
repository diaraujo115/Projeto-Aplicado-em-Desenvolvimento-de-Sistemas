package com.receitasdespensa.receitas_despensa_backend.repository;


import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.receitasSalvas WHERE u.id = :usuarioId")
    Optional<Usuario> findByIdWithReceitasSalvas(@Param("usuarioId") Integer usuarioId);

}