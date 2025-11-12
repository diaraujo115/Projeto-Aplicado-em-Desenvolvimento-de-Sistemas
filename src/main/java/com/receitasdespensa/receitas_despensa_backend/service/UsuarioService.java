package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.LoginRequestDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioDTO;
import com.receitasdespensa.receitas_despensa_backend.dto.UsuarioUpdateDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Receita;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;
import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public Usuario cadastrar(Usuario usuario) {
        // Regra de negócio: Criptografar a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Salva o usuário no banco de dados usando o repositório
        return usuarioRepository.save(usuario);
    }

    // MÉTODO DE AUTENTICAÇÃO
    public String autenticar(LoginRequestDTO loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email ou senha inválidos"));

        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Email ou senha inválidos");
        }

        if (passwordEncoder.matches(loginRequest.getSenha(), usuario.getPassword())) {
            return jwtService.gerarToken(usuario);
        } else {
            throw new UsernameNotFoundException("Email ou senha inválidos");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));


        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + username);
        }

        return usuario;
    }

    public Usuario atualizarPerfil(UsuarioUpdateDTO dto) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuarioParaAtualizar = usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco de dados."));


        if (StringUtils.hasText(dto.getNome())) {
            usuarioParaAtualizar.setNome(dto.getNome());
        }

        if (StringUtils.hasText(dto.getSenha())) {
            usuarioParaAtualizar.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return usuarioRepository.save(usuarioParaAtualizar);
    }

    public void deletarPerfil() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuarioParaDeletar = usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Em vez de deletar, apenas marcamos como inativo
        usuarioParaDeletar.setAtivo(false);

        usuarioRepository.save(usuarioParaDeletar);
    }

    public Set<Receita> getMinhasReceitasSalvas() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca o usuário com a coleção já carregada (eager)
        Optional<Usuario> usuarioOpt = usuarioRepository.findByIdWithReceitasSalvas(usuarioLogado.getId());

        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get().getReceitasSalvas();
        }
        return Collections.emptySet(); // Retorna um conjunto vazio se algo der errado
    }

    public UsuarioDTO getMeuPerfil() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca o usuário "vivo" para garantir dados atualizados
        Usuario usuario = usuarioRepository.findById(usuarioLogado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        // Intencionalmente não retornamos email ou senha
        return dto;
    }
}
