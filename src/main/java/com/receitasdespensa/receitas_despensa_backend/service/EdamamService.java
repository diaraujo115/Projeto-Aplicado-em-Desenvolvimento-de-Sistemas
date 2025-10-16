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

//    public InformacaoNutricionalDTO getInformacoesNutricionais(List<String> ingredientes) {
//        String url = API_URL + "?app_id=" + appId + "&app_key=" + appKey;
//
//        // A API da Edamam espera um JSON com uma chave "ingr" contendo um array de strings
//        var requestBody = new Object() {
//            public final List<String> ingr = ingredientes;
//        };
//
//        try {
//            return restTemplate.postForObject(url, requestBody, InformacaoNutricionalDTO.class);
//        } catch (Exception e) {
//            // Lida com o fluxo de exceção FE6.1, caso a API esteja indisponível
//            System.err.println("Erro ao chamar a API Edamam: " + e.getMessage());
//            return null;
//        }
//    }

    public InformacaoNutricionalDTO getInformacoesNutricionais(List<String> ingredientes) {
        String url = API_URL + "?app_id=" + appId + "&app_key=" + appKey;

        var requestBody = new Object() {
            public final List<String> ingr = ingredientes;
        };

        try {
            // Mapeia a resposta da Edamam para nosso novo DTO
            EdamamResponseDTO edamamResponse = restTemplate.postForObject(url, requestBody, EdamamResponseDTO.class);

            // Se a resposta e a lista de ingredientes não forem nulas
            if (edamamResponse != null && edamamResponse.getIngredients() != null) {
                InformacaoNutricionalDTO infoFinal = new InformacaoNutricionalDTO();
                // Inicializa os contadores
                double totalCalorias = 0.0;
                double totalProteinas = 0.0;
                double totalCarboidratos = 0.0;
                double totalGorduras = 0.0;

                // Itera sobre cada ingrediente retornado pela API
                for (IngredientDetail detail : edamamResponse.getIngredients()) {
                    if (detail.parsed != null && !detail.parsed.isEmpty()) {
                        // Pega o mapa de nutrientes do primeiro item "parsed"
                        Map<String, NutrienteDTO> nutrientes = detail.parsed.get(0).nutrients;

                        // Soma os valores
                        if (nutrientes.containsKey("ENERC_KCAL")) {
                            totalCalorias += nutrientes.get("ENERC_KCAL").getQuantity();
                        }
                        if (nutrientes.containsKey("PROCNT")) {
                            totalProteinas += nutrientes.get("PROCNT").getQuantity();
                        }
                        if (nutrientes.containsKey("CHOCDF")) {
                            totalCarboidratos += nutrientes.get("CHOCDF").getQuantity();
                        }
                        if (nutrientes.containsKey("FAT")) {
                            totalGorduras += nutrientes.get("FAT").getQuantity();
                        }
                    }
                }

                // Seta os totais calculados no nosso DTO de resposta final
                infoFinal.setCalorias(totalCalorias);
                infoFinal.setProteinas(totalProteinas);
                infoFinal.setCarboidratos(totalCarboidratos);
                infoFinal.setGorduras(totalGorduras);

                return infoFinal;
            }

            return null;

        } catch (Exception e) {
            System.err.println("Erro ao chamar ou processar a resposta da API Edamam: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}