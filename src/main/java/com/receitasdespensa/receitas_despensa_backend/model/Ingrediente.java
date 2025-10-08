package com.receitasdespensa.receitas_despensa_backend.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ingredientes")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingrediente")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;
}