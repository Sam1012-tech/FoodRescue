import firebase_admin
from firebase_admin import credentials, firestore
import os
from dotenv import load_dotenv
from pathlib import Path

# Smart .env loading: Look in current dir and parent dir (root)
env_path = Path('.') / '.env'
if not env_path.exists():
    env_path = Path('..') / '.env'
load_dotenv(dotenv_path=env_path)

def init_firestore():
    # Smart Key Discovery: Check environment variable, then common local paths
    cert_path = os.getenv("FIREBASE_SERVICE_ACCOUNT_PATH", "backend/serviceAccountKey.json")
    
    # If the default path fails, try looking in the current directory (case where user is already in /backend)
    search_paths = [cert_path, "serviceAccountKey.json", "../serviceAccountKey.json"]
    
    final_path = None
    for path in search_paths:
        if os.path.exists(path):
            final_path = path
            break

    if not firebase_admin._apps:
        if final_path:
            print(f"✅ Firebase initialized using: {final_path}")
            cred = credentials.Certificate(final_path)
            firebase_admin.initialize_app(cred)
        else:
            print("⚠️ Warning: Firebase Service Account key not found. Using default credentials.")
            firebase_admin.initialize_app()
            
    return firestore.client()

db = init_firestore()
