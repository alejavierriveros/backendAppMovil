package com.example.backenBD.repository;

import com.example.backenBD.model.RegistroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORIO DE REGISTROS DE APRENDIZAJE.
 * Gestiona las consultas SQL sobre la tabla 'registros'.
 */
@Repository
public interface RegistroRepository extends JpaRepository<RegistroEntity, Long> {

    /**
     * Filtra registros por ID de bitacora y/o por RUT de estudiante.
     * Si alguno es null, devuelve todos los registros que cumplan el otro criterio.
     */
    @Query("SELECT r FROM RegistroEntity r WHERE " +
           "(:bitacoraId IS NULL OR r.bitacoraId = :bitacoraId) AND " +
           "(:estudianteId IS NULL OR r.estudianteId = :estudianteId)")
    List<RegistroEntity> filtrarRegistros(@Param("bitacoraId") Long bitacoraId,
                                          @Param("estudianteId") String estudianteId);

    // Verifica si un estudiante ya envio un registro para una bitacora especifica (evita duplicados)
    boolean existsByBitacoraIdAndEstudianteId(Long bitacoraId, String estudianteId);
}
