package com.example.backenBD.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class RegistroDTOs {

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
        private String fecha;

        private String estado = "PENDIENTE";
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class RevisionRequest {
        @NotBlank(message = "El estado es obligatorio")
        private String estado;
        private String retroalimentacionDocente;
    }

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