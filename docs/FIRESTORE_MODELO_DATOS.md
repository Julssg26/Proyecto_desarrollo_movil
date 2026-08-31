# Modelo de Datos Firestore

Contrato inicial de colecciones para Mi Tecmi. Este documento debe mantenerse sincronizado con el equipo antes de cambiar campos usados por mas de un modulo.

## Colecciones Base

### `usuarios`

Representa estudiantes, miembros del consejo o administradores.

Campos sugeridos:

- `id`: String, UID de Firebase Auth.
- `nombre`: String.
- `correo`: String.
- `campus`: String.
- `facultad`: String.
- `semestre`: String o Number.
- `rol`: String, ejemplo `estudiante`, `consejo`, `admin`.
- `puntosTotales`: Number.
- `fechaRegistro`: Timestamp.

### `eventos`

Eventos publicados para la comunidad.

Campos sugeridos:

- `id`: String.
- `titulo`: String.
- `descripcion`: String.
- `fecha`: Timestamp o String normalizado.
- `hora`: String.
- `ubicacion`: String.
- `categoria`: String.
- `imagenUrl`: String.
- `puntosOtorgados`: Number.
- `qrToken`: String.
- `creadoPor`: String, UID.
- `fechaCreacion`: Timestamp.

### `asistencias`

Registra check-in por QR a eventos.

Campos sugeridos:

- `id`: String.
- `usuarioId`: String.
- `eventoId`: String.
- `fechaCheckIn`: Timestamp.
- `metodo`: String, ejemplo `qr`.
- `puntosGenerados`: Number.

Regla recomendada:

- Un usuario no debe registrar mas de una asistencia por evento.

### `puntos`

Historial de puntos para ranking y perfil.

Campos sugeridos:

- `id`: String.
- `usuarioId`: String.
- `eventoId`: String opcional.
- `origen`: String, ejemplo `asistencia`, `participacion`, `bonus`.
- `cantidad`: Number.
- `fecha`: Timestamp.

### `propuestas`

Buzon de quejas y propuestas.

Campos sugeridos:

- `id`: String.
- `titulo`: String.
- `descripcion`: String.
- `autorId`: String.
- `autorNombre`: String.
- `votos`: Number.
- `estado`: String, ejemplo `abierta`, `en_revision`, `resuelta`.
- `fecha`: Timestamp.

### `votos`

Evita votos duplicados en propuestas.

Campos sugeridos:

- `id`: String, recomendado `${usuarioId}_${propuestaId}`.
- `usuarioId`: String.
- `propuestaId`: String.
- `fecha`: Timestamp.

Regla recomendada:

- El documento debe ser unico por combinacion usuario/propuesta.
- El contador `votos` debe mantenerse en `propuestas` para evitar lecturas costosas.

### `encuestas`

Preguntas creadas por el consejo.

Campos sugeridos:

- `id`: String.
- `pregunta`: String.
- `opciones`: Array de objetos `{ id, texto, votos }`.
- `fechaCierre`: Timestamp.
- `activa`: Boolean.
- `creadoPor`: String.

### `respuestasEncuesta`

Evita doble voto en encuestas.

Campos sugeridos:

- `id`: String, recomendado `${usuarioId}_${encuestaId}`.
- `usuarioId`: String.
- `encuestaId`: String.
- `opcionElegida`: String.
- `fecha`: Timestamp.

### `clubes`

Directorio de clubes y organizaciones.

Campos sugeridos:

- `id`: String.
- `nombre`: String.
- `descripcion`: String.
- `contacto`: String.
- `categoria`: String.
- `horario`: String.
- `imagenUrl`: String.

### `anuncios`

Tablon de anuncios institucionales o del consejo.

Campos sugeridos:

- `id`: String.
- `titulo`: String.
- `contenido`: String.
- `facultad`: String.
- `semestre`: String opcional.
- `fecha`: Timestamp.
- `autorId`: String.

### `objetosPerdidos`

Publicaciones de objetos perdidos o encontrados.

Campos sugeridos:

- `id`: String.
- `tipo`: String, `Perdido` o `Encontrado`.
- `descripcion`: String.
- `ubicacion`: String.
- `fotoUrl`: String.
- `autorId`: String.
- `autorNombre`: String.
- `contacto`: String.
- `fecha`: Timestamp.
- `estado`: String, ejemplo `Buscando`, `Disponible`, `Entregado`.

### `lugaresCampus`

Puntos para el mapa estatico del campus.

Campos sugeridos:

- `id`: String.
- `nombre`: String.
- `tipo`: String, ejemplo `salon`, `servicio`, `oficina`, `area_comun`.
- `coordenadaX`: Number.
- `coordenadaY`: Number.
- `descripcion`: String.

## Orden de Integracion Recomendado

1. `usuarios` con Firebase Auth.
2. `propuestas` y `votos`.
3. `encuestas` y `respuestasEncuesta`.
4. `clubes` y `anuncios`.
5. `objetosPerdidos` con Storage.
6. `eventos`, `asistencias` y `puntos`.
7. `lugaresCampus`.
8. Cloud Messaging para eventos y actualizaciones importantes.
