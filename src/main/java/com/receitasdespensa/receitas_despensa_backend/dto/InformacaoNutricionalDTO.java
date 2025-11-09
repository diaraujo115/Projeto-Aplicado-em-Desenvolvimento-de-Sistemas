package com.receitasdespensa.receitas_despensa_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InformacaoNutricionalDTO {

    private Double calorias;
    private Double proteinas;
    private Double carboidratos;
    private Double gorduras;
    private Double fibra;
    private Double acucar;
    private Double sodio;
    private Double gorduraSaturada;

    private Double yield;
}