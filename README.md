# Mi Tecmi

Aplicacion Android para centralizar servicios y comunicacion de la comunidad estudiantil Tecmilenio. El proyecto funciona actualmente como un prototipo funcional construido con Kotlin y Jetpack Compose.

Repositorio: https://github.com/Julssg26/Proyecto_desarrollo_movil

## Objetivo

Mi Tecmi busca servir como un hub universitario donde estudiantes puedan consultar eventos, participar en propuestas y encuestas, revisar anuncios, descubrir clubes y publicar objetos perdidos o encontrados.

El prototipo esta pensado como base para una segunda fase con Firebase/Firestore, autenticacion y notificaciones.

## Estado Actual

El proyecto ya cuenta con una primera version funcional para pruebas en emulador o dispositivo Android.

Funcionalidades disponibles:

- Login con Firebase Authentication por correo institucional y contrasena.
- Registro separado con nombre, correo `@tecmilenio.mx`, contrasena y confirmacion de contrasena.
- Navegacion inferior con Inicio, Buscar, Eventos, Comunidad y Perfil.
- Feed principal con contenido de eventos, propuestas, encuestas, anuncios, clubes y objetos perdidos.
- Busqueda local sobre el contenido disponible.
- Listado de eventos y marcado de interes.
- Creacion y votacion de propuestas.
- Encuestas con voto unico durante la sesion y resultados en pantalla.
- Directorio de clubes con pantalla de detalle.
- Publicacion y detalle de objetos perdidos o encontrados.
- Perfil con resumen de actividad del estudiante.

Limitacion principal:

- Los datos se almacenan en memoria usando un repositorio local de prueba. Si la app se cierra por completo, los cambios vuelven al estado inicial.

## Tecnologia

- Android Studio
- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel
- StateFlow / MutableStateFlow
- Gradle Kotlin DSL

Configuracion Android:

- `applicationId`: `com.optimizare.mitecmi`
- Namespace Kotlin: `com.julm.mitecmi`
- `minSdk`: 24
- `targetSdk`: 37
- `compileSdk`: 37

## Arquitectura

El proyecto sigue una estructura tipo MVVM para separar interfaz, estado y origen de datos.

Carpetas principales:

- `app/src/main/java/com/julm/mitecmi/model`: modelos de datos.
- `app/src/main/java/com/julm/mitecmi/repository`: interfaz de repositorio y repositorio local de prueba.
- `app/src/main/java/com/julm/mitecmi/viewmodel`: ViewModel principal de la app.
- `app/src/main/java/com/julm/mitecmi/navigation`: rutas y navegacion Compose.
- `app/src/main/java/com/julm/mitecmi/ui/screens`: pantallas de la aplicacion.
- `app/src/main/java/com/julm/mitecmi/ui/components`: componentes reutilizables.
- `app/src/main/java/com/julm/mitecmi/ui/theme`: tema visual de Compose.

Esta separacion permite reemplazar el repositorio local por una implementacion con Firebase/Firestore sin rehacer todas las pantallas.

## Como Ejecutar

Requisitos:

- Android Studio instalado.
- JDK compatible con el proyecto.
- Emulador Android o dispositivo fisico.

Pasos recomendados:

1. Abrir el proyecto en Android Studio.
2. Esperar a que Gradle sincronice dependencias.
3. Seleccionar un emulador o dispositivo Android.
4. Ejecutar la app con el boton Run.

Tambien se puede compilar desde terminal:

```bash
./gradlew :app:assembleDebug
```

Para ejecutar pruebas unitarias:

```bash
./gradlew :app:testDebugUnitTest
```

## Pruebas Manuales Recomendadas

- Cambiar entre Inicio, Buscar, Eventos, Comunidad y Perfil desde la barra inferior.
- En Inicio, usar filtros y abrir propuestas, encuestas, clubes u objetos.
- Marcar y desmarcar interes en eventos y revisar el contador.
- Crear una propuesta desde Comunidad, abrirla y votar o retirar el voto.
- Responder una encuesta y comprobar que se muestran resultados.
- Abrir un club y revisar descripcion, categoria, horario y contacto.
- Publicar un objeto perdido o encontrado y abrir su detalle.
- Usar Buscar con terminos como `Hackathon`, `club`, `laptop` o `biblioteca`.
- Revisar Perfil despues de realizar acciones.

Nota: el Compose Preview no debe tomarse como prueba final. La validacion real debe hacerse ejecutando la app en emulador o dispositivo, porque la navegacion, el ViewModel y los cambios de estado dependen de la ejecucion completa.

## Fase 2

Pendientes principales:

- Conectar Firestore como fuente de datos persistente.
- Guardar propuestas, votos, encuestas, eventos, objetos y anuncios en la nube.
- Validar votos unicos por usuario con datos reales.
- Agregar Firebase Storage para fotografias de objetos perdidos.
- Implementar notificaciones push para eventos, propuestas o encuestas relevantes.
- Revisar reglas de seguridad y permisos de Firebase.

Documentacion tecnica:

- `docs/FIREBASE_SETUP.md`
- `docs/FIRESTORE_MODELO_DATOS.md`
