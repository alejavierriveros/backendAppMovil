package com.example.backenBD.controller;

import com.example.backenBD.dto.BitacoraDTOs;
import com.example.backenBD.service.BitacoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints CRUD para las Bitácoras Escolares.
 */
@RestController
@RequestMapping("/api/bitacoras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BitacoraController {

    private final BitacoraService bitacoraService;

    // GET /api/bitacoras -> Lista bitácoras con filtros opcionales por docenteId, curso o estado
    @GetMapping
    public ResponseEntity<List<BitacoraDTOs.BitacoraResponse>> listar(
            @RequestParam(required = false) String docenteId,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(bitacoraService.listarBitacoras(docenteId, curso, estado));
    }

    // GET /api/bitacoras/{id} -> Obtiene el detalle de una bitácora específica
    @GetMapping("/{id}")
    public ResponseEntity<BitacoraDTOs.BitacoraResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bitacoraService.buscarBitacoraPorId(id));
    }

    // POST /api/bitacoras -> Crea una nueva bitácora (solo rol DOCENTE)
    @PostMapping
    public ResponseEntity<BitacoraDTOs.BitacoraResponse> crear(@Valid @RequestBody BitacoraDTOs.BitacoraCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bitacoraService.crearBitacora(request));
    }

    // PUT /api/bitacoras/{id} -> Edita una bitácora existente (solo el docente creador)
    @PutMapping("/{id}")
    public ResponseEntity<BitacoraDTOs.BitacoraResponse> actualizar(
            @PathVariable Long id,
            @RequestBody BitacoraDTOs.BitacoraCreateRequest request) {
        return ResponseEntity.ok(bitacoraService.editarBitacora(id, request));
    }

    // DELETE /api/bitacoras/{id} -> Elimina una bitácora (y en cascada sus registros asociados)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            @RequestParam(required = false) String docenteId) {
        bitacoraService.eliminarBitacora(id, docenteId);
        return ResponseEntity.noContent().build();
    }
}