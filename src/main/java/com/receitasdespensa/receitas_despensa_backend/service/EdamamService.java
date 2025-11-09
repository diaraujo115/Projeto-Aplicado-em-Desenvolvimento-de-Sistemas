package com.receitasdespensa.receitas_despensa_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.receitasdespensa.receitas_despensa_backend.dto.EdamamResponseDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.InformacaoNutricionalDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.IngredientDetail;
import com.receitasdespensa.receitas_despensa_backend.dto.NutrienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EdamamService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${edamam.api.app-id}")
    private String appId;

    @Value("${edamam.api.app-key}")
    private String appKey;

    private final String API_URL = "https://api.edamam.com/api/nutrition-details";

    private static final Map<String, String> UNIDADE_TRADUCOES = Map.of(
            "gramas", "grams",
            "g", "g",
            "xícara", "cup",
            "xícaras", "cups",
            "unidade", "unit",
            "unidades", "units",
            "colher de sopa", "tablespoon",
            "colher de chá", "teaspoon",
            "lata", "can",
            "inteiro", "whole"
    );

    public InformacaoNutricionalDTO getInformacoesNutricionais(List<String> ingredientes) {
        String url = API_URL + "?app_id=" + appId + "&app_key=" + appKey;

        var requestBody = new Object() {
            public final List<String> ingr = ingredientes;
        };

        try {
            EdamamResponseDTO edamamResponse = restTemplate.postForObject(url, requestBody, EdamamResponseDTO.class);

            if (edamamResponse != null && edamamResponse.getIngredients() != null) {
                InformacaoNutricionalDTO infoFinal = new InformacaoNutricionalDTO();
                double totalCalorias = 0.0, totalProteinas = 0.0, totalCarboidratos = 0.0;
                double totalGorduras = 0.0, totalFibra = 0.0, totalAcucar = 0.0, totalSodio = 0.0, totalGorduraSaturada = 0.0;

                for (IngredientDetail detail : edamamResponse.getIngredients()) {
                    if (detail.parsed != null && !detail.parsed.isEmpty()) {
                        Map<String, NutrienteDTO> nutrientes = detail.parsed.get(0).nutrients;


                        if (nutrientes.containsKey("ENERC_KCAL")) totalCalorias += nutrientes.get("ENERC_KCAL").getQuantity();
                        if (nutrientes.containsKey("PROCNT")) totalProteinas += nutrientes.get("PROCNT").getQuantity();
                        if (nutrientes.containsKey("CHOCDF")) totalCarboidratos += nutrientes.get("CHOCDF").getQuantity();
                        if (nutrientes.containsKey("FAT")) totalGorduras += nutrientes.get("FAT").getQuantity();


                        if (nutrientes.containsKey("FIBTG")) totalFibra += nutrientes.get("FIBTG").getQuantity();
                        if (nutrientes.containsKey("SUGAR")) totalAcucar += nutrientes.get("SUGAR").getQuantity();
                        if (nutrientes.containsKey("NA")) totalSodio += nutrientes.get("NA").getQuantity();
                        if (nutrientes.containsKey("FASAT")) totalGorduraSaturada += nutrientes.get("FASAT").getQuantity();
                    }
                }

                infoFinal.setCalorias(totalCalorias);
                infoFinal.setProteinas(totalProteinas);
                infoFinal.setCarboidratos(totalCarboidratos);
                infoFinal.setGorduras(totalGorduras);

                infoFinal.setFibra(totalFibra);
                infoFinal.setAcucar(totalAcucar);
                infoFinal.setSodio(totalSodio);
                infoFinal.setGorduraSaturada(totalGorduraSaturada);

                return infoFinal;
            }

            return null;

        } catch (Exception e) {
            System.err.println("Erro ao chamar ou processar a resposta da API Edamam: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String traduzirUnidade(String unidadePt) {
        if (unidadePt == null) {
            return "";
        }

        return UNIDADE_TRADUCOES.getOrDefault(unidadePt.toLowerCase(), unidadePt);
    }
}