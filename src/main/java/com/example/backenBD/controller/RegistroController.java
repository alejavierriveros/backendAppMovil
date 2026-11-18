package com.example.backenBD.controller;

import com.example.backenBD.dto.RegistroDTOs;
import com.example.backenBD.service.RegistroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints para los Registros de Aprendizaje de los estudiantes.
 */
@RestController
@RequestMapping("/api/registros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistroController {

    private final RegistroService registroService;

    // GET /api/registros -> Lista registros filtrando opcionalmente por bitacoraId y/o estudianteId
    @GetMapping
    public ResponseEntity<List<RegistroDTOs.RegistroResponse>> listar(
            @RequestParam(required = false) Long bitacoraId,
            @RequestParam(required = false) String estudianteId) {
        return ResponseEntity.ok(registroService.listarRegistros(bitacoraId, estudianteId));
    }

    // POST /api/registros -> Envía un nuevo registro de aprendizaje (solo rol ESTUDIANTE)
    @PostMapping
    public ResponseEntity<RegistroDTOs.RegistroResponse> crear(@Valid @RequestBody RegistroDTOs.RegistroCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroService.crearRegistro(request));
    }

    // PUT /api/registros/{id} -> Edita un registro propio mientras esté en estado PENDIENTE
    @PutMapping("/{id}")
    public ResponseEntity<RegistroDTOs.RegistroResponse> editarEstudiante(
            @PathVariable Long id,
            @Valid @RequestBody RegistroDTOs.RegistroUpdateRequest request) {
        return ResponseEntity.ok(registroService.editarRegistroEstudiante(id, request));
    }

    // PATCH /api/registros/{id}/revision -> Evalúa y retroalimenta un registro (solo el docente creador)
    @PatchMapping("/{id}/revision")
    public ResponseEntity<RegistroDTOs.RegistroResponse> revisar(
            @PathVariable Long id,
            @Valid @RequestBody RegistroDTOs.RevisionRequest request) {
        return ResponseEntity.ok(registroService.revisarRegistro(id, request));
    }

    // DELETE /api/registros/{id} -> Elimina un registro mientras no haya sido revisado por el docente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam(required = false) String estudianteId) {
        registroService.eliminarRegistro(id, estudianteId);
        return ResponseEntity.noContent().build();
    }
}