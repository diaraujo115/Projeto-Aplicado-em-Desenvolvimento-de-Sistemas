package com.receitasdespensa.receitas_despensa_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InformacaoNutricionalDTO {
    // Apenas os campos que queremos na nossa resposta final
    private Double calorias;
    private Double proteinas;
    private Double carboidratos;
    private Double gorduras;
}