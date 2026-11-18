package com.example.backenBD.config;

import com.example.backenBD.model.UsuarioEntity;
import com.example.backenBD.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CARGA INICIAL DE DATOS DE PRUEBA (SEEDER).
 * Al arrancar Spring Boot, verifica si los 10 usuarios de prueba existen en MySQL.
 * Si no existen, los crea automaticamente con sus contrasenas encriptadas en BCrypt.
 * (Si en algun momento no quieres datos automaticos, puedes borrar este archivo sin afectar el resto).
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<UsuarioEntity> usuariosPrueba = List.of(
                // 5 Docentes de prueba
                UsuarioEntity.builder()
                        .id("11111111-1").rut("11111111-1").nombre("María González")
                        .correo("maria.gonzalez@colegio.cl").contrasena(passwordEncoder.encode("docente123"))
                        .rol("DOCENTE").cursoAsignado("6.º básico A").asignaturaAsignada("Matemáticas").build(),
                UsuarioEntity.builder()
                        .id("22222222-2").rut("22222222-2").nombre("Carlos Soto")
                        .correo("carlos.soto@colegio.cl").contrasena(passwordEncoder.encode("docente123"))
                        .rol("DOCENTE").cursoAsignado("7.º básico A").asignaturaAsignada("Ciencias Naturales").build(),
                UsuarioEntity.builder()
                        .id("55555555-5").rut("55555555-5").nombre("Ana Pérez")
                        .correo("ana.perez@colegio.cl").contrasena(passwordEncoder.encode("docente123"))
                        .rol("DOCENTE").cursoAsignado("5.º básico A").asignaturaAsignada("Lenguaje y Comunicación").build(),
                UsuarioEntity.builder()
                        .id("66666666-6").rut("66666666-6").nombre("Jorge Ramírez")
                        .correo("jorge.ramirez@colegio.cl").contrasena(passwordEncoder.encode("docente123"))
                        .rol("DOCENTE").cursoAsignado("8.º básico A").asignaturaAsignada("Historia y Geografía").build(),
                UsuarioEntity.builder()
                        .id("77777777-7").rut("77777777-7").nombre("Claudia Silva")
                        .correo("claudia.silva@colegio.cl").contrasena(passwordEncoder.encode("docente123"))
                        .rol("DOCENTE").cursoAsignado("6.º básico B").asignaturaAsignada("Inglés").build(),

                // 5 Estudiantes de prueba
                UsuarioEntity.builder()
                        .id("33333333-3").rut("33333333-3").nombre("Matías Rojas")
                        .correo("matias.rojas@estudiante.cl").contrasena(passwordEncoder.encode("estudiante123"))
                        .rol("ESTUDIANTE").cursoAsignado("6.º básico A").asignaturaAsignada(null).build(),
                UsuarioEntity.builder()
                        .id("44444444-4").rut("44444444-4").nombre("Sofía Morales")
                        .correo("sofia.morales@estudiante.cl").contrasena(passwordEncoder.encode("estudiante123"))
                        .rol("ESTUDIANTE").cursoAsignado("6.º básico A").asignaturaAsignada(null).build(),
                UsuarioEntity.builder()
                        .id("88888888-8").rut("88888888-8").nombre("Benjamín Silva")
                        .correo("benjamin.silva@estudiante.cl").contrasena(passwordEncoder.encode("estudiante123"))
                        .rol("ESTUDIANTE").cursoAsignado("6.º básico A").asignaturaAsignada(null).build(),
                UsuarioEntity.builder()
                        .id("99999999-9").rut("99999999-9").nombre("Valentina Castro")
                        .correo("valentina.castro@estudiante.cl").contrasena(passwordEncoder.encode("estudiante123"))
                        .rol("ESTUDIANTE").cursoAsignado("7.º básico A").asignaturaAsignada(null).build(),
                UsuarioEntity.builder()
                        .id("10101010-1").rut("10101010-1").nombre("Martín Pérez")
                        .correo("martin.perez@estudiante.cl").contrasena(passwordEncoder.encode("estudiante123"))
                        .rol("ESTUDIANTE").cursoAsignado("5.º básico A").asignaturaAsignada(null).build()
        );

        for (UsuarioEntity usuario : usuariosPrueba) {
            if (!usuarioRepository.existsById(usuario.getId())) {
                usuarioRepository.save(usuario);
            }
        }
        System.out.println(">>> Seeding inicial verificado: 10 cuentas de prueba listas en MySQL.");
    }
}
