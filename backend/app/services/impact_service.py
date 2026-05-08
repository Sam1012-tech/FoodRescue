from ..core.firebase import db

class ImpactService:
    @staticmethod
    def get_dashboard_metrics():
        """
        Calculates the real-time metrics row for the dashboard.
        """
        # In a real app, these would be aggregated periodically or using Firestore count()
        donations = db.collection('donations').where('status', '==', 'delivered').stream()
        
        total_meals = 0
        co2_saved = 0
        
        for doc in donations:
            data = doc.to_dict()
            servings = data.get('quantity', 0)
            total_meals += servings
            # Approx 2.5kg CO2 saved per 1kg/4 meals prevented waste
            co2_saved += (servings / 4) * 2.5
            
        return {
            "meals_rescued": total_meals,
            "co2_saved_kg": round(co2_saved, 2),
            "active_deliveries": ImpactService._get_active_count(),
            "ngo_partners": 37, # Simulated for demo
            "volunteers_active": 22 # Simulated for demo
        }

    @staticmethod
    def _get_active_count():
        active = db.collection('missions').where('status', 'in', ['matched', 'picked_up']).stream()
        return len(list(active))
