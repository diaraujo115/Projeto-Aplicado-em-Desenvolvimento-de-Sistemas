package com.receitasdespensa.receitas_despensa_backend.service;

import com.receitasdespensa.receitas_despensa_backend.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails; // Import
import java.util.function.Function; // Import

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Tempo de expiração do token: 10 horas em milissegundos
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    @Value("${jwt.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();

        // Pode adicionar informações extras do usuário no token se quiser
        // claims.put("nome", usuario.getNome());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail()) // Define o "dono" do token (o email do usuário)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de criação
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina o token
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}