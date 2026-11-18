package com.example.backenBD.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * CLASE CONTENEDORA DE DTOs PARA INICIO DE SESION.
 * Los DTO (Data Transfer Objects) definen la estructura exacta del JSON que viaja
 * entre la aplicacion Android y el servidor Spring Boot.
 */
public class LoginDTOs {

    /**
     * JSON que envia Android al hacer POST /api/auth/login
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "El RUT es obligatorio")
        private String rut;

        @NotBlank(message = "La contraseña es obligatoria")
        private String contrasena;
    }

    /**
     * JSON que responde el backend cuando el login es exitoso (HTTP 200 OK)
     */
    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class LoginResponse {
        private String token;
        private UsuarioDTO usuario;
    }

    /**
     * Datos publicos del perfil del usuario (nunca incluye la contrasena por seguridad)
     */
    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class UsuarioDTO {
        private String id;
        private String rut;
        private String nombre;
        private String correo;
        private String rol;
        private String cursoAsignado;
        private String asignaturaAsignada;
    }
}
