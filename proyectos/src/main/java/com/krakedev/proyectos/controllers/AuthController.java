
package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {

        Usuario usuarioGuardado = usuarioService.registrar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            Map.of(
                "mensaje", "Usuario registrado correctamente",
                "username", usuarioGuardado.getUsername(),
                "rol", usuarioGuardado.getRol()
            )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {

        Usuario usuarioAutenticado = usuarioService.autenticar(
            usuario.getUsername(),
            usuario.getPassword()
        );

        if (usuarioAutenticado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("mensaje", "Usuario o contraseña incorrectos")
            );
        }

        return ResponseEntity.ok(
            Map.of(
                "mensaje", "Autenticación exitosa",
                "username", usuarioAutenticado.getUsername(),
                "rol", usuarioAutenticado.getRol()
            )
        );
    }
}
