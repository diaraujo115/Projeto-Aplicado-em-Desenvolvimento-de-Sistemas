package com.receitasdespensa.receitas_despensa_backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "classificacoes")
public class Classificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classificacao")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_receita", nullable = false)
    @JsonIgnore
    private Receita receita;

    @Column(nullable = false)
    private Integer nota; // A nota que o usuário deu (ex: 1 a 5)

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao;

    @PrePersist
    @PreUpdate
    public void prePersistUpdate() {
        dataAvaliacao = LocalDateTime.now();
    }
}
