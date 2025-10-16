package com.receitasdespensa.receitas_despensa_backend.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NutrienteDTO {
    private String label;
    private Double quantity;
    private String unit;
}