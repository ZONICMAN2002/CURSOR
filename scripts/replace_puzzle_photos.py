#!/usr/bin/env python3
"""
Replace placeholder puzzle photos with your real images.

Usage:
  python3 scripts/replace_puzzle_photos.py photo1.jpg photo2.jpg photo3.jpg photo4.jpg photo5.jpg

Or copy manually to:
  - android-puzzle/app/src/main/res/drawable/puzzle_photo_1.jpg ... puzzle_photo_5.jpg
  - mobile-game/puzzle-assets/puzzle_photo_1.jpg ... puzzle_photo_5.jpg
"""

import shutil
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
TARGET_DIRS = [
    ROOT / "android-puzzle" / "app" / "src" / "main" / "res" / "drawable",
    ROOT / "mobile-game" / "puzzle-assets",
]


def main() -> None:
    if len(sys.argv) != 6:
        print(__doc__)
        sys.exit(1)

    for i, src in enumerate(sys.argv[1:6], start=1):
        src_path = Path(src)
        if not src_path.is_file():
            print(f"Error: no existe {src}")
            sys.exit(1)
        name = f"puzzle_photo_{i}.jpg"
        for dest_dir in TARGET_DIRS:
            dest = dest_dir / name
            dest_dir.mkdir(parents=True, exist_ok=True)
            shutil.copy2(src_path, dest)
            print(f"Copiado → {dest}")

    print("Listo. Recompila la app Android o actualiza la web.")


if __name__ == "__main__":
    main()
