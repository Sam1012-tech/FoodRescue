from .core.firebase import db
from datetime import datetime, timedelta
from firebase_admin import firestore

def seed_database():
    print("Seeding Firestore with sample data...")

    # 1. Add NGOs
    ngos = [
        {
            "name": "City Food Bank",
            "location": firestore.GeoPoint(12.9716, 77.5946),
            "capacity": 200,
            "is_active": True,
            "reliability": 0.95,
            "current_demand": 150,
            "is_cooking": False,
            "available_storage": 100
        },
        {
            "name": "Helping Hands NGO",
            "location": firestore.GeoPoint(12.9352, 77.6245),
            "capacity": 100,
            "is_active": True,
            "reliability": 0.88,
            "current_demand": 80,
            "is_cooking": True, # Busy cooking, should lower priority
            "available_storage": 20
        }
    ]

    for ngo in ngos:
        db.collection('ngos').add(ngo)
        print(f"Added NGO: {ngo['name']}")

    # 2. Add Volunteers
    volunteers = [
        {
            "name": "Arjun Rider",
            "current_location": firestore.GeoPoint(12.9500, 77.6000),
            "status": "idle",
            "is_active": True,
            "reliability": 0.92,
            "current_assignments": 0
        },
        {
            "name": "Priya Delivery",
            "current_location": firestore.GeoPoint(13.0100, 77.6500),
            "status": "idle",
            "is_active": True,
            "reliability": 0.98,
            "current_assignments": 1 # Busy, should lower priority
        }
    ]
    
    for v in volunteers:
        db.collection('volunteers').add(v)
        print(f"Added Volunteer: {v['name']}")

    # 3. Add a sample donation expiring soon (Trigger Emergency Rescue Mode)
    expiry = datetime.now() + timedelta(minutes=30)
    donation = {
        "food_type": "Veg Biryani",
        "quantity": 50,
        "location": firestore.GeoPoint(13.0100, 77.6500),
        "location_name": "Manyata Tech Park",
        "expiry_time": expiry.timestamp(),
        "status": "pending",
        "created_at": firestore.SERVER_TIMESTAMP
    }
    
    doc_ref = db.collection('donations').add(donation)
    print(f"Added Sample Donation: {donation['food_type']}")

    # 4. Add historical delivered donations for Metrics
    historical_donations = [
        {
            "food_type": "Rice & Curry",
            "quantity": 120,
            "status": "delivered",
            "created_at": datetime.now() - timedelta(days=1)
        },
        {
            "food_type": "Sandwiches",
            "quantity": 45,
            "status": "delivered",
            "created_at": datetime.now() - timedelta(hours=5)
        }
    ]
    for hd in historical_donations:
        db.collection('donations').add(hd)
    
    print("\nDatabase seeding complete! You can now start the backend.")

if __name__ == "__main__":
    seed_database()
