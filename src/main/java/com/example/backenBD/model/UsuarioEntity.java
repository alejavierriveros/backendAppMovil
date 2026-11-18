package com.example.backenBD.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD USUARIO (Tabla 'usuarios' en MySQL).
 * Representa tanto a los DOCENTES como a los ESTUDIANTES del sistema.
 * Usa Lombok (@Getter, @Setter, @Builder) para generar automaticamente los getters, setters y constructores.
 */
@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UsuarioEntity {

    // Llave primaria (PK). Se guarda el RUT normalizado sin puntos (ej: "11111111-1")
    @Id
    @Column(name = "id", length = 15, nullable = false, unique = true)
    private String id;

    // RUT del usuario sin puntos y con guion (ej: "11111111-1"). No se puede repetir
    @Column(length = 15, nullable = false, unique = true)
    private String rut;

    // Nombre completo del docente o estudiante
    @Column(length = 100, nullable = false)
    private String nombre;

    // Correo electronico unico para cada usuario
    @Column(length = 120, nullable = false, unique = true)
    private String correo;

    // Contrasena encriptada con algoritmo BCrypt (nunca se guarda en texto plano)
    @Column(nullable = false)
    private String contrasena;

    // Rol del usuario dentro de la app: "DOCENTE" o "ESTUDIANTE"
    @Column(length = 20, nullable = false)
    private String rol;

    // Curso asignado al estudiante o al docente (ej: "6.º básico A")
    @Column(length = 50)
    private String cursoAsignado;

    // Asignatura que imparte el docente (ej: "Matemáticas"). En estudiantes suele ser null
    @Column(length = 80)
    private String asignaturaAsignada;
}
