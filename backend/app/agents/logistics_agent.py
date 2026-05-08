import googlemaps
from app.core.firebase import db
import os

class LogisticsAgent:
    def __init__(self):
        self.gmaps = None
        api_key = os.getenv("GOOGLE_MAPS_API_KEY")
        if api_key:
            self.gmaps = googlemaps.Client(key=api_key)

    def get_optimized_route(self, origin, destination, waypoints=None):
        """
        Calculates the best route and ETA for a volunteer.
        """
        if not self.gmaps:
            # Simulation Mode for Demo
            return {
                "polyline": "simulated_polyline_data",
                "duration": "12 mins",
                "distance": "4.5 km",
                "status": "simulated"
            }
        
        directions_result = self.gmaps.directions(
            origin,
            destination,
            waypoints=waypoints,
            optimize_waypoints=True,
            mode="driving"
        )
        
        if directions_result:
            leg = directions_result[0]['legs'][0]
            return {
                "polyline": directions_result[0]['overview_polyline']['points'],
                "duration": leg['duration']['text'],
                "distance": leg['distance']['text'],
                "status": "real-time"
            }
        return None

    def update_volunteer_location(self, volunteer_id, lat, lng):
        """
        Updates volunteer position in Firestore to drive the moving map markers.
        """
        volunteer_ref = db.collection('volunteers').document(volunteer_id)
        volunteer_ref.update({
            "current_location": {"lat": lat, "lng": lng},
            "last_updated": firestore.SERVER_TIMESTAMP
        })
