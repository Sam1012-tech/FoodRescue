from app.core.firebase import db
from datetime import datetime
import math

class MatchingAgent:
    def __init__(self):
        self.weights = {
            "distance": 0.4,
            "urgency": 0.3,
            "capacity": 0.2,
            "reliability": 0.1
        }

    def calculate_score(self, donation, ngo):
        """
        Implements the Matching Formula:
        MatchingScore = w1(1/Distance) + w2(Urgency) + w3(CapacityMatch) + w4(Reliability)
        """
        # 1. Distance Score (Simplified Haversine)
        dist = self._get_distance(donation['location'], ngo['location'])
        distance_score = 1 / max(dist, 0.1) # Avoid division by zero
        
        # 2. Urgency Score (Based on time left)
        time_left = (donation['expiry_time'] - datetime.now().timestamp()) / 3600
        urgency_score = 1 / max(time_left, 0.5)
        
        # 3. Capacity Match
        capacity_score = 1.0 if ngo['capacity'] >= donation['quantity'] else (ngo['capacity'] / donation['quantity'])
        
        # 4. Reliability
        reliability_score = ngo.get('reliability', 0.8)
        
        # Total Weighted Score
        final_score = (
            self.weights['distance'] * distance_score +
            self.weights['urgency'] * urgency_score +
            self.weights['capacity'] * capacity_score +
            self.weights['reliability'] * reliability_score
        )
        
        # Decision Log (Explainable AI)
        decision_log = {
            "ngo_id": ngo['id'],
            "final_score": round(final_score, 2),
            "breakdown": {
                "distance": round(distance_score, 2),
                "urgency": round(urgency_score, 2),
                "capacity": round(capacity_score, 2)
            },
            "reason": f"Matched {ngo['name']} due to high { 'proximity' if distance_score > 5 else 'capacity match' }."
        }
        
        return final_score, decision_log

    def _get_distance(self, loc1, loc2):
        # Simplified distance for demo (lat/lng math)
        return math.sqrt((loc1.latitude - loc2.latitude)**2 + (loc1.longitude - loc2.longitude)**2) * 111

    def match_donation(self, donation_id):
        donation_ref = db.collection('donations').document(donation_id)
        donation = donation_ref.get().to_dict()
        donation['id'] = donation_id
        
        # Get all active NGOs
        ngos = db.collection('ngos').where('is_active', '==', True).stream()
        
        best_match = None
        highest_score = -1
        decision_logs = []
        
        for ngo_doc in ngos:
            ngo = ngo_doc.to_dict()
            ngo['id'] = ngo_doc.id
            score, log = self.calculate_score(donation, ngo)
            decision_logs.append(log)
            
            if score > highest_score:
                highest_score = score
                best_match = ngo
        
        if best_match:
            # Create Mission
            mission_data = {
                "donation_id": donation_id,
                "ngo_id": best_match['id'],
                "status": "matched",
                "assigned_at": datetime.now(),
                "ai_decision": decision_logs[0] # Best decision
            }
            db.collection('missions').add(mission_data)
            donation_ref.update({"status": "matched"})
            
            # Save Decision Logs for XAI panel
            db.collection('decision_logs').add({
                "donation_id": donation_id,
                "all_candidates": decision_logs,
                "timestamp": datetime.now()
            })
            
            return best_match['id']
        return None
