import React, { useState } from 'react';
import { Navigation, MapPin, ZoomIn, ZoomOut, Layers } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

const donations = [
  { id: 1, name: 'Manyata Tech Park', meals: 52, lat: 35, lng: 25, urgency: 'high' },
  { id: 2, name: 'Koramangala Office', meals: 38, lat: 55, lng: 55, urgency: 'medium' },
  { id: 3, name: 'Whitefield Campus', meals: 67, lat: 70, lng: 35, urgency: 'high' },
  { id: 4, name: 'Indiranagar Cafe', meals: 24, lat: 45, lng: 70, urgency: 'low' },
];

const ngos = [
  { id: 1, name: 'Akshaya Patra', lat: 30, lng: 60 },
  { id: 2, name: 'Feeding India', lat: 65, lng: 50 },
  { id: 3, name: 'Robin Hood Army', lat: 50, lng: 30 },
];

const volunteers = [
  { id: 1, name: 'Volunteer #12', lat: 40, lng: 45, status: 'moving' },
  { id: 2, name: 'Volunteer #07', lat: 60, lng: 40, status: 'moving' },
  { id: 3, name: 'Volunteer #23', lat: 35, lng: 65, status: 'moving' },
];

interface OperationsMapProps {
  isFullscreen?: boolean;
}

