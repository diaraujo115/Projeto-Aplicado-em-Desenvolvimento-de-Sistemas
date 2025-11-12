package com.receitasdespensa.receitas_despensa_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReceitaUpdateDTO {
    private String titulo;
    private String descricao;
    private String modoPreparo;
    private String categoria;

    private List<IngredienteUpdateDTO> ingredientes;
}