from fastapi import FastAPI, BackgroundTasks
from app.agents.matching_agent import MatchingAgent
from app.services.impact_service import ImpactService
from app.services.escalation_service import EscalationService
from app.core.firebase import db
from datetime import datetime

app = FastAPI(title="RescueBite AI Operations Command Center")

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

@app.get("/dashboard/predictions")
def get_predictions():
    """
    Tier 2: Predicted Surplus Panel (AI Intelligence Layer)
    """
    # Placeholder for the Prediction Agent output
    return [
        {
            "location": "Manyata Tech Park",
            "probability": "78%",
            "estimated_meals": "40-60",
            "time": "8:30 PM",
            "trend": "rising"
        },
        {
            "location": "Wedding Hall XYZ",
            "probability": "82%",
            "estimated_meals": "100-150",
            "time": "10:00 PM",
            "trend": "stable"
        }
    ]

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
