package com.receitasdespensa.receitas_despensa_backend.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ReceitaResponseDTO {
    private Integer id;
    private String titulo;
    private String descricao;
    private String modoPreparo;
    private String categoria;
    private String dieta;
    private LocalDateTime dataCriacao;
    private UsuarioDTO usuario;
    private List<ReceitaIngredienteDTO> ingredientes;

    private Double mediaAvaliacoes;
}