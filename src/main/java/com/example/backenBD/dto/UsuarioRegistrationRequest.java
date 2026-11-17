package com.example.backenBD.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UsuarioRegistrationRequest {

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @NotBlank(message = "El rol es obligatorio (DOCENTE o ESTUDIANTE)")
    private String rol;

    private String cursoAsignado;
    private String asignaturaAsignada;
}