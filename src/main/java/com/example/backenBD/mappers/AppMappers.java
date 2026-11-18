package com.example.backenBD.mappers;

import com.example.backenBD.dto.*;
import com.example.backenBD.model.*;

/**
 * CLASE DE MAPEO (CONVERSOR).
 * Convierte las entidades de base de datos (Entity) en objetos seguros de respuesta (DTO)
 * para enviarlos como JSON a Android (por ejemplo, ocultando la contrasena de UsuarioEntity).
 */
public class AppMappers {

    // Convierte un UsuarioEntity de MySQL en un UsuarioDTO (sin contrasena)
    public static LoginDTOs.UsuarioDTO toUsuarioDTO(UsuarioEntity u) {
        return LoginDTOs.UsuarioDTO.builder()
                .id(u.getId())
                .rut(u.getRut())
                .nombre(u.getNombre())
                .correo(u.getCorreo())
                .rol(u.getRol())
                .cursoAsignado(u.getCursoAsignado())
                .asignaturaAsignada(u.getAsignaturaAsignada())
                .build();
    }

    // Convierte una BitacoraEntity de MySQL en BitacoraResponse para Android
    public static BitacoraDTOs.BitacoraResponse toBitacoraResponse(BitacoraEntity b) {
        return BitacoraDTOs.BitacoraResponse.builder()
                .id(b.getId())
                .titulo(b.getTitulo())
                .descripcion(b.getDescripcion())
                .fecha(b.getFecha())
                .curso(b.getCurso())
                .estado(b.getEstado())
                .docenteId(b.getDocenteId())
                .build();
    }

    // Convierte un RegistroEntity de MySQL en RegistroResponse para Android
    public static RegistroDTOs.RegistroResponse toRegistroResponse(RegistroEntity r) {
        return RegistroDTOs.RegistroResponse.builder()
                .id(r.getId())
                .bitacoraId(r.getBitacoraId())
                .estudianteId(r.getEstudianteId())
                .observacion(r.getObservacion())
                .fotoUri(r.getFotoUri())
                .fecha(r.getFecha())
                .estado(r.getEstado())
                .retroalimentacionDocente(r.getRetroalimentacionDocente())
                .build();
    }
}
