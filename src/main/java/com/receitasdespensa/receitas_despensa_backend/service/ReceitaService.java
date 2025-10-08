package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.ReceitaIngrediente;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    // READ (Listar todas as receitas)
    public List<Receita> listarTodas() {
        return receitaRepository.findAll();
    }

    // READ (Buscar uma receita por ID)
    public Optional<Receita> buscarPorId(Integer id) {
        return receitaRepository.findById(id);
    }

    // CREATE (Criar uma nova receita)
    public Receita criar(Receita receita) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        receita.setUsuario(usuarioLogado);

        if (receita.getIngredientes() != null) {
            for (ReceitaIngrediente ingredienteDaReceita : receita.getIngredientes()) {
                ingredienteDaReceita.setReceita(receita);
            }
        }

        Receita receitaSalva = receitaRepository.save(receita);

        return receitaRepository.findByIdWithIngredientes(receitaSalva.getId()).get();


    }

    // DELETE (Deletar uma receita)
    public void deletar(Integer id) {
        // Futuramente, adicionaremos uma verificação para ver se o usuário logado é o dono da receita.
        receitaRepository.deleteById(id);
    }
}