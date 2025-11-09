package com.receitasdespensa.receitas_despensa_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Data
@Entity
@Table(name = "informacoes_nutricionais")
public class InformacaoNutricional implements Serializable {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_receita")
    @JsonIgnore // Evita loops
    private Receita receita;

    @Column(name = "calorias")
    private Double calorias;

    @Column(name = "proteinas")
    private Double proteinas;

    @Column(name = "carboidratos")
    private Double carboidratos;

    @Column(name = "gorduras")
    private Double gorduras;

    // Novos campos
    @Column(name = "fibra")
    private Double fibra;

    @Column(name = "acucar")
    private Double acucar;

    @Column(name = "sodio")
    private Double sodio;

    @Column(name = "gordura_saturada")
    private Double gorduraSaturada;
}