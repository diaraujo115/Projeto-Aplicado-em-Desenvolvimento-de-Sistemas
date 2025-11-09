package com.receitasdespensa.receitas_despensa_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

import java.util.List;
import java.util.Map;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdamamResponseDTO {

    // Agora vamos capturar a lista de ingredientes
    @JsonProperty("ingredients")
    private List<IngredientDetail> ingredients;

    @JsonProperty("yield")
    private Double yield;
}