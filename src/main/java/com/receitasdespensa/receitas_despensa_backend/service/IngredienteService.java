package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.model.Ingrediente;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;


    public List<Ingrediente> listarTodas() {
        return ingredienteRepository.findAll();
    }

    // READ (Buscar uma receita por ID)
    public Optional<Ingrediente> buscarPorId(Integer id) {
        return ingredienteRepository.findById(id);
    }

    // CREATE (Criar uma nova receita)
    public Ingrediente criar(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    // DELETE (Deletar uma receita)
    public void deletar(Integer id) {
        ingredienteRepository.deleteById(id);
    }
}
