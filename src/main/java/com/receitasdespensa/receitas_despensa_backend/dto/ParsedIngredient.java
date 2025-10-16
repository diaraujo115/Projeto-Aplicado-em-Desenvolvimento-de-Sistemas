package com.receitasdespensa.receitas_despensa_backend.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedIngredient {
    @JsonProperty("nutrients")
    public Map<String, NutrienteDTO> nutrients;
}