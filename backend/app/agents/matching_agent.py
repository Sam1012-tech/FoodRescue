from ..core.firebase import db
from datetime import datetime, timedelta
import math

class MatchingAgent:
    def __init__(self):
        # NGO Scoring Weights
        self.ngo_weights = {
            "distance": 0.25,
            "demand": 0.20,
            "storage": 0.15,
            "reliability": 0.10,
            "freshness": 0.15,
            "volunteer_prox": 0.10,
            "traffic_penalty": 0.05
        }
        
        # Volunteer Scoring Weights
        self.vol_weights = {
            "distance": 0.40,
            "reliability": 0.30,
            "availability": 0.20,
            "workload_penalty": 0.10
        }

    def calculate_ngo_score(self, donation, ngo):
        """
        Tier 1: Smart NGO Matching Logic
        Ranking NGOs using weighted multi-factor scoring.
        """
        # 1. Distance & ETA (Simplified)
        dist = self._get_distance(donation['location'], ngo['location'])
        distance_score = 10 / max(dist, 1) # Higher score for closer
        
        # 2. Demand/Meals Needed
        demand_score = (ngo.get('current_demand', 0) / 100) * 10
        
        # 3. Available Storage
        storage_score = 10 if ngo.get('capacity', 0) >= donation['quantity'] else (ngo.get('capacity', 0) / donation['quantity']) * 10
        
        # 4. Reliability
        reliability_score = ngo.get('reliability', 0.8) * 10
        
        # 5. Food Freshness & Spoilage (Emergency Scaling)
        time_left = (donation['expiry_time'] - datetime.now().timestamp()) / 60 # in minutes
        freshness_score = 10 if time_left > 120 else (time_left / 120) * 10
        
        # 6. Cooking Status Penalty
        cooking_penalty = 3 if ngo.get('is_cooking', False) else 0
        
        # Final Aggregate Score
        final_score = (
            self.ngo_weights['distance'] * distance_score +
            self.ngo_weights['demand'] * demand_score +
            self.ngo_weights['storage'] * storage_score +
            self.ngo_weights['reliability'] * reliability_score +
            self.ngo_weights['freshness'] * freshness_score
        ) - cooking_penalty

        return round(final_score, 2), {
            "distance": round(distance_score, 1),
            "demand": round(demand_score, 1),
            "storage": round(storage_score, 1),
            "reliability": round(reliability_score, 1),
            "reason": self._generate_reason(distance_score, demand_score, storage_score, cooking_penalty)
        }

    def calculate_volunteer_score(self, donation, volunteer):
        """
        Tier 2: Volunteer Matching Logic
        Finds best volunteer based on proximity and reliability.
        """
        dist = self._get_distance(donation['location'], volunteer['current_location'])
        distance_score = 10 / max(dist, 0.5)
        
        reliability_score = volunteer.get('reliability', 0.8) * 10
        
        # Workload penalty (if they already have assignments)
        workload_penalty = volunteer.get('current_assignments', 0) * 2
        
        final_score = (
            self.vol_weights['distance'] * distance_score +
            self.vol_weights['reliability'] * reliability_score +
            self.vol_weights['availability'] * 10
        ) - workload_penalty
        
        return round(final_score, 2)

    def match_donation(self, donation_id):
        donation_ref = db.collection('donations').document(donation_id)
        donation = donation_ref.get().to_dict()
        donation['id'] = donation_id
        
        # CASE 5: Food near expiry (Trigger Emergency Mode)
        time_left_mins = (donation['expiry_time'] - datetime.now().timestamp()) / 60
        is_emergency = time_left_mins < 45

        # 1. Rank NGOs
        ngos = db.collection('ngos').where('is_active', '==', True).stream()
        ranked_ngos = []
        for doc in ngos:
            ngo_data = doc.to_dict()
            ngo_data['id'] = doc.id
            score, details = self.calculate_ngo_score(donation, ngo_data)
            ranked_ngos.append({"ngo": ngo_data, "score": score, "details": details})
        
        ranked_ngos.sort(key=lambda x: x['score'], reverse=True)

        # CASE 3: No NGO available (Escalation)
        if not ranked_ngos or ranked_ngos[0]['score'] < 3:
            return self._trigger_escalation(donation)

        selected_match = ranked_ngos[0]
        
        # CASE 1 & 6: Split donation if quantity is large or NGO overloaded
        if donation['quantity'] > selected_match['ngo'].get('capacity', 0) and len(ranked_ngos) > 1:
            return self._handle_split_donation(donation, ranked_ngos)

        # 2. Find Volunteer
        volunteers = db.collection('volunteers').where('status', '==', 'idle').stream()
        best_vol = None
        highest_vol_score = -1
        
        for v_doc in volunteers:
            v_data = v_doc.to_dict()
            v_data['id'] = v_doc.id
            v_score = self.calculate_volunteer_score(donation, v_data)
            if v_score > highest_vol_score:
                highest_vol_score = v_score
                best_vol = v_data

        # Final Assignment
        mission_id = self._create_mission(donation, selected_match, best_vol, is_emergency)
        return {"mission_id": mission_id, "ngo": selected_match['ngo']['name'], "volunteer": best_vol['name'] if best_vol else "Searching..."}

    def _create_mission(self, donation, match, volunteer, is_emergency):
        mission_data = {
            "donation_id": donation['id'],
            "ngo_id": match['ngo']['id'],
            "volunteer_id": volunteer['id'] if volunteer else None,
            "status": "emergency_rescue" if is_emergency else "matched",
            "assigned_at": datetime.now(),
            "ai_explanation": {
                "selected_ngo": match['ngo']['name'],
                "score": match['score'],
                "reasons": match['details']['reason']
            }
        }
        _, doc_ref = db.collection('missions').add(mission_data)
        
        # Log decision for XAI Panel
        db.collection('decision_logs').add({
            "action": "NGO Matching",
            "details": f"Optimized for {match['ngo']['name']} (Score: {match['score']})",
            "timestamp": datetime.now(),
            "is_emergency": is_emergency
        })
        
        db.collection('donations').document(donation['id']).update({"status": "matched"})
        return doc_ref.id

    def _handle_split_donation(self, donation, ranked_ngos):
        # Logic to split between top 2 NGOs
        ngo1 = ranked_ngos[0]['ngo']
        ngo2 = ranked_ngos[1]['ngo']
        # Simplified split
        print(f"Splitting donation between {ngo1['name']} and {ngo2['name']}")
        # In a real system, we'd create two missions here
        return self._create_mission(donation, ranked_ngos[0], None, False)

    def _trigger_escalation(self, donation):
        print("ALERT: No optimal NGO found. Escalating to community kitchens...")
        # Emergency logic to notify all nearby users/shelters
        return "escalated"

    def _generate_reason(self, dist, demand, storage, penalty):
        reasons = []
        if dist > 7: reasons.append("Highest proximity")
        if demand > 7: reasons.append("Urgent local demand")
        if storage > 8: reasons.append("Sufficient storage capacity")
        if penalty > 0: reasons.append("Adjusted for active cooking status")
        return reasons if reasons else ["Balanced optimal match"]

    def _get_distance(self, loc1, loc2):
        return math.sqrt((loc1.latitude - loc2.latitude)**2 + (loc1.longitude - loc2.longitude)**2) * 111
