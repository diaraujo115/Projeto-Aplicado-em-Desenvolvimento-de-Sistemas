package com.receitasdespensa.receitas_despensa_backend.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformacaoNutricionalDTO {

    private Double calorias;
    private Double proteinas;
    private Double carboidratos;
    private Double gorduras;

    @JsonProperty("totalNutrients")
    private void unpackTotalNutrients(Map<String, NutrienteDTO> totalNutrients) {
        if (totalNutrients.containsKey("ENERC_KCAL")) {
            this.calorias = totalNutrients.get("ENERC_KCAL").getQuantity();
        }
        if (totalNutrients.containsKey("PROCNT")) {
            this.proteinas = totalNutrients.get("PROCNT").getQuantity();
        }
        if (totalNutrients.containsKey("CHOCDF")) {
            this.carboidratos = totalNutrients.get("CHOCDF").getQuantity();
        }
        if (totalNutrients.containsKey("FAT")) {
            this.gorduras = totalNutrients.get("FAT").getQuantity();
        }
    }
}