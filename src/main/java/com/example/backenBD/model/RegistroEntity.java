package com.example.backenBD.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "registros")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RegistroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bitacora_id", nullable = false)
    private Long bitacoraId;

    @Column(name = "estudiante_id", nullable = false)
    private String estudianteId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String observacion;

    private String fotoUri;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String estado; // "PENDIENTE" o "REVISADO"

    @Column(columnDefinition = "TEXT")
    private String retroalimentacionDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bitacora_id", insertable = false, updatable = false)
    private BitacoraEntity bitacora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", insertable = false, updatable = false)
    private UsuarioEntity estudiante;
}