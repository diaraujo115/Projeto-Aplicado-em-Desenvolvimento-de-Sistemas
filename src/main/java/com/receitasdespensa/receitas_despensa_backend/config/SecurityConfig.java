package com.receitasdespensa.receitas_despensa_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //para hashing de senhas.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF, pois não usaremos sessões/cookies para autenticação (comum em APIs REST)
                .csrf(csrf -> csrf.disable())

                // 2. Define as regras de autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite requisições POST para "/api/usuarios/cadastrar" sem autenticação
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/cadastrar").permitAll()

                        // (Opcional, mas já vamos deixar pronto para o futuro endpoint de login)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login").permitAll()

                        // Exige autenticação para todas as outras requisições
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}