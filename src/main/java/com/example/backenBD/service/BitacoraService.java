package com.example.backenBD.service;

import com.example.backenBD.dto.BitacoraDTOs;
import com.example.backenBD.mappers.AppMappers;
import com.example.backenBD.model.BitacoraEntity;
import com.example.backenBD.model.UsuarioEntity;
import com.example.backenBD.repository.BitacoraRepository;
import com.example.backenBD.repository.UsuarioRepository;
import com.example.backenBD.util.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que contiene toda la lógica de negocio y validaciones para las Bitácoras Escolares.
 */
@Service
@RequiredArgsConstructor
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * LISTA Y FILTRA LAS BITÁCORAS:
     * Usa filtrarBitacoras() de BitacoraRepository para filtrar por docenteId, curso y/o estado.
     */
    @Transactional(readOnly = true)
    public List<BitacoraDTOs.BitacoraResponse> listarBitacoras(String docenteId, String curso, String estado) {
        String docenteNorm = (docenteId != null && !docenteId.isBlank()) ? RutUtils.normalizar(docenteId) : null;
        String cursoFiltro = (curso != null && !curso.isBlank()) ? curso.trim() : null;
        String estadoFiltro = (estado != null && !estado.isBlank()) ? estado.trim().toUpperCase() : null;

        return bitacoraRepository.filtrarBitacoras(docenteNorm, cursoFiltro, estadoFiltro)
                .stream()
                .map(AppMappers::toBitacoraResponse)
                .toList();
    }

    /**
     * BUSCA UNA BITÁCORA POR SU ID:
     * Si no existe en la base de datos, lanza IllegalArgumentException (HTTP 400).
     */
    @Transactional(readOnly = true)
    public BitacoraDTOs.BitacoraResponse buscarBitacoraPorId(Long id) {
        BitacoraEntity bitacora = bitacoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bitácora no encontrada con ID: " + id));
        return AppMappers.toBitacoraResponse(bitacora);
    }

    /**
     * CREA UNA NUEVA BITÁCORA:
     * Valida que el usuario exista, tenga rol DOCENTE y cree la bitácora únicamente para su curso asignado.
     */
    @Transactional
    public BitacoraDTOs.BitacoraResponse crearBitacora(BitacoraDTOs.BitacoraCreateRequest req) {
        String rutDocente = RutUtils.normalizar(req.getDocenteId());
        UsuarioEntity docente = usuarioRepository.findById(rutDocente)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado con RUT: " + rutDocente));

        if (!"DOCENTE".equalsIgnoreCase(docente.getRol())) {
            throw new IllegalArgumentException("Solo un usuario con rol DOCENTE puede crear bitácoras");
        }

        if (docente.getCursoAsignado() != null && !docente.getCursoAsignado().equalsIgnoreCase(req.getCurso().trim())) {
            throw new IllegalArgumentException("Un docente solo puede crear bitácoras para su propio curso (" + docente.getCursoAsignado() + ")");
        }

        BitacoraEntity nueva = BitacoraEntity.builder()
                .titulo(req.getTitulo().trim())
                .descripcion(req.getDescripcion().trim())
                .fecha(req.getFecha().trim())
                .curso(req.getCurso().trim())
                .estado(req.getEstado() != null ? req.getEstado().toUpperCase() : "PUBLICADA")
                .docenteId(rutDocente)
                .build();

        return AppMappers.toBitacoraResponse(bitacoraRepository.save(nueva));
    }

    /**
     * EDITA UNA BITÁCORA EXISTENTE:
     * Valida que la bitácora exista y que solo el docente creador pueda modificarla.
     */
    @Transactional
    public BitacoraDTOs.BitacoraResponse editarBitacora(Long id, BitacoraDTOs.BitacoraCreateRequest req) {
        BitacoraEntity bitacora = bitacoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bitácora no encontrada con ID: " + id));

        if (req.getDocenteId() != null && !req.getDocenteId().isBlank()) {
            String rutSolicitante = RutUtils.normalizar(req.getDocenteId());
            if (!bitacora.getDocenteId().equals(rutSolicitante)) {
                throw new IllegalArgumentException("No tiene permisos para editar la bitácora de otro docente");
            }
        }

        if (req.getTitulo() != null && !req.getTitulo().isBlank()) {
            bitacora.setTitulo(req.getTitulo().trim());
        }
        if (req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
            bitacora.setDescripcion(req.getDescripcion().trim());
        }
        if (req.getFecha() != null && !req.getFecha().isBlank()) {
            bitacora.setFecha(req.getFecha().trim());
        }
        if (req.getCurso() != null && !req.getCurso().isBlank()) {
            bitacora.setCurso(req.getCurso().trim());
        }
        if (req.getEstado() != null && !req.getEstado().isBlank()) {
            bitacora.setEstado(req.getEstado().toUpperCase());
        }

        return AppMappers.toBitacoraResponse(bitacoraRepository.save(bitacora));
    }

    /**
     * ELIMINA UNA BITÁCORA POR SU ID:
     * Valida que la bitácora exista y, si se envía docenteId, comprueba que sea el docente dueño.
     */
    @Transactional
    public void eliminarBitacora(Long id, String docenteId) {
        BitacoraEntity bitacora = bitacoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bitácora no encontrada con ID: " + id));

        if (docenteId != null && !docenteId.isBlank()) {
            String rutSolicitante = RutUtils.normalizar(docenteId);
            if (!bitacora.getDocenteId().equals(rutSolicitante)) {
                throw new IllegalArgumentException("No tiene permisos para eliminar la bitácora de otro docente");
            }
        }

        bitacoraRepository.delete(bitacora);
    }
}