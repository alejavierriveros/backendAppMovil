package com.example.backenBD.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id; // RUT normalizado (ej: "12345678-9")

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena; // Cifrada con BCrypt

    @Column(nullable = false)
    private String rol; // "DOCENTE" o "ESTUDIANTE"

    private String cursoAsignado;
    private String asignaturaAsignada;
}