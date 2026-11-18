package com.example.backenBD.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * MANEJADOR GLOBAL DE EXCEPCIONES.
 * Intercepta cualquier error de validacion o regla de negocio lanzada en los servicios
 * y devuelve un JSON ordenado con el codigo HTTP exacto (400, 404, 500) que espera Android.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura errores de anotaciones @NotBlank, @Size, @Email, @Pattern en los DTOs -> HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        body.put("errors", errors);
        body.put("message", "Por favor complete todos los campos obligatorios correctamente");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Captura errores de validacion de negocio (RUT invalido, permisos denegados, duplicados) -> HTTP 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Captura cualquier otro error inesperado en el servidor -> HTTP 500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 500);
        body.put("message", "Error al procesar la solicitud: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
