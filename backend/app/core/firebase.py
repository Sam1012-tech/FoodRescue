import firebase_admin
from firebase_admin import credentials, firestore
import os
from dotenv import load_dotenv
from pathlib import Path

# ── Load .env from project root (works whether you run from /backend or /) ──
_here = Path(__file__).resolve().parent          # .../backend/app/core
_backend_root = _here.parent.parent             # .../backend
_project_root = _backend_root.parent            # .../FoodRescue-

for _env_file in [_backend_root / ".env", _project_root / ".env"]:
    if _env_file.exists():
        load_dotenv(dotenv_path=_env_file)
        break

def init_firestore():
    # 1. Check env variable first (CI / production override)
    env_path = os.getenv("FIREBASE_SERVICE_ACCOUNT_PATH")

    # 2. Search well-known local paths (relative to this file's location)
    candidates = [
        Path(env_path) if env_path else None,
        _backend_root / "serviceAccountKey.json",
        _project_root / "serviceAccountKey.json",
        _project_root / "backend" / "serviceAccountKey.json",
    ]

    final_path = None
    for p in candidates:
        if p and p.exists():
            final_path = p
            break

    if not firebase_admin._apps:
        if final_path:
            print(f"✅ Firebase: using service account at {final_path}")
            cred = credentials.Certificate(str(final_path))
            firebase_admin.initialize_app(cred)
        else:
            raise RuntimeError(
                "\n\n"
                "❌ Firebase service account key NOT FOUND.\n"
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                "Fix in 2 steps:\n\n"
                "  1. Download your key:\n"
                "     → console.firebase.google.com\n"
                "     → Project foodrescue-34370\n"
                "     → Project Settings → Service Accounts\n"
                "     → 'Generate new private key'\n\n"
                "  2. Save the file as:\n"
                "     FoodRescue-/backend/serviceAccountKey.json\n\n"
                "  OR set the env variable:\n"
                "     FIREBASE_SERVICE_ACCOUNT_PATH=/path/to/key.json\n"
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
            )

    return firestore.client()

db = init_firestore()

