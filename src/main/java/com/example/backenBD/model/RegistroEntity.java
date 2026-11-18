package com.example.backenBD.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTIDAD REGISTRO DE APRENDIZAJE (Tabla 'registros' en MySQL).
 * Representa el avance, respuesta o evidencia fotografica que envia un ESTUDIANTE
 * dentro de una bitacora creada por su docente.
 */
@Entity
@Table(name = "registros")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RegistroEntity {

    // ID numerico autoincremental generado por MySQL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de la bitacora a la que pertenece este registro
    @Column(name = "bitacora_id", nullable = false)
    private Long bitacoraId;

    // RUT normalizado del estudiante que envio este registro
    @Column(name = "estudiante_id", length = 15, nullable = false)
    private String estudianteId;

    // Texto con lo que aprendio el estudiante o sus observaciones
    @Column(nullable = false, columnDefinition = "TEXT")
    private String observacion;

    // Ruta o URI de la foto adjunta como evidencia (opcional)
    @Column(length = 500)
    private String fotoUri;

    // Fecha en la que el estudiante envio su registro (dd/MM/yyyy)
    @Column(length = 15, nullable = false)
    private String fecha;

    // Estado de revision: "PENDIENTE" (recien enviado) o "REVISADO" (ya evaluado por el docente)
    @Column(length = 20, nullable = false)
    private String estado;

    // Comentario escrito por el docente al revisar el registro del estudiante
    @Column(columnDefinition = "TEXT")
    private String retroalimentacionDocente;

    // Relacion Muchos-a-Uno hacia la bitacora padre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bitacora_id", insertable = false, updatable = false)
    private BitacoraEntity bitacora;

    // Relacion Muchos-a-Uno hacia el estudiante autor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", insertable = false, updatable = false)
    private UsuarioEntity estudiante;
}
