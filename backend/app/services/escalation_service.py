from ..core.firebase import db
from datetime import datetime, timedelta

class EscalationService:
    @staticmethod
    def monitor_expiries():
        """
        Background task to check for near-expiry donations.
        If a donation is within 30 mins of expiry and NOT matched, 
        trigger 'Emergency Mode'.
        """
        now = datetime.now().timestamp()
        buffer_time = now + (30 * 60) # 30 mins from now
        
        # Query unmatched donations expiring soon
        query = db.collection('donations')\
            .where('status', '==', 'pending')\
            .where('expiry_time', '<=', buffer_time)\
            .stream()
            
        for doc in query:
            donation = doc.to_dict()
            EscalationService.trigger_emergency_mode(doc.id, donation)

    @staticmethod
    def trigger_emergency_mode(donation_id, donation):
        print(f"🚨 EMERGENCY MODE ACTIVATED for {donation_id}")
        
        # 1. Update status to emergency
        db.collection('donations').document(donation_id).update({
            "status": "emergency",
            "is_escalated": True
        })
        
        # 2. Log in live rescue feed
        db.collection('live_feed').add({
            "type": "emergency",
            "message": f"Emergency rescue triggered for {donation.get('food_type')} at {donation.get('location_name', 'Unknown')}",
            "timestamp": datetime.now()
        })
        
        # 3. Notification Agent would broadcast to ALL nearby volunteers here
        # (Stub for notification logic)
