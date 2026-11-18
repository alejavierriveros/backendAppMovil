package com.example.backenBD.service;

import com.example.backenBD.dto.BitacoraDTOs;
import com.example.backenBD.mappers.AppMappers;
import com.example.backenBD.model.BitacoraEntity;
import com.example.backenBD.repository.BitacoraRepository;
import com.example.backenBD.util.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;

    public List<BitacoraDTOs.BitacoraResponse> obtenerBitacoras(String docenteId, String curso, String estado) {
        String docenteIdNorm = docenteId != null ? RutUtils.normalizar(docenteId) : null;
        return bitacoraRepository.filtrarBitacoras(docenteIdNorm, curso, estado).stream()
                .map(AppMappers::toBitacoraResponse)
                .collect(Collectors.toList());
    }

    public BitacoraDTOs.BitacoraResponse obtenerPorId(Long id) {
        BitacoraEntity b = bitacoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bitácora no encontrada con ID: " + id));
        return AppMappers.toBitacoraResponse(b);
    }

    public BitacoraDTOs.BitacoraResponse crearBitacora(BitacoraDTOs.BitacoraCreateRequest req) {
        String docenteNorm = RutUtils.normalizar(req.getDocenteId());
        
        BitacoraEntity entity = BitacoraEntity.builder()
                .titulo(req.getTitulo())
                .descripcion(req.getDescripcion())
                .fecha(req.getFecha())
                .curso(req.getCurso())
                .estado(req.getEstado())
                .docenteId(docenteNorm)
                .build();

        bitacoraRepository.save(entity);
        return AppMappers.toBitacoraResponse(entity);
    }

    public BitacoraDTOs.BitacoraResponse actualizarBitacora(Long id, BitacoraDTOs.BitacoraCreateRequest req) {
        BitacoraEntity b = bitacoraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bitácora no encontrada"));

        b.setTitulo(req.getTitulo());
        b.setDescripcion(req.getDescripcion());
        b.setFecha(req.getFecha());
        b.setCurso(req.getCurso());
        b.setEstado(req.getEstado());

        bitacoraRepository.save(b);
        return AppMappers.toBitacoraResponse(b);
    }
}