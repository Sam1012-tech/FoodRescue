import React from 'react';
import { motion } from 'motion/react';
import { VolunteerLogistics } from '@/app/components/VolunteerLogistics';
import { Truck, MapPin, Navigation, Clock, CheckCircle, ChevronRight, MoreHorizontal } from 'lucide-react';

export function LogisticsPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex items-center justify-between mb-2">
        <div className="flex items-center gap-3">
          <div className="bg-primary/10 p-2 rounded-lg">
            <Truck className="size-6 text-primary" />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-foreground">Logistics Control</h2>
            <p className="text-muted-foreground">Fleet optimization and volunteer orchestration</p>
          </div>
        </div>
        <div className="flex items-center gap-4 bg-card border border-border rounded-lg px-4 py-2">
          <div className="flex flex-col">
            <span className="text-[10px] text-muted-foreground uppercase font-bold">Avg Response</span>
            <span className="text-sm font-bold text-foreground">8.2 min</span>
          </div>
          <div className="w-[1px] h-8 bg-border" />
          <div className="flex flex-col">
            <span className="text-[10px] text-muted-foreground uppercase font-bold">Route Efficiency</span>
            <span className="text-sm font-bold text-primary">+14.2%</span>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <VolunteerLogistics />
          
          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="font-semibold mb-4 flex items-center justify-between">
              Active Delivery Queue
              <button className="text-xs text-primary hover:underline">Optimize Routes</button>
            </h3>
            <div className="space-y-3">
              {[
                { id: 'DEL-102', from: 'Hotel Lalit', to: 'Shanti Niketan Shelter', eta: '4 min', status: 'In Transit' },
                { id: 'DEL-105', from: 'Infosys Mess', to: 'NGO Hope', eta: '12 min', status: 'In Transit' },
                { id: 'DEL-108', from: 'Indiranagar Cafe', to: 'Feeding India', eta: '18 min', status: 'Pending Pickup' },
              ].map((del, i) => (
                <div key={i} className="flex items-center gap-4 p-4 border border-border rounded-lg hover:border-primary/30 transition-colors">
                  <div className="size-10 rounded-full bg-muted flex items-center justify-center">
                    <Navigation className={`size-5 ${del.status === 'In Transit' ? 'text-primary' : 'text-muted-foreground'}`} />
                  </div>
                  <div className="flex-1 grid grid-cols-4 gap-4 items-center">
                    <div>
                      <p className="text-[10px] text-muted-foreground uppercase font-bold">{del.id}</p>
                      <p className="text-xs font-semibold text-foreground truncate">{del.from}</p>
                    </div>
                    <div>
                       <p className="text-[10px] text-muted-foreground uppercase font-bold">Destination</p>
                       <p className="text-xs font-semibold text-foreground truncate">{del.to}</p>
                    </div>
                    <div>
                       <p className="text-[10px] text-muted-foreground uppercase font-bold">ETA</p>
                       <div className="flex items-center gap-1">
                         <Clock className="size-3 text-primary" />
                         <p className="text-xs font-bold text-foreground">{del.eta}</p>
                       </div>
                    </div>
                    <div className="text-right">
                       <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${del.status === 'In Transit' ? 'bg-primary/10 text-primary' : 'bg-muted text-muted-foreground'}`}>
                         {del.status}
                       </span>
                    </div>
                  </div>
                  <button className="p-1 hover:bg-muted rounded">
                    <MoreHorizontal className="size-4 text-muted-foreground" />
                  </button>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-[#1F3A33] text-white rounded-xl p-6 shadow-xl relative overflow-hidden">
             <div className="absolute top-0 right-0 p-4 opacity-10">
               <Navigation className="size-24" />
             </div>
             <h3 className="text-sm font-bold uppercase tracking-wider mb-6 text-primary/80">Route Intelligence</h3>
             <div className="space-y-4">
                <div className="p-3 bg-white/5 rounded-lg border border-white/10">
                   <p className="text-xs text-white/90 mb-2">Recommendation:</p>
                   <p className="text-[10px] text-white/70 leading-relaxed italic">
                     "Heavy traffic on Outer Ring Road. Redirecting deliveries DEL-105 and DEL-108 to Secondary Route B via Hebbal."
                   </p>
                </div>
                <button className="w-full py-2 bg-primary text-white rounded-lg text-xs font-bold hover:bg-primary/90 transition-colors">
                  Apply Global Re-routing
                </button>
             </div>
          </div>

          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="text-sm font-semibold mb-4 text-muted-foreground uppercase tracking-widest">Volunteer Health</h3>
            <div className="space-y-6">
               {[
                 { name: 'Volunteer #12', active: '4h 12m', missions: 8, status: 'Healthy' },
                 { name: 'Volunteer #07', active: '6h 45m', missions: 14, status: 'Break Required' },
                 { name: 'Volunteer #23', active: '1h 30m', missions: 3, status: 'Healthy' },
               ].map((v, i) => (
                 <div key={i} className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                       <div className="size-8 rounded-full bg-muted" />
                       <div>
                          <p className="text-xs font-bold text-foreground">{v.name}</p>
                          <p className="text-[10px] text-muted-foreground">{v.active} active today</p>
                       </div>
                    </div>
                    <div className="text-right">
                       <p className="text-[10px] font-bold uppercase" style={{ color: v.status === 'Healthy' ? '#6BA58F' : '#D8A569' }}>{v.status}</p>
                       <p className="text-[10px] text-muted-foreground">{v.missions} missions</p>
                    </div>
                 </div>
               ))}
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
