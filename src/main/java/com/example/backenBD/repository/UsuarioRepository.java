package com.example.backenBD.repository;

import com.example.backenBD.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * REPOSITORIO DE USUARIOS.
 * Spring Data JPA genera automaticamente las consultas SQL a partir del nombre de cada metodo.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {

    // Busca un usuario por su RUT exacto (SELECT * FROM usuarios WHERE rut = ?)
    Optional<UsuarioEntity> findByRut(String rut);

    // Busca un usuario por su correo electronico
    Optional<UsuarioEntity> findByCorreo(String correo);

    // Devuelve true si ya existe un usuario registrado con ese RUT
    boolean existsByRut(String rut);

    // Devuelve true si ya existe un usuario registrado con ese correo
    boolean existsByCorreo(String correo);
}
