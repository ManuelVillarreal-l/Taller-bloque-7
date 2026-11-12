# Taller práctico · Bloque 7 — SmartLibrary

**Del modelo de clases orientado a objetos a un diseño por tres capas para una API REST**
Diseño de Software · Cuarto semestre

| | |
|---|---|
| Integrante 1 | _______________________ |
| Integrante 2 | _______________________ |
| Profesor | _______________________ |

Caso de uso trabajado: **«Registrar préstamo»** (`POST /api/prestamos`) y consulta de préstamos (`GET /api/prestamos`).

---

## Contenido del repositorio

```
├── diagramas/
│   ├── Taller_Bloque7_SmartLibrary.vpp      ← Proyecto Visual Paradigm (los 3 diagramas UML)
│   ├── png/                                 ← Los mismos diagramas como imagen
│   │   ├── 01_modelo_inicial_anotado.png
│   │   ├── 02_modelo_clases_por_capas.png
│   │   └── 03_secuencia_registrar_prestamo.png
│   └── fuentes/                             ← Fuente de las imágenes (Graphviz / Mermaid)
├── documentos/
│   ├── Taller_Bloque7_Resuelto.docx         ← Taller completo resuelto (actividades 1 a 7)
│   ├── Taller_Bloque7_Resuelto.pdf          ← Misma versión en PDF
│   ├── Enunciado_Taller_practico_bloque_7.docx
│   └── Lectura_Previa_Bloque_7.docx
└── codigo/
    └── smartlibrary/                        ← Proyecto Spring Boot (Java 21, Maven, H2)
```

## Entregables solicitados

| Entregable | Dónde está |
|---|---|
| Diagrama inicial anotado con observaciones | `diagramas/…vpp` → *01 - Modelo inicial anotado* · Word, Actividad 1 |
| Tabla de responsabilidades completa | Word, Actividad 2 |
| Diagrama de clases por capas con relaciones y multiplicidades justificadas | `diagramas/…vpp` → *02 - Modelo de clases por capas* · Word, Actividad 3 |
| Diagrama de secuencia del préstamo (éxito y libro no disponible) | `diagramas/…vpp` → *03 - Secuencia POST api-prestamos* · Word, Actividad 4 |
| Proyecto Spring Boot y evidencias de pruebas | `codigo/smartlibrary` · Word, Actividades 5 y 6 |
| Explicación de decisiones de diseño (máx. 1 página) | Word, sección final |
| Preguntas de revisión y defensa | Word, Actividad 7 |

### Cómo abrir los diagramas
Abrir `diagramas/Taller_Bloque7_SmartLibrary.vpp` con **Visual Paradigm** (versión 17 o superior) y usar el *Diagram Navigator*:
- **Class Diagram → 01 - Modelo inicial anotado**
- **Class Diagram → 02 - Modelo de clases por capas (Controller-Service-Repository)**
- **Sequence Diagram → 03 - Secuencia POST api-prestamos (Registrar prestamo)**

---

## Diseño en resumen

**Capas:** `PrestamoController` → `PrestamoService` → `EstudianteRepository`, `LibroRepository`, `PrestamoRepository`.
Las entidades `Estudiante`, `Libro` y `Prestamo` forman el modelo de dominio que usan las capas. No son una cuarta capa.

**Reglas del caso y dónde se cumplen**

| Regla | Clase | Operación | Prueba |
|---|---|---|---|
| R1 Solo se presta un libro disponible | `PrestamoService`, `Libro` | `registrarPrestamo()`, `isDisponible()` | 409 · `libroYaPrestado_devuelve409_sinNuevoPrestamo` |
| R2 Registra estudiante, libro y fecha | `Prestamo`, `PrestamoRepository` | `Prestamo(estudiante, libro, fecha)`, `save()` | 201 · `prestamoDeLibroDisponible_devuelve201` |
| R3 El libro deja de estar disponible | `Libro` | `marcarPrestado()` | segundo préstamo del mismo libro → 409 |
| R4 Solicitud inválida no crea préstamo | `PrestamoService` (`@Transactional`), `GlobalExceptionHandler` | `registrarPrestamo()`, `noEncontrado()`, `invalida()` | 404 / 400 · tests de inexistentes y validación |
| R5 Un solo préstamo activo por libro | `Libro`, `Prestamo` | `isDisponible()`, `estaActivo()` | libro 3 ya prestado → 409 |

**Patrones GRASP:** Controlador (`PrestamoService` coordina el caso de uso), Experto en información (`Libro` decide sobre su disponibilidad), Creador (`PrestamoService` crea `Prestamo`), Fabricación pura e Indirección (repositorios), Bajo acoplamiento y Alta cohesión.

---

## Ejecutar el proyecto

Requisitos: **Java 21** y **Maven 3.9+**.

```bash
cd codigo/smartlibrary
mvn spring-boot:run      # levanta la API en http://localhost:8080
mvn test                 # ejecuta las pruebas de los 6 escenarios
```

Consola H2: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:smartlibrary`, usuario `sa`, sin contraseña).

### Datos iniciales (`data.sql`)
- Estudiantes: 1 Ana Gómez · 2 Luis Pérez
- Libros: 1 Diseño de Software (disponible) · 2 Patrones GRASP (disponible) · 3 Clean Architecture (**prestado**)
- Un préstamo activo: estudiante 2 – libro 3

### Escenarios de prueba (también en `codigo/smartlibrary/pruebas.http`)

| # | Escenario | Petición | Resultado esperado |
|---|---|---|---|
| 1 | Préstamo de libro disponible | `POST /api/prestamos` `{"estudianteId":1,"libroId":1}` | **201 Created** + préstamo registrado |
| 2 | Libro ya prestado | `POST /api/prestamos` `{"estudianteId":1,"libroId":3}` | **409 Conflict**, sin nuevo préstamo |
| 3 | Estudiante inexistente | `POST /api/prestamos` `{"estudianteId":99,"libroId":2}` | **404 Not Found** |
| 4 | Libro inexistente | `POST /api/prestamos` `{"estudianteId":1,"libroId":99}` | **404 Not Found** |
| 5 | Consulta de préstamos | `GET /api/prestamos` | **200 OK** con registros |
| 6 | Petición inválida (extra) | `POST /api/prestamos` `{"libroId":2}` | **400 Bad Request** |

Ejemplo con curl:
```bash
curl -i -X POST http://localhost:8080/api/prestamos \
     -H "Content-Type: application/json" \
     -d '{"estudianteId":1,"libroId":1}'
```

### Estructura de paquetes
```
co.edu.smartlibrary
├── controller   PrestamoController
├── service      PrestamoService
├── repository   EstudianteRepository, LibroRepository, PrestamoRepository
├── model        Estudiante, Libro, Prestamo
├── dto          PrestamoRequest, PrestamoResponse
└── exception    RecursoNoEncontradoException, LibroNoDisponibleException, GlobalExceptionHandler
```
