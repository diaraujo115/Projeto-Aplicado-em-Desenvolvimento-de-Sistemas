package com.receitasdespensa.receitas_despensa_backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReceitaIngredienteDTO {
    private String nomeIngrediente;
    private String quantidade;
    private String unidade;
    private Integer ingredienteId;
}
