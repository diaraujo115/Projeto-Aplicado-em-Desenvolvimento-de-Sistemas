package com.receitasdespensa.receitas_despensa_backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "receitas")
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receita")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Lob
    @Column(nullable = false)
    private String descricao;

    @Lob
    @Column(name = "modo_preparo", nullable = false)
    private String modoPreparo;

    @Column(length = 100)
    private String categoria;

    @Column(length = 50)
    private String dieta;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceitaIngrediente> ingredientes;

    @ManyToMany(mappedBy = "receitasSalvas", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Usuario> salvadoPorUsuarios;

    @OneToOne(mappedBy = "receita", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private InformacaoNutricional informacaoNutricional;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
    }

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receita receita = (Receita) o;
        return Objects.equals(id, receita.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}