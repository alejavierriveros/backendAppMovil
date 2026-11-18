package com.example.backenBD.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * UTILIDAD DE TOKENS JWT (Version simple).
 * Genera el token firmado que se entrega a Android al iniciar sesion.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Crea un token JWT que contiene el RUT del usuario, su ROL y la fecha de expiracion
    public String generateToken(String rut, String rol) {
        return Jwts.builder()
                .subject(rut)
                .claim("rol", rol)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    // Decodifica la clave secreta de application.properties para firmar el token
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
