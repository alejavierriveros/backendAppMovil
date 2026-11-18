package com.example.backenBD.service;

import com.example.backenBD.dto.RegistroDTOs;
import com.example.backenBD.mappers.AppMappers;
import com.example.backenBD.model.RegistroEntity;
import com.example.backenBD.repository.RegistroRepository;
import com.example.backenBD.util.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroRepository registroRepository;

    public List<RegistroDTOs.RegistroResponse> obtenerRegistros(Long bitacoraId, String estudianteId) {
        String estIdNorm = estudianteId != null ? RutUtils.normalizar(estudianteId) : null;
        return registroRepository.filtrarRegistros(bitacoraId, estIdNorm).stream()
                .map(AppMappers::toRegistroResponse)
                .collect(Collectors.toList());
    }

    public RegistroDTOs.RegistroResponse crearRegistro(RegistroDTOs.RegistroCreateRequest req) {
        String estNorm = RutUtils.normalizar(req.getEstudianteId());

        RegistroEntity reg = RegistroEntity.builder()
                .bitacoraId(req.getBitacoraId())
                .estudianteId(estNorm)
                .observacion(req.getObservacion())
                .fotoUri(req.getFotoUri())
                .fecha(req.getFecha())
                .estado("PENDIENTE")
                .build();

        registroRepository.save(reg);
        return AppMappers.toRegistroResponse(reg);
    }

    public RegistroDTOs.RegistroResponse revisarRegistro(Long id, RegistroDTOs.RevisionRequest req) {
        RegistroEntity reg = registroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de aprendizaje no encontrado"));

        reg.setEstado(req.getEstado());
        reg.setRetroalimentacionDocente(req.getRetroalimentacionDocente());

        registroRepository.save(reg);
        return AppMappers.toRegistroResponse(reg);
    }
}