package com.example.backenBD.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD BITACORA (Tabla 'bitacoras' en MySQL).
 * Representa una actividad o tarea escolar creada por un DOCENTE para un curso especifico.
 */
@Entity
@Table(name = "bitacoras")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BitacoraEntity {

    // ID numerico autoincremental generado automaticamente por MySQL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Titulo de la bitacora o actividad
    @Column(length = 150, nullable = false)
    private String titulo;

    // Instrucciones o descripcion detallada de la actividad
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    // Fecha de la bitacora en formato dd/MM/yyyy
    @Column(length = 15, nullable = false)
    private String fecha;

    // Curso al que va dirigida la bitacora (ej: "6.º básico A")
    @Column(length = 50, nullable = false)
    private String curso;

    // Estado del ciclo de vida de la bitacora: "BORRADOR", "PUBLICADA" o "CERRADA"
    @Column(length = 20, nullable = false)
    private String estado;

    // Llave foranea (FK) que guarda el RUT normalizado del docente que creo la bitacora
    @Column(name = "docente_id", length = 15, nullable = false)
    private String docenteId;

    // Relacion Muchos-a-Uno con la tabla usuarios (solo lectura, se enlaza mediante docente_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", insertable = false, updatable = false)
    private UsuarioEntity docente;
}
