import joblib
import os
import pandas as pd
from datetime import datetime
from ..core.firebase import db

class PredictionService:
    _model = None
    _model_path = os.path.join(os.path.dirname(__file__), "..", "..", "..", "ml", "models", "surplus_intelligence_engine.joblib")

    @classmethod
    def load_model(cls):
        if cls._model is None:
            try:
                if os.path.exists(cls._model_path):
                    cls._model = joblib.load(cls._model_path)
                    print(f"✅ Surplus Intelligence Engine loaded successfully.")
                else:
                    print(f"⚠️ Model file not found at {cls._model_path}. Using heuristic fallback.")
            except Exception as e:
                print(f"❌ Error loading ML model: {e}. Using heuristic fallback.")
        return cls._model

    @staticmethod
    def get_forecasts():
        """
        Generates forecasts for known upcoming events or hotspots.
        In a real app, this would query a 'events' collection and run the model on each.
        """
        model = PredictionService.load_model()
        
        # Simulated upcoming hotspots for the demo
        hotspots = [
            {"location": "Manyata Tech Park", "base_servings": 100, "hour": 20, "type": "tech_park"},
            {"location": "Electronic City P1", "base_servings": 250, "hour": 19, "type": "tech_park"},
            {"location": "Whitefield ITPL", "base_servings": 80, "hour": 21, "type": "corporate"},
        ]
        
        predictions = []
        for spot in hotspots:
            # If model is loaded, we'd use it here:
            # prob = model.predict_proba(features)
            # For the demo, we'll use the model to 'influence' the probability
            probability = 75 if model else 60
            
            # Add some variability based on time of day
            hour_factor = 1.2 if spot['hour'] > 18 else 0.8
            final_prob = min(int(probability * hour_factor), 98)
            
            predictions.append({
                "location": spot['location'],
                "probability": f"{final_prob}%",
                "estimated_meals": int(spot['base_servings'] * (final_prob/100)),
                "time": f"{spot['hour']}:00 PM",
                "trend": "up" if final_prob > 70 else "stable"
            })
            
        return predictions
