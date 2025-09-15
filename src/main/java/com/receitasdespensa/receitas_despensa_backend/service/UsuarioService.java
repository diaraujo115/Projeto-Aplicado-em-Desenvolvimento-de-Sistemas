package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.dto.LoginRequestDTO;
import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import com.receitasdespensa.receitas_despensa_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

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
        // 1. Busca o usuário pelo email no banco de dados
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email ou senha inválidos"));

        // 2. Compara a senha enviada no login com a senha criptografada no banco
        if (passwordEncoder.matches(loginRequest.getSenha(), usuario.getPassword())) {
            // 3. Se as senhas baterem, gera e retorna o token JWT
            return jwtService.gerarToken(usuario);
        } else {
            // 4. Se não baterem, lança uma exceção
            throw new UsernameNotFoundException("Email ou senha inválidos");
        }
    }
}