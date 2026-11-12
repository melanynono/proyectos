
package com.krakedev.proyectos.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtil {

    private static final String EMISOR = "krakedevBackend";

    @Value("${jwt.secret}")
    private String claveSecreta;

    @Value("${jwt.expiration-ms:3600000}")
    private long expiracionMs;

    public String generarToken(String username, String rol) {

        Algorithm algoritmo = Algorithm.HMAC256(claveSecreta);

        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(
            fechaActual.getTime() + expiracionMs
        );

        return JWT.create()
            .withIssuer(EMISOR)
            .withSubject(username)
            .withClaim("rol", rol)
            .withIssuedAt(fechaActual)
            .withExpiresAt(fechaExpiracion)
            .sign(algoritmo);
    }

    public DecodedJWT verificarToken(String token) {

        Algorithm algoritmo = Algorithm.HMAC256(claveSecreta);

        JWTVerifier verificador = JWT.require(algoritmo)
            .withIssuer(EMISOR)
            .build();

        return verificador.verify(token);
    }
}
