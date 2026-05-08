import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { MapPin, Truck, Home, Loader2 } from 'lucide-react';
import { renderToStaticMarkup } from 'react-dom/server';
import { getNGOs, getVolunteers, getDonations } from '@/app/services/api';

// Fix for default leaflet icons in React
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

// Custom Icon Creators using Lucide
const createCustomIcon = (IconComponent: any, color: string) => {
  return L.divIcon({
    html: renderToStaticMarkup(
      <div style={{ color }} className="bg-white p-1.5 rounded-full shadow-lg border-2 border-current">
        <IconComponent size={18} />
      </div>
    ),
    className: '',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
  });
};

const donorIcon = createCustomIcon(MapPin, '#D8A569');
const ngoIcon = createCustomIcon(Home, '#7CB69F');
const volunteerIcon = createCustomIcon(Truck, '#9B8BC5');

interface OperationsMapProps {
  isFullscreen?: boolean;
}

export function OperationsMap({ isFullscreen }: OperationsMapProps) {
  const [data, setData] = useState<{ngos: any[], volunteers: any[], donations: any[]}>({
    ngos: [],
    volunteers: [],
    donations: []
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMapData = async () => {
      try {
        const [ngos, volunteers, donations] = await Promise.all([
          getNGOs(),
          getVolunteers(),
          getDonations()
        ]);
        setData({ ngos, volunteers, donations });
      } catch (error) {
        console.error("Failed to fetch map data", error);
      } finally {
        setLoading(false);
      }
    };
    fetchMapData();
    const interval = setInterval(fetchMapData, 10000); // Update every 10s
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-full bg-muted/10">
        <Loader2 className="animate-spin text-primary mb-4" />
        <p className="text-sm text-muted-foreground font-medium uppercase tracking-widest">Initializing Geographic Stream...</p>
      </div>
    );
  }

  return (
    <div className={`relative w-full ${isFullscreen ? 'h-full' : 'h-[600px] rounded-xl overflow-hidden border border-border shadow-2xl'}`}>
      <MapContainer 
        center={[12.9716, 77.5946]} 
        zoom={12} 
        scrollWheelZoom={true}
        className="w-full h-full z-0"
        zoomControl={false}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
        />
        
        {/* NGOs */}
        {data.ngos.filter(n => n.location?.latitude && n.location?.longitude).map((ngo) => (
          <Marker 
            key={ngo.id} 
            position={[ngo.location.latitude, ngo.location.longitude]} 
            icon={ngoIcon}
          >
            <Popup className="custom-popup">
              <div className="p-1">
                <h3 className="font-bold text-sm mb-1">{ngo.name}</h3>
                <p className="text-[10px] text-muted-foreground uppercase">Capacity: {ngo.capacity} meals</p>
                <div className="mt-2 text-[10px] bg-green-50 text-green-700 px-2 py-0.5 rounded-full inline-block">NGO Partner</div>
              </div>
            </Popup>
          </Marker>
        ))}

        {/* Volunteers */}
        {data.volunteers.filter(v => v.current_location?.latitude && v.current_location?.longitude).map((v) => (
          <Marker 
            key={v.id} 
            position={[v.current_location.latitude, v.current_location.longitude]} 
            icon={volunteerIcon}
          >
            <Popup>
              <div className="p-1">
                <h3 className="font-bold text-sm mb-1">{v.name}</h3>
                <p className="text-[10px] text-muted-foreground uppercase">Status: {v.status}</p>
              </div>
            </Popup>
          </Marker>
        ))}

        {/* Donations */}
        {data.donations.filter(d => d.location?.latitude && d.location?.longitude).map((d) => (
          <Marker 
            key={d.id} 
            position={[d.location.latitude, d.location.longitude]} 
            icon={donorIcon}
          >
            <Popup>
              <div className="p-1">
                <h3 className="font-bold text-sm mb-1">{d.food_type}</h3>
                <p className="text-primary font-black text-sm">{d.quantity} servings</p>
                <p className="text-[10px] text-muted-foreground mt-1">{d.location_name}</p>
                <div className="mt-2 text-[10px] bg-orange-50 text-orange-700 px-2 py-0.5 rounded-full inline-block uppercase font-bold">
                  {d.status}
                </div>
              </div>
            </Popup>
          </Marker>
        ))}

      </MapContainer>

      {/* Map UI Overlay */}
      <div className="absolute top-6 left-6 z-[1000] flex flex-col gap-3">
        <div className="bg-background/90 backdrop-blur-md border border-border p-4 rounded-xl shadow-2xl min-w-[200px]">
          <h3 className="text-xs font-black uppercase tracking-[0.2em] text-muted-foreground mb-3">Live Network</h3>
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <div className="size-2 rounded-full bg-[#D8A569]" />
                <span className="text-xs font-medium">Donations</span>
              </div>
              <span className="text-xs font-bold">{data.donations.length}</span>
            </div>
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <div className="size-2 rounded-full bg-[#7CB69F]" />
                <span className="text-xs font-medium">NGO Partners</span>
              </div>
              <span className="text-xs font-bold">{data.ngos.length}</span>
            </div>
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <div className="size-2 rounded-full bg-[#9B8BC5]" />
                <span className="text-xs font-medium">Active Fleet</span>
              </div>
              <span className="text-xs font-bold">{data.volunteers.length}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
