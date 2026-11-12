package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService usuarioService;
	private final JwtUtil jwtUtil;
	private final TokenBlacklistService blacklistService;

	public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil, TokenBlacklistService blacklistService) {

		this.usuarioService = usuarioService;
		this.jwtUtil = jwtUtil;
		this.blacklistService = blacklistService;
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {

		Usuario usuarioGuardado = usuarioService.registrar(usuario);

		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Usuario registrado correctamente",
				"username", usuarioGuardado.getUsername(), "rol", usuarioGuardado.getRol()));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario usuario) {

		Usuario usuarioAutenticado = usuarioService.autenticar(usuario.getUsername(), usuario.getPassword());

		if (usuarioAutenticado == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("mensaje", "Usuario o contraseña incorrectos"));
		}

		String token = jwtUtil.generarToken(usuarioAutenticado.getUsername(), usuarioAutenticado.getRol());

		return ResponseEntity.ok(Map.of("mensaje", "Autenticación exitosa", "username",
				usuarioAutenticado.getUsername(), "rol", usuarioAutenticado.getRol(), "token", token));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("mensaje", "Token no proporcionado o formato incorrecto"));
		}

		String token = authHeader.substring(7);

		DecodedJWT jwt = jwtUtil.validarToken(token);

		if (jwt == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "Token inválido o expirado"));
		}

		if (blacklistService.estaInvalidado(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "El token ya fue invalidado"));
		}

		blacklistService.invalidarToken(token);

		return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada correctamente"));
	}

	@GetMapping("/perfil")
	public ResponseEntity<?> perfil() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = authentication.getName();

		return ResponseEntity.ok(Map.of("mensaje", "Perfil del usuario autenticado", "username", username, "rol",
				authentication.getAuthorities()));
	}

}
