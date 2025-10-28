package com.receitasdespensa.receitas_despensa_backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

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

    private boolean ativo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "receitas_salvas", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "id_usuario"), // Coluna que se refere a esta entidade (Usuario)
            inverseJoinColumns = @JoinColumn(name = "id_receita") // Coluna que se refere à outra entidade (Receita)
    )
    @JsonIgnore // Essencial para evitar loops
    private Set<Receita> receitasSalvas;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
