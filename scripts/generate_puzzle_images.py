#!/usr/bin/env python3
"""Generate puzzle photo assets for Horizontes Puzzle (replace with real photos in assets/)."""

from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent.parent
SIZE = (1200, 900)

LEVELS = [
    {
        "file": "puzzle_photo_1.jpg",
        "title": "Equipo Horizontes",
        "subtitle": "4 ago. 2026 · Oficina Elmer Faucett",
        "colors": ("#1e3a5f", "#4a90c2", "#e8eef4"),
    },
    {
        "file": "puzzle_photo_2.jpg",
        "title": "Almuerzo del equipo",
        "subtitle": "3 ago. 2026 · 376 Union, Pucallpa",
        "colors": ("#2d5016", "#7cb342", "#f5f0e6"),
    },
    {
        "file": "puzzle_photo_3.jpg",
        "title": "Día de limpieza",
        "subtitle": "14 jul. 2026 · Oficina Elmer Faucett",
        "colors": ("#5c2d4a", "#c94b7a", "#fce4ec"),
    },
    {
        "file": "puzzle_photo_4.jpg",
        "title": "Uniforme corporativo",
        "subtitle": "11 jul. 2026 · Pucallpa, Ucayali",
        "colors": ("#1a237e", "#3f51b5", "#e8eaf6"),
    },
    {
        "file": "puzzle_photo_5.jpg",
        "title": "Sucursal Masisea",
        "subtitle": "7 jul. 2026 · Aviación, Pucallpa",
        "colors": ("#4a3728", "#8d6e63", "#efebe9"),
    },
]

OUT_DIRS = [
    ROOT / "android-puzzle" / "app" / "src" / "main" / "res" / "drawable",
    ROOT / "mobile-game" / "puzzle-assets",
]


def load_font(size: int) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    candidates = [
        "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",
        "/usr/share/fonts/truetype/liberation/LiberationSans-Bold.ttf",
    ]
    for path in candidates:
        if Path(path).exists():
            return ImageFont.truetype(path, size)
    return ImageFont.load_default()


def draw_banner(draw: ImageDraw.ImageDraw, w: int, font_sm: ImageFont.ImageFont) -> None:
    banner = "FINANCIERA Horizontes · Creciendo Juntos"
    draw.rectangle((0, 0, w, 72), fill="#0d47a1")
    draw.text((24, 20), banner, fill="#ffffff", font=font_sm)


def make_image(level: dict) -> Image.Image:
    w, h = SIZE
    c0, c1, c2 = level["colors"]
    img = Image.new("RGB", SIZE, c0)
    draw = ImageDraw.Draw(img)

    for i in range(h):
        t = i / h
        r = int(int(c0[1:3], 16) * (1 - t) + int(c1[1:3], 16) * t)
        g = int(int(c0[3:5], 16) * (1 - t) + int(c1[3:5], 16) * t)
        b = int(int(c0[5:7], 16) * (1 - t) + int(c1[5:7], 16) * t)
        draw.line([(0, i), (w, i)], fill=(r, g, b))

    draw_banner(draw, w, load_font(28))

    # Decorative "team" silhouettes
    base_y = h - 180
    for i, x in enumerate(range(120, w - 80, 140)):
        body_h = 140 + (i % 3) * 15
        draw.ellipse((x, base_y - 50, x + 60, base_y + 10), fill=c2)
        draw.rounded_rectangle(
            (x + 8, base_y, x + 52, base_y + body_h),
            radius=12,
            fill="#dfe6ef",
        )

    draw.rectangle((0, h - 120, w, h), fill="#b71c1c")
    draw.text((40, h - 95), "HORIZONTES", fill="#ffd54f", font=load_font(52))

    title_font = load_font(44)
    sub_font = load_font(26)
    draw.text((40, h - 200), level["title"], fill="#ffffff", font=title_font)
    draw.text((40, h - 155), level["subtitle"], fill="#e0e0e0", font=sub_font)

    return img


def main() -> None:
    for out_dir in OUT_DIRS:
        out_dir.mkdir(parents=True, exist_ok=True)

    for level in LEVELS:
        img = make_image(level)
        for out_dir in OUT_DIRS:
            path = out_dir / level["file"]
            img.save(path, "JPEG", quality=92)
            print(f"Wrote {path}")


if __name__ == "__main__":
    main()