export function OperationsMap({ isFullscreen }: OperationsMapProps) {
  const [hoveredId, setHoveredId] = useState<number | null>(null);

  return (
    <div className={`bg-card border border-border rounded-xl shadow-sm overflow-hidden flex flex-col ${isFullscreen ? 'h-full border-none rounded-none' : 'h-[600px] p-6'}`}>
      {!isFullscreen && (
        <div className="flex items-center justify-between mb-4">
          <div>
            <h3 className="text-foreground mb-1 font-semibold">Live Operations Map</h3>
            <p className="text-muted-foreground text-sm">Realtime tracking across Bengaluru</p>
          </div>
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2">
              <div className="size-3 rounded-full bg-[#D8A569]" />
              <span className="text-xs text-muted-foreground">Donations</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="size-3 rounded-full bg-[#7CB69F]" />
              <span className="text-xs text-muted-foreground">NGOs</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="size-3 rounded-full bg-[#9B8BC5]" />
              <span className="text-xs text-muted-foreground">Volunteers</span>
            </div>
          </div>
        </div>
      )}

      <div className="relative flex-1 bg-muted/20 rounded-lg overflow-hidden group">
        {/* Map Controls */}
        <div className="absolute bottom-6 right-6 z-10 flex flex-col gap-2">
          <button className="bg-white border border-border p-2 rounded-lg shadow-md hover:bg-accent transition-colors">
            <ZoomIn className="size-4" />
          </button>
          <button className="bg-white border border-border p-2 rounded-lg shadow-md hover:bg-accent transition-colors">
            <ZoomOut className="size-4" />
          </button>
          <button className="bg-white border border-border p-2 rounded-lg shadow-md hover:bg-accent transition-colors">
            <Layers className="size-4" />
          </button>
        </div>

        {/* Status Indicator (only in fullscreen) */}
        {isFullscreen && (
          <div className="absolute bottom-6 left-6 z-10">
            <div className="bg-[#1F3A33] text-white px-4 py-2 rounded-lg shadow-xl flex items-center gap-3">
              <div className="size-2 rounded-full bg-primary animate-pulse" />
              <span className="text-xs font-bold tracking-wider">MAP LIVE SYNC</span>
              <div className="w-[1px] h-4 bg-white/20 mx-1" />
              <span className="text-[10px] text-white/60">34 Volunteers Active</span>
            </div>
          </div>
        )}

        {/* Map-like background with streets */}
        <div
          className="absolute inset-0 transition-transform duration-700 ease-out scale-105 group-hover:scale-100"
          style={{
            backgroundImage: `
              linear-gradient(rgba(94, 155, 132, 0.08) 1px, transparent 1px),
              linear-gradient(90deg, rgba(94, 155, 132, 0.08) 1px, transparent 1px),
              linear-gradient(rgba(94, 155, 132, 0.04) 2px, transparent 2px),
              linear-gradient(90deg, rgba(94, 155, 132, 0.04) 2px, transparent 2px)
            `,
            backgroundSize: '20px 20px, 20px 20px, 100px 100px, 100px 100px',
            backgroundColor: '#F9FCF9'
          }}
        />

        {/* Map overlay with subtle roads */}
        <svg className="absolute inset-0 w-full h-full opacity-30">
          <path d="M 0 30% L 100% 30%" stroke="#5E9B84" strokeWidth="4" />
          <path d="M 0 60% L 100% 60%" stroke="#5E9B84" strokeWidth="3" />
          <path d="M 30% 0 L 30% 100%" stroke="#5E9B84" strokeWidth="3" />
          <path d="M 60% 0 L 60% 100%" stroke="#5E9B84" strokeWidth="4" />
          <path d="M 0 0 L 100% 100%" stroke="#5E9B84" strokeWidth="1" strokeDasharray="10,10" />
          <path d="M 100% 0 L 0 100%" stroke="#5E9B84" strokeWidth="1" strokeDasharray="10,10" />
        </svg>

        <div className="relative h-full">
          {donations.map((donation) => (
            <motion.div
              key={donation.id}
              className="absolute z-20"
              style={{ left: `${donation.lng}%`, top: `${donation.lat}%` }}
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              onMouseEnter={() => setHoveredId(donation.id)}
              onMouseLeave={() => setHoveredId(null)}
            >
              <div className="relative -translate-x-1/2 -translate-y-1/2 cursor-pointer">
                <motion.div
                  className={`size-6 rounded-full ${
                    donation.urgency === 'high' ? 'bg-[#D8A569]' : 'bg-[#D8A569]/60'
                  } shadow-lg border-2 border-white flex items-center justify-center`}
                  animate={{ 
                    scale: hoveredId === donation.id ? 1.2 : 1,
                    boxShadow: ['0 0 0 0 rgba(216, 165, 105, 0.4)', '0 0 0 16px rgba(216, 165, 105, 0)'] 
                  }}
                  transition={{ duration: 2, repeat: Infinity }}
                >
                   <MapPin className="size-3 text-white" />
                </motion.div>
                
                <AnimatePresence>
                  {(hoveredId === donation.id || isFullscreen) && (
                    <motion.div 
                      initial={{ opacity: 0, y: 10, scale: 0.9 }}
                      animate={{ opacity: 1, y: 0, scale: 1 }}
                      exit={{ opacity: 0, scale: 0.9 }}
                      className="absolute left-8 top-0 bg-white border border-border rounded-xl px-4 py-3 whitespace-nowrap shadow-xl z-30 min-w-[140px]"
                    >
                      <div className="flex items-center justify-between mb-1">
                        <p className="text-foreground text-xs font-bold">{donation.name}</p>
                        <div className={`size-2 rounded-full ${donation.urgency === 'high' ? 'bg-red-500' : 'bg-orange-500'}`} />
                      </div>
                      <p className="text-primary text-sm font-black">{donation.meals} meals</p>
                      <div className="mt-2 pt-2 border-t border-border flex justify-between items-center">
                        <span className="text-[10px] text-muted-foreground uppercase font-bold">Expires in 12m</span>
                        <button className="text-[10px] font-bold text-primary hover:underline">Assign</button>
                      </div>
                    </motion.div>
                  )}
                </AnimatePresence>
              </div>
            </motion.div>
          ))}

          {ngos.map((ngo) => (
            <motion.div
              key={ngo.id}
              className="absolute z-10"
              style={{ left: `${ngo.lng}%`, top: `${ngo.lat}%` }}
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
            >
              <div className="relative -translate-x-1/2 -translate-y-1/2">
                <div className="size-5 rounded-lg bg-[#7CB69F] shadow-lg border-2 border-white flex items-center justify-center">
                   <div className="size-1.5 bg-white rounded-full" />
                </div>
              </div>
            </motion.div>
          ))}

          {volunteers.map((volunteer) => (
            <motion.div
              key={volunteer.id}
              className="absolute z-20"
              style={{ left: `${volunteer.lng}%`, top: `${volunteer.lat}%` }}
              animate={{
                x: [0, Math.random() * 60 - 30, Math.random() * 60 - 30, 0],
                y: [0, Math.random() * 60 - 30, Math.random() * 60 - 30, 0],
                rotate: [0, 90, 180, 270, 360],
              }}
              transition={{ duration: 15 + Math.random() * 10, repeat: Infinity, ease: "linear" }}
            >
              <div className="relative -translate-x-1/2 -translate-y-1/2">
                <div className="size-5 rounded-full bg-[#9B8BC5] shadow-lg border-2 border-white flex items-center justify-center transform hover:scale-125 transition-transform cursor-pointer">
                  <Navigation className="size-2.5 text-white fill-white" />
                </div>
                {isFullscreen && (
                  <div className="absolute top-6 left-1/2 -translate-x-1/2 bg-[#1F3A33]/90 text-white text-[8px] px-1.5 py-0.5 rounded backdrop-blur-sm whitespace-nowrap">
                    {volunteer.name}
                  </div>
                )}
              </div>
            </motion.div>
          ))}

          <svg className="absolute inset-0 w-full h-full pointer-events-none z-0">
            <motion.path
              d="M 25% 35% Q 40% 50% 60% 40%"
              stroke="rgba(94, 155, 132, 0.4)"
              strokeWidth="2"
              fill="none"
              strokeDasharray="5,5"
              initial={{ pathLength: 0 }}
              animate={{ pathLength: 1 }}
              transition={{ duration: 2, delay: 1 }}
            />
            <motion.path
              d="M 55% 55% Q 65% 45% 65% 50%"
              stroke="rgba(94, 155, 132, 0.4)"
              strokeWidth="2"
              fill="none"
              strokeDasharray="5,5"
              initial={{ pathLength: 0 }}
              animate={{ pathLength: 1 }}
              transition={{ duration: 2, delay: 1.2 }}
            />
          </svg>
        </div>
      </div>
    </div>
  );
}
