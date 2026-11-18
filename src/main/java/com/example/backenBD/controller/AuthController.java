package com.example.backenBD.controller;

import com.example.backenBD.dto.*;
import com.example.backenBD.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLADOR REST DE AUTENTICACION Y REGISTRO.
 * Expone los endpoints HTTP para que Android inicie sesion y registre nuevos usuarios.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Endpoint POST http://localhost:8080/api/auth/login -> Inicia sesion y devuelve JWT + Usuario
    @PostMapping("/auth/login")
    public ResponseEntity<LoginDTOs.LoginResponse> login(@Valid @RequestBody LoginDTOs.LoginRequest request) {
        return ResponseEntity.ok(authService.iniciarSesion(request));
    }

    // Endpoint POST http://localhost:8080/api/usuarios -> Crea un nuevo docente o estudiante (HTTP 201)
    @PostMapping("/usuarios")
    public ResponseEntity<LoginDTOs.UsuarioDTO> registrar(@Valid @RequestBody UsuarioRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarUsuario(request));
    }
}
