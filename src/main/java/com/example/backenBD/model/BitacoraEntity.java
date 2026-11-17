package com.example.backenBD.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bitacoras")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BitacoraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private String fecha; // Formato dd/MM/yyyy

    @Column(nullable = false)
    private String curso;

    @Column(nullable = false)
    private String estado; // "BORRADOR", "PUBLICADA", "CERRADA"

    @Column(name = "docente_id", nullable = false)
    private String docenteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", insertable = false, updatable = false)
    private UsuarioEntity docente;
}