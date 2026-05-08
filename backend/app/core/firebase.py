import firebase_admin
from firebase_admin import credentials, firestore
import os
from dotenv import load_dotenv

load_dotenv()

def init_firestore():
    # In production, use the path to your serviceAccountKey.json
    # Or use environment variables for easier deployment
    cert_path = os.getenv("FIREBASE_SERVICE_ACCOUNT_PATH", "serviceAccountKey.json")
    
    if not firebase_admin._apps:
        if os.path.exists(cert_path):
            cred = credentials.Certificate(cert_path)
            firebase_admin.initialize_app(cred)
        else:
            # Fallback for local testing if no cert is found
            print("Warning: Firebase Service Account key not found. Using default credentials.")
            firebase_admin.initialize_app()
            
    return firestore.client()

db = init_firestore()
