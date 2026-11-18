# Bitácora Escolar — Backend API REST

Servicio Backend desarrollado en **Spring Boot (Java 21)**, **Spring Data JPA** y **MySQL**, diseñado para dar soporte de persistencia remota, autenticación segura y reglas de negocio a la aplicación móvil **Bitácora Escolar** de la Escuela Básica de San Bernardo (Proyecto correspondiente a la asignatura **DSY1105 - Desarrollo de Aplicaciones Móviles**, Duoc UC).

## 👥 Integrantes del Equipo
- **Jahir Pineda** (Ingeniería en Informática - Duoc UC)
- **Nicolás Romero** (Ingeniería en Informática - Duoc UC)
- **Alejandro Riveros** (Ingeniería en Informática - Duoc UC)

---

## 🚀 Arquitectura y Funcionalidades Implementadas

1. **Modelo Relacional Normalizado (3FN) con Spring Data JPA:**
   - `UsuarioEntity`: Gestión de usuarios diferenciados por rol (`"DOCENTE"` o `"ESTUDIANTE"`), utilizando el RUT normalizado como clave primaria, correo único, contraseña encriptada y curso/asignatura asignada.
   - `BitacoraEntity`: Entidad de bitácoras pedagógicas con ID autoincremental, título, descripción, fecha (`dd/MM/yyyy`), curso, estado (`"BORRADOR"`, `"PUBLICADA"`, `"CERRADA"`) y relación `ManyToOne` con el docente creador (`docente_id`).
   - `RegistroEntity`: Entidad de evidencias y avances del estudiante vinculada a `BitacoraEntity` y `UsuarioEntity`, con soporte para observaciones, URI de evidencia fotográfica, fecha, estado de revisión (`"PENDIENTE"`, `"REVISADO"`) y retroalimentación docente.
   - **Seeding Automático (`DataSeeder`):** Carga inicial automática al arrancar el servidor de los 5 docentes y 5 estudiantes de prueba en MySQL.

2. **Seguridad, Encriptación y Validaciones:**
   - **Encriptación BCrypt (`SecurityConfig`):** Todas las contraseñas se almacenan encriptadas mediante `BCryptPasswordEncoder`.
   - **Autenticación con JWT (`JwtUtils`):** Generación de tokens firmados con el RUT y rol del usuario al iniciar sesión.
   - **Validación de RUT Chileno (`RutUtils`):** Normalización automática (eliminación de puntos y estandarización de guion) y validación mediante algoritmo **Módulo 11** con lista blanca para las cuentas institucionales de prueba.
   - **Reglas de Negocio por Rol:**
     - Solo usuarios con rol `DOCENTE` pueden crear, editar o eliminar bitácoras dentro de su propio curso asignado.
     - Solo usuarios con rol `ESTUDIANTE` pueden enviar registros en bitácoras `PUBLICADA` de su mismo curso, sin duplicados, y editarlos o eliminarlos únicamente mientras estén en estado `PENDIENTE`.
     - Solo el docente creador de una bitácora puede revisar y retroalimentar los registros asociados a ella.
   - **Manejo Global de Errores (`GlobalExceptionHandler`):** Respuestas JSON estandarizadas (`400 Bad Request`, `401 Unauthorized`, `403 Forbidden`, `500 Internal Server Error`) para su consumo desde Retrofit en Android.

---

## 📡 Endpoints de la API REST

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Inicia sesión con RUT (o correo) y contraseña; retorna token JWT y perfil. |
| `POST` | `/api/usuarios` | Registra un nuevo docente o estudiante validando RUT Módulo 11 y correo único. |
| `GET` | `/api/bitacoras` | Lista bitácoras con filtros opcionales (`?docenteId=...&curso=...&estado=...`). |
| `GET` | `/api/bitacoras/{id}` | Obtiene el detalle de una bitácora específica por su ID. |
| `POST` | `/api/bitacoras` | Crea una nueva bitácora (exclusivo rol `DOCENTE`). |
| `PUT` | `/api/bitacoras/{id}` | Edita una bitácora existente (solo el docente creador). |
| `DELETE` | `/api/bitacoras/{id}` | Elimina una bitácora y sus registros asociados. |
| `GET` | `/api/registros` | Lista registros de aprendizaje (`?bitacoraId=...&estudianteId=...`). |
| `POST` | `/api/registros` | Envía un nuevo registro de aprendizaje (exclusivo rol `ESTUDIANTE`). |
| `PUT` | `/api/registros/{id}` | Edita un registro propio mientras esté en estado `PENDIENTE`. |
| `PATCH` | `/api/registros/{id}/revision` | Evalúa un registro como `REVISADO` y agrega retroalimentación docente. |
| `DELETE` | `/api/registros/{id}` | Elimina un registro propio mientras no haya sido revisado. |

---

## 🔑 Cuentas de Prueba (Seeding Inicial)

### 👩‍🏫 Docentes (5)
- **María González** | RUT: `11.111.111-1` | Clave: `docente123` | 6.º básico A • Matemáticas
- **Carlos Soto** | RUT: `22.222.222-2` | Clave: `docente123` | 7.º básico A • Ciencias Naturales
- **Ana Pérez** | RUT: `55.555.555-5` | Clave: `docente123` | 5.º básico A • Lenguaje y Comunicación
- **Jorge Ramírez** | RUT: `66.666.666-6` | Clave: `docente123` | 8.º básico A • Historia y Geografía
- **Claudia Silva** | RUT: `77.777.777-7` | Clave: `docente123` | 6.º básico B • Inglés

### 👨‍🎓 Estudiantes (5)
- **Matías Rojas** | RUT: `33.333.333-3` | Clave: `estudiante123` | 6.º básico A
- **Sofía Morales** | RUT: `44.444.444-4` | Clave: `estudiante123` | 6.º básico A
- **Benjamín Silva** | RUT: `88.888.888-8` | Clave: `estudiante123` | 6.º básico A
- **Valentina Castro** | RUT: `99.999.999-9` | Clave: `estudiante123` | 7.º básico A
- **Martín Pérez** | RUT: `10.101.101-1` | Clave: `estudiante123` | 5.º básico A

---

## 🛠️ Instrucciones de Ejecución Local

1. **Requisitos previos:**
   - **JDK 21** instalado y configurado.
   - **XAMPP** (o servidor MySQL) ejecutándose en el puerto `3306` con usuario `root` sin contraseña.
2. **Levantar la base de datos:**
   - Iniciar el módulo **MySQL** en el panel de control de XAMPP (Spring Boot creará automáticamente la base de datos `Bitacora` y sus tablas al arrancar).
3. **Ejecutar el servidor Spring Boot:**
   
```bash
   ./mvnw spring-boot:run