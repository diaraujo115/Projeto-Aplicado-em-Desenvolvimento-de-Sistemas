package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.*;
import com.receitasdespensa.receitas_despensa_backend.model.*;
import com.receitasdespensa.receitas_despensa_backend.repository.ClassificacaoRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.IngredienteRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.ReceitaRepository;
import com.receitasdespensa.receitas_despensa_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EdamamService edamamService;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<Receita> listarTodas(String categoria, String dieta) {
        return receitaRepository.findByFilters(categoria, dieta);
    }

    public Optional<ReceitaResponseDTO> buscarPorId(Integer id) {
        Optional<Receita> receitaOpt = receitaRepository.findByIdWithIngredientes(id);

        if (receitaOpt.isEmpty()) {
            return Optional.empty();
        }

        Receita receita = receitaOpt.get();

        Double media = classificacaoRepository.findAverageNotaByReceitaId(id);

        ReceitaResponseDTO dto = new ReceitaResponseDTO();
        dto.setId(receita.getId());
        dto.setTitulo(receita.getTitulo());
        dto.setDescricao(receita.getDescricao());
        dto.setModoPreparo(receita.getModoPreparo());
        dto.setCategoria(receita.getCategoria());
        dto.setDieta(receita.getDieta());
        dto.setDataCriacao(receita.getDataCriacao());
        dto.setInformacaoNutricional(receita.getInformacaoNutricional());
        dto.setImagemUrl(receita.getImagemUrl());

        if (media != null) {
            dto.setMediaAvaliacoes(Math.round(media * 100.0) / 100.0);
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(receita.getUsuario().getId());
        usuarioDTO.setNome(receita.getUsuario().getNome());
        dto.setUsuario(usuarioDTO);

        List<ReceitaIngredienteDTO> ingredientesDTO = receita.getIngredientes().stream().map(ri -> {
            ReceitaIngredienteDTO riDto = new ReceitaIngredienteDTO();
            riDto.setNomeIngrediente(ri.getIngrediente().getNome());
            riDto.setQuantidade(ri.getQuantidade());
            riDto.setUnidade(ri.getUnidade());
            riDto.setIngredienteId(ri.getIngrediente().getId());
            return riDto;
        }).collect(Collectors.toList());
        dto.setIngredientes(ingredientesDTO);

        return Optional.of(dto);
    }

    public Receita criar(Receita receita, MultipartFile imagem) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        receita.setUsuario(usuarioLogado);


        if (receita.getIngredientes() != null) {
            for (ReceitaIngrediente ingredienteDaReceita : receita.getIngredientes()) {
                ingredienteDaReceita.setReceita(receita);
            }
        }

        if (imagem != null && !imagem.isEmpty()) {
            String filename = fileStorageService.save(imagem);
            receita.setImagemUrl(filename);
        }

        calcularEPreencherNutrientes(receita);

        return receitaRepository.save(receita);
    }

    public void deletar(Integer id) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!receita.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você não é o autor desta receita.");
        }

        receitaRepository.delete(receita);
    }

    public void salvarReceita(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));


        usuarioLogado.getReceitasSalvas().add(receita);

        usuarioRepository.save(usuarioLogado);
    }

    public void removerReceitaSalva(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        Receita receita = receitaRepository.findById(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        usuarioLogado.getReceitasSalvas().remove(receita);

        usuarioRepository.save(usuarioLogado);
    }

    public List<Receita> recomendarPorIngredientes(List<Integer> idsIngredientes) {
        if (idsIngredientes == null || idsIngredientes.isEmpty()) {
            return List.of();
        }
        return receitaRepository.findReceitasByIngredientes(idsIngredientes);
    }

    public boolean isReceitaSalvaPeloUsuarioLogado(Integer receitaId) {
        Usuario usuarioPrincipal = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuarioLogado = usuarioRepository.findById(usuarioPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return usuarioLogado.getReceitasSalvas().stream()
                .anyMatch(receita -> receita.getId().equals(receitaId));
    }

    public List<Receita> listarMinhasReceitas() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return receitaRepository.findByUsuarioId(usuarioLogado.getId());
    }

    public List<Receita> buscarPorTitulo(String termoDeBusca) {
        return receitaRepository.findByTituloContainingIgnoreCase(termoDeBusca);
    }

    public Receita atualizar(Integer receitaId, ReceitaUpdateDTO dto, MultipartFile imagem) {
        Receita receita = receitaRepository.findByIdWithIngredientes(receitaId)
                .orElseThrow(() -> new RuntimeException("Receita não encontrada!"));

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!receita.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você não é o autor desta receita.");
        }

        if (imagem != null && !imagem.isEmpty()) {
            fileStorageService.delete(receita.getImagemUrl());

            String newFilename = fileStorageService.save(imagem);
            receita.setImagemUrl(newFilename);
        }

        receita.setTitulo(dto.getTitulo());
        receita.setDescricao(dto.getDescricao());
        receita.setCategoria(dto.getCategoria());

        receita.setModoPreparo(dto.getModoPreparo());

        Map<Integer, IngredienteUpdateDTO> ingredientesDtoMap = dto.getIngredientes().stream()
                .collect(Collectors.toMap(IngredienteUpdateDTO::getIngredienteId, Function.identity()));

        receita.getIngredientes().removeIf(receitaIngrediente -> {
            int id = receitaIngrediente.getIngrediente().getId();
            if (ingredientesDtoMap.containsKey(id)) {
                IngredienteUpdateDTO updateDto = ingredientesDtoMap.get(id);
                receitaIngrediente.setQuantidade(updateDto.getQuantidade());
                receitaIngrediente.setUnidade(updateDto.getUnidade());
                ingredientesDtoMap.remove(id);
                return false;
            } else {
                return true;
            }
        });

        for (IngredienteUpdateDTO newIngredienteDto : ingredientesDtoMap.values()) {
            Ingrediente ingrediente = ingredienteRepository.findById(newIngredienteDto.getIngredienteId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado!"));
            ReceitaIngrediente novoReceitaIngrediente = new ReceitaIngrediente();
            novoReceitaIngrediente.setReceita(receita);
            novoReceitaIngrediente.setIngrediente(ingrediente);
            novoReceitaIngrediente.setQuantidade(newIngredienteDto.getQuantidade());
            novoReceitaIngrediente.setUnidade(newIngredienteDto.getUnidade());
            receita.getIngredientes().add(novoReceitaIngrediente);
        }
        calcularEPreencherNutrientes(receita);
        return receitaRepository.save(receita);
    }

    private void calcularEPreencherNutrientes(Receita receita) {
        // --- Início da Lógica Movida ---
        List<String> ingredientesParaApi = new ArrayList<>();
        boolean isReceitaVegana = true;
        boolean isReceitaVegetariana = true;

        if (receita.getIngredientes() != null) {
            for (ReceitaIngrediente ingredienteDaReceita : receita.getIngredientes()) {
                // Busca o ingrediente completo do banco
                Ingrediente ingCompleto = ingredienteRepository.findById(ingredienteDaReceita.getIngrediente().getId())
                        .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado: " + ingredienteDaReceita.getIngrediente().getId()));

                if (!ingCompleto.isVegano()) isReceitaVegana = false;
                if (!ingCompleto.isVegetariano()) isReceitaVegetariana = false;

                // Monta a string traduzida para a API Edamam
                String qtd = ingredienteDaReceita.getQuantidade();
                String unidadeEn = edamamService.traduzirUnidade(ingredienteDaReceita.getUnidade());
                String nomeEn = (ingCompleto.getNomeEn() != null) ? ingCompleto.getNomeEn() : ingCompleto.getNome();

                ingredientesParaApi.add(qtd + " " + unidadeEn + " " + nomeEn);
            }
        }

        InformacaoNutricionalDTO infoDto = edamamService.getInformacoesNutricionais(ingredientesParaApi);
        List<String> tagsDeDieta = new ArrayList<>();

        if (isReceitaVegana) tagsDeDieta.add("Vegana");
        else if (isReceitaVegetariana) tagsDeDieta.add("Vegetariana");

        if (infoDto != null) {
            // Pega a info nutricional existente ou cria uma nova
            InformacaoNutricional infoEntity = receita.getInformacaoNutricional();
            if (infoEntity == null) {
                infoEntity = new InformacaoNutricional();
            }

            double porcoes = (infoDto.getYield() != null && infoDto.getYield() > 0) ? infoDto.getYield() : 1.0;
            infoEntity.setPorcoes((int) porcoes);

            // ... (seta todos os campos: calorias, proteinas, carboidratos, gorduras, etc.) ...
            infoEntity.setCalorias(infoDto.getCalorias());
            infoEntity.setProteinas(infoDto.getProteinas());
            infoEntity.setCarboidratos(infoDto.getCarboidratos());
            infoEntity.setGorduras(infoDto.getGorduras());
            infoEntity.setFibra(infoDto.getFibra());
            infoEntity.setAcucar(infoDto.getAcucar());
            infoEntity.setSodio(infoDto.getSodio());
            infoEntity.setGorduraSaturada(infoDto.getGorduraSaturada());


            double caloriasPorPorcao = infoDto.getCalorias() / porcoes;
            double carbsPorPorcao = infoDto.getCarboidratos() / porcoes;
            double sodioPorPorcao = infoDto.getSodio() / porcoes;
            double acucarPorPorcao = infoDto.getAcucar() / porcoes;
            double gorduraPorPorcao = infoDto.getGorduras() / porcoes;

            if (caloriasPorPorcao <= 400) {
                tagsDeDieta.add("Baixo Calórico");
            }

            if (carbsPorPorcao <= 20) {
                tagsDeDieta.add("Baixo carboidrato");
            }

            if (sodioPorPorcao <= 500) {
                tagsDeDieta.add("Baixo Sódio");
            }

            if (acucarPorPorcao <= 10) {
                tagsDeDieta.add("Baixo Açúcar");
            }

            if (gorduraPorPorcao <= 15) {
                tagsDeDieta.add("Baixa Gordura");
            }

            infoEntity.setReceita(receita);
            receita.setInformacaoNutricional(infoEntity);
        }

        if (tagsDeDieta.isEmpty()) tagsDeDieta.add("Nenhuma");
        receita.setDieta(String.join(", ", tagsDeDieta));

    }

    public Set<String> getTagsDeDietaDisponiveis() {

        List<Receita> receitas = receitaRepository.findAll();


        return receitas.stream()

                .map(Receita::getDieta)
                .filter(dieta -> dieta != null && !dieta.isEmpty())
                .flatMap(dieta -> Arrays.stream(dieta.split(",")))
                .map(String::trim)
                .filter(tag -> !tag.equalsIgnoreCase("Nenhuma"))
                .collect(Collectors.toSet());
    }
}