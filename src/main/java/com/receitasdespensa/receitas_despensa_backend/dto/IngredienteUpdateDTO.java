package com.receitasdespensa.receitas_despensa_backend.dto;

import lombok.Data;

@Data
public class IngredienteUpdateDTO {
    private Integer ingredienteId; // O ID do Ingrediente
    private String quantidade;
    private String unidade;
}