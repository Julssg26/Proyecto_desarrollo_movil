# Progreso del Proyecto Mi Tecmi

Fecha de revision: 31 de agosto de 2026

Repositorio GitHub: https://github.com/Julssg26/Proyecto_desarrollo_movil

## Resumen Ejecutivo

Mi Tecmi se encuentra en una primera fase de prototipo funcional. La aplicacion ya puede ejecutarse en Android Studio y permite probar los flujos principales definidos para la parte de comunidad estudiantil: propuestas, encuestas, clubes, anuncios, eventos, busqueda, perfil y objetos perdidos.

La aplicacion todavia no tiene persistencia real. Actualmente usa datos locales en memoria mediante un repositorio falso, lo cual permite validar interfaz y comportamiento antes de conectar Firebase/Firestore.

## Estado en GitHub

Estado observado del repositorio remoto:

- Repositorio publico: `Julssg26/Proyecto_desarrollo_movil`.
- Rama principal: `main`.
- Ultimo commit observado: `Prepara base Firebase`.
- Contenido remoto visible: proyecto Android con modulo `app`, Gradle Wrapper y configuracion base.

Observacion local:

- La rama local `main` esta conectada con `origin/main`.
- Los cambios se estan subiendo directamente a `main` durante esta etapa de desarrollo.
- Los documentos PDF/DOCX de referencia se mantienen locales y no se suben al repositorio.

## Avance Implementado

### Base Tecnica

- Proyecto Android creado con Kotlin y Jetpack Compose.
- Modulo principal `:app` configurado.
- Tema Compose y Material 3 integrados.
- Navegacion con Navigation Compose.
- Estructura organizada por modelos, repositorio, ViewModel, navegacion y pantallas.
- Uso de `StateFlow` y `MutableStateFlow` para manejar estado observable.

### Navegacion Principal

La app incluye una barra inferior con cinco secciones:

- Inicio.
- Buscar.
- Eventos.
- Comunidad.
- Perfil.

Tambien existen pantallas secundarias para detalles y formularios:

- Crear propuesta.
- Detalle de propuesta.
- Votar encuesta.
- Detalle de club.
- Crear objeto perdido o encontrado.
- Detalle de objeto.

### Funcionalidades de Comunidad

Propuestas:

- Listado de propuestas.
- Creacion de nueva propuesta.
- Detalle de propuesta.
- Votar y retirar voto durante la sesion.

Encuestas:

- Visualizacion de encuesta activa.
- Seleccion de opcion.
- Registro de voto una sola vez durante la sesion.
- Resultados con conteo/porcentaje.

Clubes:

- Listado de clubes.
- Detalle con descripcion, categoria, horario y contacto.

Anuncios:

- Datos de anuncios disponibles en el repositorio local.
- Visualizacion dentro del contenido de la aplicacion.

Objetos perdidos:

- Feed de objetos perdidos o encontrados.
- Formulario para publicar un objeto.
- Detalle con ubicacion, estado y contacto.

### Eventos y Perfil

Eventos:

- Listado de eventos.
- Informacion de fecha, hora, ubicacion, categoria e interesados.
- Interaccion para marcar o desmarcar interes.

Perfil:

- Vista de perfil de estudiante de prueba.
- Resumen de actividad relacionado con acciones dentro de la app.

### Busqueda

- Busqueda local sobre eventos, propuestas, encuestas, clubes, anuncios y objetos.
- Navegacion desde resultados hacia pantallas de detalle cuando aplica.

## Limitaciones Actuales

- Firebase ya esta conectado a nivel de configuracion Android.
- La autenticacion por correo/contrasena ya funciona con Firebase Auth.
- No hay base de datos persistente.
- No hay sincronizacion entre dispositivos.
- No hay carga real de imagenes para objetos o clubes.
- No hay notificaciones push implementadas.
- Las pruebas automatizadas actuales son las pruebas base generadas por el proyecto.

## Relacion con el Plan de Trabajo

El plan original contempla una app hub para el consejo estudiantil con eventos, QR, puntos/ranking, insignias y modulos de comunidad.

Para esta fase, el avance local cubre principalmente los modulos de comunidad:

- Buzon de propuestas.
- Directorio de clubes.
- Tablero de anuncios.
- Encuestas.
- Objetos perdidos.
- Busqueda y perfil como soporte de experiencia general.

Pendientes del plan original que todavia no se observan implementados:

- Check-in por QR.
- Sistema de puntos y ranking.
- Insignias por tipo de evento.
- Mapa del campus.
- Notificaciones push.
- Integracion real de los modulos de comunidad con Firestore.

## Pruebas Recomendadas

Las pruebas funcionales deben hacerse ejecutando la app en emulador o dispositivo fisico, no solamente con Compose Preview.

Checklist manual:

- Abrir la app desde Android Studio.
- Crear cuenta con nombre, correo `@tecmilenio.mx`, contrasena y confirmacion.
- Intentar crear cuenta con otro dominio y confirmar que la app lo rechaza.
- Iniciar sesion con una cuenta registrada.
- Cerrar sesion desde Perfil.
- Navegar entre las cinco secciones principales.
- Crear una propuesta y confirmar que aparece en la lista.
- Votar una propuesta y retirar el voto.
- Marcar interes en un evento y verificar el contador.
- Votar en una encuesta y revisar resultados.
- Intentar votar una segunda vez en la misma encuesta.
- Abrir detalles de clubes.
- Publicar un objeto perdido o encontrado.
- Buscar contenido con palabras clave.
- Revisar si Perfil refleja actividad despues de interactuar.

## Preparacion Firebase y Login

La base tecnica para Firebase ya quedo iniciada:

- Dependencias preparadas para Authentication, Firestore, Cloud Messaging y Storage.
- Plugin de Google Services declarado y activo con `app/google-services.json`.
- Constantes de colecciones Firestore en `FirestoreCollections`.
- Modelos preparados con valores por defecto para facilitar lectura desde Firestore.
- Documentacion tecnica en `docs/FIREBASE_SETUP.md` y `docs/FIRESTORE_MODELO_DATOS.md`.
- Login con Firebase Authentication por correo institucional y contrasena.
- Registro separado con nombre, correo `@tecmilenio.mx`, contrasena y confirmacion de contrasena.
- Cierre de sesion desde Perfil.

## Siguiente Fase Recomendada

Fase 2: persistencia y usuarios reales.

Tareas sugeridas:

1. Crear una implementacion real de `MiTecmiRepository` usando Firestore.
2. Mantener el `FakeMiTecmiRepository` para pruebas locales o desarrollo rapido.
3. Agregar Firebase Storage para fotos de objetos perdidos.
4. Definir reglas de seguridad para que cada usuario solo modifique lo permitido.
5. Preparar notificaciones push para eventos y actualizaciones importantes.

## Estado General

El proyecto esta listo como prototipo de interfaz y flujo funcional. La arquitectura actual permite continuar hacia Firebase sin redisenar desde cero, porque las pantallas ya dependen del ViewModel y de una interfaz de repositorio en lugar de depender directamente de una base de datos especifica.
