package com.example.backenBD.repository;

import com.example.backenBD.model.RegistroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<RegistroEntity, Long> {

    @Query("SELECT r FROM RegistroEntity r WHERE " +
           "(:bitacoraId IS NULL OR r.bitacoraId = :bitacoraId) AND " +
           "(:estudianteId IS NULL OR r.estudianteId = :estudianteId)")
    List<RegistroEntity> filtrarRegistros(@Param("bitacoraId") Long bitacoraId,
                                          @Param("estudianteId") String estudianteId);
}