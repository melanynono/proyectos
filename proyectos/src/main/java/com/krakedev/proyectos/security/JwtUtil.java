package com.krakedev.proyectos.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtil {

    private static final String CLAVE_SECRETA = "clave_secreta_123";

    private static final String EMISOR = "krakedevBackend";

    private static final long TIEMPO_EXPIRACION = 3600000;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public String generarToken(String username, String rol) {

        Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA);

        long tiempoActual = System.currentTimeMillis();

        Date fechaExpiracion = new Date(tiempoActual + TIEMPO_EXPIRACION);

        String tokenGenerado = JWT.create()
                .withIssuer(EMISOR)
                .withSubject(username)
                .withIssuedAt(new Date(tiempoActual))
                .withExpiresAt(fechaExpiracion)
                .withClaim("rol", rol)
                .sign(algoritmo);

        return tokenGenerado;
    }

    public DecodedJWT validarToken(String token) {

        try {

            Algorithm algoritmo = Algorithm.HMAC256(CLAVE_SECRETA);

            JWTVerifier verificador = JWT.require(algoritmo)
                    .withIssuer(EMISOR)
                    .build();

            DecodedJWT tokenDecodificado = verificador.verify(token);

            return tokenDecodificado;

        } catch (Exception e) {

            logger.error("Error de validación del Token: " + e.getMessage());

            return null;
        }
    }
}
