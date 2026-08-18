# Horizontes Puzzle — Android

Rompecabezas con las 5 fotos del equipo Financiera Horizontes y cuatro niveles de dificultad.

## Características

- **5 niveles** con fotos distintas (oficina, restaurante, limpieza, etc.)
- **4 dificultades**: Fácil (3×3), Medio (4×4), Difícil (5×5), Experto (6×6)
- Arrastra piezas para intercambiarlas hasta completar la imagen
- Vista previa de la foto completa
- Registro de mejores movimientos y tiempos por nivel
- Versión web en `mobile-game/puzzle.html` (jugar en el navegador del teléfono)

## Requisitos para compilar el APK

- Android Studio Ladybug o superior, o SDK Android 34 + JDK 17
- Conectar un teléfono o usar un emulador

## Compilar e instalar

```bash
cd android-puzzle
./gradlew assembleDebug
```

El APK estará en:

`app/build/outputs/apk/debug/app-debug.apk`

Instalar en el dispositivo:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

O abre el proyecto `android-puzzle` en Android Studio y pulsa **Run**.

## Usar tus fotos originales

Los assets actuales son placeholders generados. Para usar las 5 fotos que enviaste:

```bash
python3 scripts/replace_puzzle_photos.py foto1.jpg foto2.jpg foto3.jpg foto4.jpg foto5.jpg
```

Luego vuelve a compilar la app.

## Jugar en el navegador (sin instalar)

```bash
cd mobile-game
python3 -m http.server 8080
```

Abre `http://localhost:8080/puzzle.html` en el teléfono o en el navegador.

Si GitHub Pages está activo en el repositorio, también puedes usar:

`https://zonicman2002.github.io/CURSOR/puzzle.html`

## Estructura

| Ruta | Descripción |
|------|-------------|
| `MainActivity` | Selección de nivel y dificultad |
| `PuzzleActivity` | Pantalla del rompecabezas |
| `PuzzleBoardView` | Tablero con arrastre táctil |
| `PuzzleEngine` | Lógica de mezcla y victoria |
| `res/drawable/puzzle_photo_*.jpg` | Imágenes de cada nivel |
