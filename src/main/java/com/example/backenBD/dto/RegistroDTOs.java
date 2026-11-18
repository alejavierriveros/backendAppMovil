package com.example.backenBD.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * CLASE CONTENEDORA DE DTOs PARA REGISTROS DE APRENDIZAJE.
 * Define los JSON para:
 * - Crear un registro por el estudiante (RegistroCreateRequest)
 * - Editar un registro por el estudiante (RegistroUpdateRequest)
 * - Revisar y retroalimentar por el docente (RevisionRequest)
 * - Devolver datos al frontend (RegistroResponse)
 */
public class RegistroDTOs {

    /**
     * JSON que envia el estudiante al crear un registro (POST /api/registros)
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class RegistroCreateRequest {
        @NotNull(message = "El ID de la bitácora es obligatorio")
        private Long bitacoraId;

        @NotBlank(message = "El ID del estudiante es obligatorio")
        private String estudianteId;

        @NotBlank(message = "La observación es obligatoria")
        @Size(min = 10, message = "La observación debe tener al menos 10 caracteres")
        private String observacion;

        private String fotoUri;

        @NotBlank(message = "La fecha es obligatoria")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "La fecha debe tener formato dd/MM/yyyy")
        private String fecha;

        private String estado = "PENDIENTE";
    }

    /**
     * JSON que envia el estudiante cuando edita su propio registro (PUT /api/registros/{id})
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class RegistroUpdateRequest {
        @NotBlank(message = "El ID del estudiante es obligatorio para verificar autoría")
        private String estudianteId;

        @NotBlank(message = "La observación es obligatoria")
        @Size(min = 10, message = "La observación debe tener al menos 10 caracteres")
        private String observacion;

        private String fotoUri;
    }

    /**
     * JSON que envia el docente al evaluar un registro (PATCH /api/registros/{id}/revision)
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class RevisionRequest {
        @NotBlank(message = "El ID del docente es obligatorio para verificar permisos")
        private String docenteId;

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "^(?i)(PENDIENTE|REVISADO)$", message = "El estado debe ser PENDIENTE o REVISADO")
        private String estado;

        private String retroalimentacionDocente;
    }

    /**
     * JSON de respuesta con toda la informacion del registro
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class RegistroResponse {
        private Long id;
        private Long bitacoraId;
        private String estudianteId;
        private String observacion;
        private String fotoUri;
        private String fecha;
        private String estado;
        private String retroalimentacionDocente;
    }
}
