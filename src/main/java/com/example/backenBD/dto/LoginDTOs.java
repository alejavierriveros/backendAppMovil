package com.example.backenBD.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginDTOs {

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "El RUT es obligatorio")
        private String rut;

        @NotBlank(message = "La contraseña es obligatoria")
        private String contrasena;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    @Builder
    public static class LoginResponse {
        private String token;
        private UsuarioDTO usuario;
    }

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