package com.receitasdespensa.receitas_despensa_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientDetail {
    @JsonProperty("parsed")
    public List<ParsedIngredient> parsed;
}