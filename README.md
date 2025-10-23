# MatchPet Android App

Aplicación móvil desarrollada con Kotlin y Jetpack Compose para el ecosistema MatchPet. Incluye flujos de registro, inicio de sesión (con soporte para Google Sign-In), persistencia de sesión con SharedPreferences y administración del perfil del usuario bajo la arquitectura MVVM.

## Módulos principales
- **`app`**: Aplicación Android con Compose, navegación y conexión a la API REST a través de Retrofit.

## Funcionalidades
- Registro y autenticación de usuarios con validaciones y mensajes de error amigables.
- Inicio de sesión tradicional y con Google utilizando `GoogleSignInClient`.
- Persistencia del token JWT en `SharedPreferences` mediante un `SessionManager` reutilizable.
- Recuperación y edición del perfil del usuario con sincronización remota.
- Estilo consistente con la paleta proporcionada (azules, coral y rosa claro).

## Dependencias destacadas
- Jetpack Compose y Navigation Compose.
- Retrofit + Moshi y OkHttp Logging Interceptor.
- Google Play Services Auth para el flujo de Google Sign-In.

## Configuración
1. Actualiza el valor de `default_web_client_id` en `app/src/main/res/values/strings.xml` con el ID generado en la consola de Firebase/Google.
2. Ajusta la URL base en `DefaultAppContainer` para apuntar al backend correspondiente.
3. Abre el proyecto en Android Studio para sincronizar dependencias y ejecutar la aplicación.
