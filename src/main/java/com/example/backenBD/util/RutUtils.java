package com.example.backenBD.util;

import java.util.Set;

public class RutUtils {

    // Lista blanca exacta con los 10 RUTs de prueba (normalizados sin puntos)
    private static final Set<String> RUTS_DE_PRUEBA = Set.of(
            "11111111-1", // María González (Docente)
            "22222222-2", // Carlos Soto (Docente)
            "55555555-5", // Ana Pérez (Docente)
            "66666666-6", // Jorge Ramírez (Docente)
            "77777777-7", // Claudia Silva (Docente)
            "33333333-3", // Matías Rojas (Estudiante)
            "44444444-4", // Sofía Morales (Estudiante)
            "88888888-8", // Benjamín Silva (Estudiante)
            "99999999-9", // Valentina Castro (Estudiante)
            "10101010-1"  // Martín Pérez (Estudiante)
    );

    /**
     * Normaliza un RUT dejándolo sin puntos, con guion y DV en mayúscula.
     * Ejemplo: "11.111.111-1" -> "11111111-1"
     */
    public static String normalizar(String rut) {
        if (rut == null || rut.isBlank()) {
            return null;
        }
        String limpio = rut.replace(".", "").replace(" ", "").trim().toUpperCase();
        if (!limpio.contains("-") && limpio.length() >= 2) {
            limpio = limpio.substring(0, limpio.length() - 1) + "-" + limpio.substring(limpio.length() - 1);
        }
        return limpio;
    }

    /**
     * Valida un RUT chileno mediante el algoritmo Módulo 11,
     * permitiendo además los 10 RUTs de prueba del sistema.
     */
    public static boolean esValido(String rut) {
        String normalizado = normalizar(rut);
        if (normalizado == null) {
            return false;
        }

        // 1. Permitir los 10 RUTs de prueba predefinidos
        if (RUTS_DE_PRUEBA.contains(normalizado)) {
            return true;
        }

        // 2. Validar formato y algoritmo Módulo 11 para cualquier otro RUT nuevo
        if (!normalizado.matches("^[0-9]{7,8}-[0-9K]$")) {
            return false;
        }

        String[] partes = normalizado.split("-");
        int numero = Integer.parseInt(partes[0]);
        char dvIngresado = partes[1].charAt(0);

        int m = 0;
        int s = 1;
        for (; numero != 0; numero /= 10) {
            s = (s + numero % 10 * (9 - m++ % 6)) % 11;
        }
        char dvCalculado = (char) (s != 0 ? s + 47 : 75);

        return dvIngresado == dvCalculado;
    }
}