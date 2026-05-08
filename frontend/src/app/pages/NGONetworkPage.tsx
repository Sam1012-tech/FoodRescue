import React from 'react';
import { motion } from 'motion/react';
import { Users, MapPin, Phone, ShieldCheck, PieChart, Search, Filter } from 'lucide-react';

export function NGONetworkPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold text-foreground">NGO Partner Network</h2>
          <p className="text-muted-foreground">Coordination and capacity management for 24 registered partners</p>
        </div>
        <div className="flex gap-3">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 size-4 text-muted-foreground" />
            <input 
              type="text" 
              placeholder="Search NGOs..." 
              className="bg-card border border-border rounded-lg pl-10 pr-4 py-2 text-sm focus:ring-1 focus:ring-primary outline-none w-64"
            />
          </div>
          <button className="bg-card border border-border p-2 rounded-lg hover:bg-accent">
            <Filter className="size-4" />
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {[
              { name: 'Akshaya Patra', status: 'Active', capacity: '85%', reliability: '0.98', distance: '2.4km' },
              { name: 'Feeding India', status: 'Active', capacity: '42%', reliability: '0.95', distance: '4.1km' },
              { name: 'Robin Hood Army', status: 'Pending Pickup', capacity: '12%', reliability: '0.99', distance: '1.2km' },
              { name: 'No Food Waste', status: 'Active', capacity: '60%', reliability: '0.92', distance: '6.8km' },
            ].map((ngo, i) => (
              <div key={i} className="bg-card border border-border rounded-xl p-5 hover:shadow-md transition-all cursor-pointer group">
                <div className="flex items-start justify-between mb-4">
                  <div className="size-12 rounded-lg bg-muted flex items-center justify-center text-xl font-bold text-muted-foreground group-hover:bg-primary/10 group-hover:text-primary transition-colors">
                    {ngo.name[0]}
                  </div>
                  <div className="text-right">
                    <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold uppercase ${ngo.status === 'Active' ? 'bg-green-100 text-green-700' : 'bg-orange-100 text-orange-700'}`}>
                      {ngo.status}
                    </span>
                    <div className="flex items-center gap-1 text-xs text-muted-foreground mt-1 justify-end">
                      <ShieldCheck className="size-3 text-primary" />
                      {ngo.reliability} score
                    </div>
                  </div>
                </div>
                <h3 className="font-bold text-foreground mb-1">{ngo.name}</h3>
                <div className="flex items-center gap-4 text-xs text-muted-foreground mb-4">
                  <span className="flex items-center gap-1"><MapPin className="size-3" /> {ngo.distance}</span>
                  <span className="flex items-center gap-1"><PieChart className="size-3" /> {ngo.capacity} Capacity</span>
                </div>
                <div className="flex gap-2">
                  <button className="flex-1 py-1.5 bg-primary text-white rounded-lg text-xs font-semibold hover:bg-primary/90 transition-colors">
                    Assign Rescue
                  </button>
                  <button className="px-3 py-1.5 border border-border rounded-lg hover:bg-accent transition-colors">
                    <Phone className="size-3" />
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="font-semibold mb-4">Network Activity Timeline</h3>
            <div className="space-y-6">
              {[1, 2, 3].map((i) => (
                <div key={i} className="flex gap-4">
                  <div className="relative">
                    <div className="size-8 rounded-full bg-muted border border-border flex items-center justify-center text-xs font-bold">
                      {i}
                    </div>
                    {i !== 3 && <div className="absolute top-8 left-1/2 -translate-x-1/2 w-[1px] h-10 bg-border" />}
                  </div>
                  <div>
                    <p className="text-sm font-medium text-foreground">Akshaya Patra accepted 52 meals from Manyata Tech Park</p>
                    <p className="text-xs text-muted-foreground">14 mins ago • Mission ID: RES-982</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="text-sm font-semibold mb-4 text-muted-foreground uppercase tracking-widest">Capacity Overview</h3>
            <div className="h-64 bg-muted/20 rounded-lg flex items-center justify-center border border-dashed border-border mb-6">
               <div className="text-center">
                 <p className="text-3xl font-bold text-foreground">68%</p>
                 <p className="text-xs text-muted-foreground">Total Network Load</p>
               </div>
            </div>
            <div className="space-y-4">
              {['East Zone', 'West Zone', 'North Zone', 'South Zone'].map((zone) => (
                <div key={zone} className="space-y-1">
                  <div className="flex justify-between text-xs">
                    <span>{zone}</span>
                    <span className="font-bold">{Math.floor(Math.random() * 100)}%</span>
                  </div>
                  <div className="h-1 bg-muted rounded-full overflow-hidden">
                    <div className="h-full bg-primary" style={{ width: `${Math.floor(Math.random() * 100)}%` }} />
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-[#1F3A33] text-white rounded-xl p-6">
            <h3 className="font-bold mb-4">Add New Partner</h3>
            <p className="text-xs text-white/70 mb-4">Onboard a new NGO to the RescueBite AI network with auto-verification.</p>
            <button className="w-full py-2 bg-white text-primary rounded-lg text-xs font-bold hover:bg-white/90">
              Start Onboarding
            </button>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
