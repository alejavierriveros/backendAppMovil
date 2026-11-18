package com.example.backenBD.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * CLASE CONTENEDORA DE DTOs PARA BITACORAS.
 * Define los formatos JSON de entrada (crear/editar) y salida (respuesta) para /api/bitacoras.
 */
public class BitacoraDTOs {

    /**
     * JSON que envia el docente para crear o editar una bitacora
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class BitacoraCreateRequest {
        @NotBlank(message = "El título es obligatorio")
        @Size(min = 5, message = "El título debe tener al menos 5 caracteres")
        private String titulo;

        @NotBlank(message = "La descripción es obligatoria")
        @Size(min = 10, message = "La descripción debe tener al menos 10 caracteres")
        private String descripcion;

        @NotBlank(message = "La fecha es obligatoria")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "La fecha debe tener formato dd/MM/yyyy")
        private String fecha;

        @NotBlank(message = "El curso es obligatorio")
        private String curso;

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "^(?i)(BORRADOR|PUBLICADA|CERRADA)$", message = "El estado debe ser BORRADOR, PUBLICADA o CERRADA")
        private String estado;

        @NotBlank(message = "El ID del docente es obligatorio")
        private String docenteId;
    }

    /**
     * JSON que devuelve el backend con los datos de una bitacora
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class BitacoraResponse {
        private Long id;
        private String titulo;
        private String descripcion;
        private String fecha;
        private String curso;
        private String estado;
        private String docenteId;
    }
}
