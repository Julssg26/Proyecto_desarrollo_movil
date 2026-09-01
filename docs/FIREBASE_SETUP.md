# Firebase Setup

Este proyecto ya tiene preparadas las dependencias base de Firebase para Android:

- Firebase Authentication.
- Cloud Firestore.
- Firebase Cloud Messaging.
- Firebase Storage.

El plugin de Google Services ya esta declarado y aplicado porque el proyecto cuenta con `app/google-services.json`.

## Pasos Para Conectar Firebase

1. Entrar a Firebase Console.
2. Crear o abrir el proyecto Firebase de Mi Tecmi.
3. Agregar una app Android con el package name `com.optimizare.mitecmi`.
4. Descargar `google-services.json`.
5. Colocar el archivo en `app/google-services.json`.
6. Abrir `app/build.gradle.kts` y verificar que el plugin este aplicado:

```kotlin
alias(libs.plugins.google.services)
```

7. Sincronizar Gradle desde Android Studio.
8. Ejecutar la app en emulador o dispositivo.

## Servicios A Activar

En Firebase Console deben activarse:

- Authentication: proveedor inicial recomendado, correo/contrasena.
- Firestore Database: modo production si ya se definiran reglas, modo test solo para pruebas controladas.
- Cloud Messaging: queda disponible al registrar la app Android.
- Storage: necesario para fotos de objetos perdidos y posiblemente imagenes de clubes/eventos.

## Notas

- `google-services.json` conecta la app Android con el proyecto Firebase correcto.
- El `applicationId` debe coincidir con el `package_name` dentro de `google-services.json`.
- El namespace/codigo Kotlin puede seguir usando `com.julm.mitecmi`; no necesita coincidir con Firebase mientras el `applicationId` sea correcto.
- Las reglas de seguridad deben definirse antes de usar datos reales.
- El login ya usa Firebase Authentication.
- El registro de usuarios valida que el correo termine en `@tecmilenio.mx` y guarda el nombre como display name del usuario en Firebase Auth.
- La app todavia usa `FakeMiTecmiRepository` para datos de comunidad; el siguiente paso tecnico es crear una implementacion real de `MiTecmiRepository` usando Firestore.
