package com.receitasdespensa.receitas_despensa_backend.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "receita_ingredientes")
public class ReceitaIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_receita") // [cite: 189]
    @JsonIgnore
    private Receita receita;

    @ManyToOne
    @JoinColumn(name = "id_ingrediente") // [cite: 190]
    private Ingrediente ingrediente;

    @Column(nullable = false, length = 50) //
    private String quantidade;

    @Column(nullable = false, length = 30) //
    private String unidade;
}