package com.example.backenBD.service;

import com.example.backenBD.dto.*;
import com.example.backenBD.mappers.AppMappers;
import com.example.backenBD.model.UsuarioEntity;
import com.example.backenBD.repository.UsuarioRepository;
import com.example.backenBD.security.JwtUtils;
import com.example.backenBD.util.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public LoginDTOs.LoginResponse login(LoginDTOs.LoginRequest request) {
        String rutNorm = RutUtils.normalizar(request.getRut());
        
        UsuarioEntity usuario = usuarioRepository.findByRut(rutNorm)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas o usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new IllegalArgumentException("Credenciales incorrectas o usuario no encontrado");
        }

        String token = jwtUtils.generateToken(usuario.getRut(), usuario.getRol());
        return new LoginDTOs.LoginResponse(token, AppMappers.toUsuarioDTO(usuario));
    }

    public LoginDTOs.UsuarioDTO registrarUsuario(UsuarioRegistrationRequest req) {
        if (!RutUtils.esValido(req.getRut())) {
            throw new IllegalArgumentException("El RUT ingresado no es válido según Módulo 11");
        }

        String rutNorm = RutUtils.normalizar(req.getRut());

        if (usuarioRepository.existsByRut(rutNorm)) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este RUT");
        }
        if (usuarioRepository.existsByCorreo(req.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este correo");
        }

        UsuarioEntity nuevoUsuario = UsuarioEntity.builder()
                .id(rutNorm)
                .rut(rutNorm)
                .nombre(req.getNombre())
                .correo(req.getCorreo())
                .contrasena(passwordEncoder.encode(req.getContrasena()))
                .rol(req.getRol().toUpperCase())
                .cursoAsignado(req.getCursoAsignado())
                .asignaturaAsignada(req.getAsignaturaAsignada())
                .build();

        usuarioRepository.save(nuevoUsuario);
        return AppMappers.toUsuarioDTO(nuevoUsuario);
    }
}