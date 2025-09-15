package com.receitasdespensa.receitas_despensa_backend.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name="usuarios")
public class Usuario implements UserDetails{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Para este projeto, não temos papéis (roles) como ADMIN, USER, etc.
        // Então retornamos uma lista vazia.
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.senha; // O Spring Security usará este método para pegar a senha
    }

    @Override
    public String getUsername() {
        return this.email; // Usaremos o email como "username" para o login
    }

    // Os métodos abaixo podem retornar 'true' por enquanto
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
