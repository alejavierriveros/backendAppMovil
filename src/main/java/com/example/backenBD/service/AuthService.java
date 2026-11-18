package com.example.backenBD.service;

import com.example.backenBD.dto.*;
import com.example.backenBD.mappers.AppMappers;
import com.example.backenBD.model.UsuarioEntity;
import com.example.backenBD.repository.UsuarioRepository;
import com.example.backenBD.security.JwtUtils;
import com.example.backenBD.util.RutUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * SERVICIO DE AUTENTICACION Y REGISTRO DE USUARIOS.
 * Contiene todas las validaciones de inicio de sesion y creacion de cuentas.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    /**
     * FUNCION PARA INICIAR SESION (LOGIN):
     * 1. Normaliza el RUT ingresado (quita puntos).
     * 2. Busca el usuario en MySQL por RUT (o por correo si ingreso correo).
     * 3. Compara la contrasena ingresada con la contrasena encriptada (BCrypt) en la BD.
     * 4. Genera el token JWT y retorna los datos del usuario a Android.
     */
    public LoginDTOs.LoginResponse iniciarSesion(LoginDTOs.LoginRequest request) {
        String identificador = request.getRut().trim();
        String rutNorm = RutUtils.normalizar(identificador);

        // Busca por RUT normalizado; si no existe, intenta buscar por correo
        UsuarioEntity usuario = usuarioRepository.findByRut(rutNorm)
                .or(() -> usuarioRepository.findByCorreo(identificador.toLowerCase()))
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas o usuario no encontrado"));

        // Verifica que la contrasena coincida con el hash BCrypt guardado en MySQL
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new IllegalArgumentException("Credenciales incorrectas o usuario no encontrado");
        }

        // Genera token JWT con el RUT y rol del usuario
        String token = jwtUtils.generateToken(usuario.getRut(), usuario.getRol());
        return new LoginDTOs.LoginResponse(token, AppMappers.toUsuarioDTO(usuario));
    }

    /**
     * FUNCION PARA REGISTRAR UN NUEVO USUARIO:
     * Valida:
     * 1. Que el RUT sea valido segun Modulo 11 (o este en la lista de prueba).
     * 2. Que no exista otro usuario con el mismo RUT o correo.
     * 3. Que el rol sea exactamente "DOCENTE" o "ESTUDIANTE".
     * 4. Si es DOCENTE, exige que tenga asignatura y curso asignado.
     * 5. Si es ESTUDIANTE, exige que tenga curso asignado y limpia asignaturaAsignada.
     * 6. Encripta la contrasena con BCrypt antes de guardar en MySQL.
     */
    public LoginDTOs.UsuarioDTO registrarUsuario(UsuarioRegistrationRequest req) {
        if (!RutUtils.esValido(req.getRut())) {
            throw new IllegalArgumentException("El RUT ingresado no es válido según el algoritmo Módulo 11");
        }

        String rutNorm = RutUtils.normalizar(req.getRut());
        String correoLimpio = req.getCorreo().trim().toLowerCase();
        String rolNormalizado = req.getRol().trim().toUpperCase();

        if (usuarioRepository.existsByRut(rutNorm)) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este RUT");
        }
        if (usuarioRepository.existsByCorreo(correoLimpio)) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este correo");
        }

        // Validaciones especificas segun el rol elegido
        if ("DOCENTE".equals(rolNormalizado)) {
            if (req.getAsignaturaAsignada() == null || req.getAsignaturaAsignada().isBlank()) {
                throw new IllegalArgumentException("Un docente debe tener una asignatura asignada");
            }
            if (req.getCursoAsignado() == null || req.getCursoAsignado().isBlank()) {
                throw new IllegalArgumentException("Un docente debe tener un curso asignado");
            }
        } else if ("ESTUDIANTE".equals(rolNormalizado)) {
            if (req.getCursoAsignado() == null || req.getCursoAsignado().isBlank()) {
                throw new IllegalArgumentException("Un estudiante debe tener un curso asignado");
            }
            req.setAsignaturaAsignada(null); // Un estudiante no imparte asignaturas
        } else {
            throw new IllegalArgumentException("Rol inválido. Solo se permite DOCENTE o ESTUDIANTE");
        }

        UsuarioEntity nuevoUsuario = UsuarioEntity.builder()
                .id(rutNorm)
                .rut(rutNorm)
                .nombre(req.getNombre().trim())
                .correo(correoLimpio)
                .contrasena(passwordEncoder.encode(req.getContrasena()))
                .rol(rolNormalizado)
                .cursoAsignado(req.getCursoAsignado().trim())
                .asignaturaAsignada(req.getAsignaturaAsignada() != null ? req.getAsignaturaAsignada().trim() : null)
                .build();

        usuarioRepository.save(nuevoUsuario);
        return AppMappers.toUsuarioDTO(nuevoUsuario);
    }
}
