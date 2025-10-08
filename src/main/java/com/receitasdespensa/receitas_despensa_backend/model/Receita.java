package com.receitasdespensa.receitas_despensa_backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "receitas")
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receita")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo; // [cite: 207]

    @Lob // Usado para campos de texto longos
    @Column(nullable = false)
    private String descricao; // [cite: 208]

    @Lob
    @Column(name = "modo_preparo", nullable = false)
    private String modoPreparo; // [cite: 209]

    @Column(length = 100)
    private String categoria; // [cite: 210]

    @Column(length = 50)
    private String dieta; // [cite: 211]

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao; // [cite: 213]

    // Relacionamento: Muitas receitas podem pertencer a um usuário.
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_usuario", nullable = false) // [cite: 214]
    private Usuario usuario;

    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceitaIngrediente> ingredientes;

    @PrePersist // Executa este método antes de salvar a entidade no banco
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
    }
}