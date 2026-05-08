from fastapi import FastAPI, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from .agents.matching_agent import MatchingAgent
from .services.impact_service import ImpactService
from .services.escalation_service import EscalationService
from .core.firebase import db
from datetime import datetime

app = FastAPI(title="RescueBite AI Operations Command Center")

# CORS Configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173", "http://localhost:5174", "http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

matching_agent = MatchingAgent()

@app.get("/")
def read_root():
    return {"status": "RescueBite AI Backend Online"}

@app.get("/dashboard/metrics")
def get_metrics():
    """
    Tier 1: Real-Time Metrics Row
    """
    return ImpactService.get_dashboard_metrics()

@app.post("/donations/{donation_id}/match")
def trigger_matching(donation_id: str, background_tasks: BackgroundTasks):
    """
    Core AI: Manually trigger the matching agent for a donation.
    """
    background_tasks.add_task(matching_agent.match_donation, donation_id)
    return {"message": "Matching process started in background", "donation_id": donation_id}

from .services.prediction_service import PredictionService

@app.get("/dashboard/predictions")
def get_predictions():
    """
    Tier 2: Predicted Surplus Panel (AI Intelligence Layer)
    """
    return PredictionService.get_forecasts()

@app.post("/system/maintenance/check-escalations")
def run_escalations():
    """
    Tier 3: Emergency Rescue Mode Trigger
    """
    EscalationService.monitor_expiries()
    return {"message": "Escalation check complete"}

@app.get("/dashboard/decision-logs")
def get_decision_logs():
    """
    Explainable AI (XAI) Decision Log
    """
    logs = db.collection('decision_logs').order_by('timestamp', direction='DESCENDING').limit(10).stream()
    return [log.to_dict() for log in logs]

@app.get("/ngos")
def get_ngos():
    ngos = db.collection('ngos').where('is_active', '==', True).stream()
    return [{"id": n.id, **n.to_dict()} for n in ngos]

@app.get("/volunteers")
def get_volunteers():
    volunteers = db.collection('volunteers').where('is_active', '==', True).stream()
    return [{"id": v.id, **v.to_dict()} for v in volunteers]

@app.get("/donations")
def get_donations():
    donations = db.collection('donations').where('status', 'in', ['pending', 'matched']).stream()
    return [{"id": d.id, **d.to_dict()} for d in donations]
