# CURSOR

## Horizontes Puzzle — Rompecabezas Android

Juego de rompecabezas con 5 fotos del equipo Financiera Horizontes y dificultades 3×3 hasta 6×6.

### Jugar en el navegador (teléfono)

Abre: **https://zonicman2002.github.io/CURSOR/puzzle.html**

(Si el enlace no carga, activa Pages una vez: **Settings → Pages → Branch `gh-pages` / root**.)

### Instalar APK en Android

Ver `android-puzzle/README.md` — compila con Android Studio o `./gradlew assembleDebug`.

### Reemplazar con tus fotos

```bash
python3 scripts/replace_puzzle_photos.py foto1.jpg foto2.jpg foto3.jpg foto4.jpg foto5.jpg
```

---

## Aero Fall — Jugar en el navegador

Para jugar **sin descargar** el archivo cada vez:

### Paso 1 — Activar GitHub Pages (solo una vez)

1. Ve a **Settings** → **Pages** en el repositorio
2. En **Build and deployment** → **Source**, elige **GitHub Actions**
3. Guarda los cambios

### Paso 2 — Abre el juego

Cuando el workflow termine, el juego estará en:

**https://zonicman2002.github.io/CURSOR/**

Cada vez que se actualice el juego en `main`, se publica solo. No hace falta volver a descargar nada.

### Requisito importante

Si el repositorio es **privado**, GitHub Pages solo funciona con plan de pago. Para un enlace gratis sin descargas, el repo debe ser **público**:

**Settings** → **General** → **Danger Zone** → **Change repository visibility** → **Public**

---

### Jugar en local (alternativa)

```bash
cd mobile-game
python3 -m http.server 8080
```

Abre `http://localhost:8080` (Aero Fall) o `http://localhost:8080/puzzle.html` (rompecabezas).
