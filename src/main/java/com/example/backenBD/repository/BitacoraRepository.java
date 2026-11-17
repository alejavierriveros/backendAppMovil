package com.example.backenBD.repository;

import com.example.backenBD.model.BitacoraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<BitacoraEntity, Long> {

    @Query("SELECT b FROM BitacoraEntity b WHERE " +
           "(:docenteId IS NULL OR b.docenteId = :docenteId) AND " +
           "(:curso IS NULL OR b.curso = :curso) AND " +
           "(:estado IS NULL OR b.estado = :estado)")
    List<BitacoraEntity> filtrarBitacoras(@Param("docenteId") String docenteId,
                                          @Param("curso") String curso,
                                          @Param("estado") String estado);
}