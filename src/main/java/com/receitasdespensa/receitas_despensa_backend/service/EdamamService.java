package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.InformacaoNutricionalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EdamamService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${edamam.api.app-id}")
    private String appId;

    @Value("${edamam.api.app-key}")
    private String appKey;

    private final String API_URL = "https://api.edamam.com/api/nutrition-details";

    public InformacaoNutricionalDTO getInformacoesNutricionais(List<String> ingredientes) {
        String url = API_URL + "?app_id=" + appId + "&app_key=" + appKey;

        // A API da Edamam espera um JSON com uma chave "ingr" contendo um array de strings
        var requestBody = new Object() {
            public final List<String> ingr = ingredientes;
        };

        try {
            return restTemplate.postForObject(url, requestBody, InformacaoNutricionalDTO.class);
        } catch (Exception e) {
            // Lida com o fluxo de exceção FE6.1, caso a API esteja indisponível
            System.err.println("Erro ao chamar a API Edamam: " + e.getMessage());
            return null;
        }
    }
}