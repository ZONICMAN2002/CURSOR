# CURSOR

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

Abre `http://localhost:8080` en el navegador.
