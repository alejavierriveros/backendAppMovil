package com.example.backenBD.service;

import com.example.backenBD.dto.RegistroDTOs;
import com.example.backenBD.mappers.AppMappers;
import com.example.backenBD.model.BitacoraEntity;
import com.example.backenBD.model.RegistroEntity;
import com.example.backenBD.model.UsuarioEntity;
import com.example.backenBD.repository.BitacoraRepository;
import com.example.backenBD.repository.RegistroRepository;
import com.example.backenBD.repository.UsuarioRepository;
import com.example.backenBD.util.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que gestiona los Registros de Aprendizaje (avances de alumnos y revisiones de docentes).
 */
@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final BitacoraRepository bitacoraRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * LISTA LOS REGISTROS DE APRENDIZAJE:
     * Usa filtrarRegistros() de RegistroRepository para filtrar por bitacoraId y/o estudianteId.
     */
    @Transactional(readOnly = true)
    public List<RegistroDTOs.RegistroResponse> listarRegistros(Long bitacoraId, String estudianteId) {
        String estNorm = (estudianteId != null && !estudianteId.isBlank()) ? RutUtils.normalizar(estudianteId) : null;
        return registroRepository.filtrarRegistros(bitacoraId, estNorm)
                .stream()
                .map(AppMappers::toRegistroResponse)
                .toList();
    }

    /**
     * CREA UN NUEVO REGISTRO DE APRENDIZAJE (ESTUDIANTE):
     * Valida que la bitácora exista, esté PUBLICADA, corresponda al curso del alumno y no esté duplicada.
     */
    @Transactional
    public RegistroDTOs.RegistroResponse crearRegistro(RegistroDTOs.RegistroCreateRequest req) {
        BitacoraEntity bitacora = bitacoraRepository.findById(req.getBitacoraId())
                .orElseThrow(() -> new IllegalArgumentException("Bitácora no encontrada con ID: " + req.getBitacoraId()));

        if (!"PUBLICADA".equalsIgnoreCase(bitacora.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden enviar registros a bitácoras en estado PUBLICADA");
        }

        String rutEstudiante = RutUtils.normalizar(req.getEstudianteId());
        UsuarioEntity estudiante = usuarioRepository.findById(rutEstudiante)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con RUT: " + rutEstudiante));

        if (!"ESTUDIANTE".equalsIgnoreCase(estudiante.getRol())) {
            throw new IllegalArgumentException("Solo un usuario con rol ESTUDIANTE puede enviar registros");
        }

        if (estudiante.getCursoAsignado() != null && !estudiante.getCursoAsignado().equalsIgnoreCase(bitacora.getCurso())) {
            throw new IllegalArgumentException("No puedes responder bitácoras de otro curso (" + bitacora.getCurso() + ")");
        }

        if (registroRepository.existsByBitacoraIdAndEstudianteId(bitacora.getId(), estudiante.getId())) {
            throw new IllegalArgumentException("Ya enviaste un registro para esta bitácora. Puedes editarlo si aún está PENDIENTE");
        }

        RegistroEntity nuevo = RegistroEntity.builder()
                .bitacoraId(bitacora.getId())
                .estudianteId(estudiante.getId())
                .observacion(req.getObservacion().trim())
                .fotoUri(req.getFotoUri())
                .fecha(req.getFecha().trim())
                .estado("PENDIENTE")
                .retroalimentacionDocente(null)
                .build();

        return AppMappers.toRegistroResponse(registroRepository.save(nuevo));
    }

    /**
     * EDITA UN REGISTRO EXISTENTE (ESTUDIANTE):
     * Solo el estudiante dueño puede editarlo y únicamente si aún no fue REVISADO por el docente.
     */
    @Transactional
    public RegistroDTOs.RegistroResponse editarRegistroEstudiante(Long id, RegistroDTOs.RegistroUpdateRequest req) {
        RegistroEntity registro = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado con ID: " + id));

        String rutSolicitante = RutUtils.normalizar(req.getEstudianteId());
        if (!registro.getEstudianteId().equals(rutSolicitante)) {
            throw new IllegalArgumentException("No puedes editar el registro de otro estudiante");
        }

        if ("REVISADO".equalsIgnoreCase(registro.getEstado())) {
            throw new IllegalArgumentException("No se puede editar un registro que ya fue REVISADO por el docente");
        }

        registro.setObservacion(req.getObservacion().trim());
        registro.setFotoUri(req.getFotoUri());

        return AppMappers.toRegistroResponse(registroRepository.save(registro));
    }

    /**
     * REVISA Y RETROALIMENTA UN REGISTRO (DOCENTE):
     * Cambia el estado a REVISADO y guarda el comentario pedagógico del docente creador.
     */
    @Transactional
    public RegistroDTOs.RegistroResponse revisarRegistro(Long id, RegistroDTOs.RevisionRequest req) {
        RegistroEntity registro = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado con ID: " + id));

        BitacoraEntity bitacora = bitacoraRepository.findById(registro.getBitacoraId())
                .orElseThrow(() -> new IllegalArgumentException("La bitácora asociada no existe"));

        if (req.getDocenteId() != null && !req.getDocenteId().isBlank()) {
            String rutDocente = RutUtils.normalizar(req.getDocenteId());
            if (!bitacora.getDocenteId().equals(rutDocente)) {
                throw new IllegalArgumentException("Solo el docente creador de esta bitácora puede revisar sus registros");
            }
        }

        registro.setEstado(req.getEstado().toUpperCase());
        registro.setRetroalimentacionDocente(req.getRetroalimentacionDocente());

        return AppMappers.toRegistroResponse(registroRepository.save(registro));
    }

    /**
     * ELIMINA UN REGISTRO DE APRENDIZAJE POR SU ID:
     * Valida que exista y que, si lo elimina un estudiante, sea el dueño y aún no esté REVISADO.
     */
    @Transactional
    public void eliminarRegistro(Long id, String estudianteId) {
        RegistroEntity registro = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado con ID: " + id));

        if (estudianteId != null && !estudianteId.isBlank()) {
            String rutSolicitante = RutUtils.normalizar(estudianteId);
            if (!registro.getEstudianteId().equals(rutSolicitante)) {
                throw new IllegalArgumentException("No puedes eliminar el registro de otro estudiante");
            }
            if ("REVISADO".equalsIgnoreCase(registro.getEstado())) {
                throw new IllegalArgumentException("No puedes eliminar un registro que ya fue REVISADO por el docente");
            }
        }

        registroRepository.delete(registro);
    }
}