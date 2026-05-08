import React from 'react';
import { motion } from 'motion/react';
import { AlertCircle, Zap, ShieldAlert, Users, Phone, ArrowRight, Radio } from 'lucide-react';

export function EmergencyModePage() {
  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.98 }}
      animate={{ opacity: 1, scale: 1 }}
      className="p-6 max-w-[1200px] mx-auto"
    >
      <div className="bg-red-50 border border-red-200 rounded-2xl p-8 mb-8 relative overflow-hidden">
        <div className="absolute top-0 right-0 p-8 opacity-10">
          <AlertCircle className="size-48 text-red-600" />
        </div>
        <div className="relative z-10">
          <div className="flex items-center gap-3 text-red-600 font-bold mb-4">
            <Zap className="size-6 animate-pulse" />
            HIGH PRIORITY RESPONSE CENTER
          </div>
          <h2 className="text-4xl font-black text-red-900 mb-4">Emergency Mode Active</h2>
          <p className="text-red-800/80 max-w-2xl text-lg mb-8">
            Critical surplus detected with expiration thresholds under 45 minutes. System is now auto-escalating all nearby volunteer notifications and prioritising direct NGO logistics.
          </p>
          <div className="flex gap-4">
            <button className="bg-red-600 text-white px-8 py-3 rounded-xl font-bold shadow-lg hover:bg-red-700 transition-colors flex items-center gap-2">
              Broadcast Emergency Alert
              <Radio className="size-4" />
            </button>
            <button className="bg-white border border-red-200 text-red-900 px-8 py-3 rounded-xl font-bold hover:bg-red-50 transition-colors">
              Contact Command Center
            </button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="space-y-6">
          <h3 className="text-xl font-bold text-foreground flex items-center gap-2">
            <ShieldAlert className="size-5 text-red-600" />
            Critical Rescue Queue
          </h3>
          {[1, 2, 3].map((i) => (
            <div key={i} className="bg-card border-2 border-red-100 rounded-xl p-6 shadow-sm relative hover:border-red-300 transition-all group">
              <div className="absolute top-4 right-4">
                 <div className="bg-red-600 text-white text-[10px] font-black px-2 py-1 rounded">EXPIRES IN {12 - i * 3}M</div>
              </div>
              <div className="flex items-start gap-4 mb-4">
                <div className="size-12 rounded-xl bg-red-50 flex items-center justify-center text-red-600">
                  <AlertCircle className="size-6" />
                </div>
                <div>
                  <h4 className="font-bold text-foreground group-hover:text-red-600 transition-colors">Bulk Surplus: {80 + i * 20} Meals</h4>
                  <p className="text-sm text-muted-foreground">ITC Gardenia Banquet Hall • Vittal Mallya Rd</p>
                </div>
              </div>
              <div className="grid grid-cols-3 gap-4 mb-6">
                 <div className="bg-muted/30 p-3 rounded-lg text-center">
                    <p className="text-[10px] text-muted-foreground uppercase font-bold">Status</p>
                    <p className="text-xs font-bold text-red-600">UNASSIGNED</p>
                 </div>
                 <div className="bg-muted/30 p-3 rounded-lg text-center">
                    <p className="text-[10px] text-muted-foreground uppercase font-bold">Distance</p>
                    <p className="text-xs font-bold text-foreground">1.4km</p>
                 </div>
                 <div className="bg-muted/30 p-3 rounded-lg text-center">
                    <p className="text-[10px] text-muted-foreground uppercase font-bold">Priority</p>
                    <p className="text-xs font-bold text-foreground">Extreme</p>
                 </div>
              </div>
              <div className="flex gap-2">
                 <button className="flex-1 py-2 bg-red-600 text-white rounded-lg text-xs font-bold hover:bg-red-700">Auto-Assign Nearest</button>
                 <button className="flex-1 py-2 border border-border rounded-lg text-xs font-bold hover:bg-accent">Manual Override</button>
              </div>
            </div>
          ))}
        </div>

        <div className="space-y-8">
           <div>
              <h3 className="text-xl font-bold text-foreground mb-6">Nearby Resources</h3>
              <div className="bg-card border border-border rounded-xl p-6">
                 <div className="space-y-4">
                    <div className="flex items-center justify-between p-3 bg-green-50 border border-green-100 rounded-lg">
                       <div className="flex items-center gap-3">
                          <Users className="size-4 text-green-600" />
                          <span className="text-sm font-bold text-green-900">12 Volunteers Available</span>
                       </div>
                       <button className="text-[10px] font-bold text-green-700 hover:underline">Ping All</button>
                    </div>
                    <div className="flex items-center justify-between p-3 bg-blue-50 border border-blue-100 rounded-lg">
                       <div className="flex items-center gap-3">
                          <Phone className="size-4 text-blue-600" />
                          <span className="text-sm font-bold text-blue-900">Emergency NGO Hotline</span>
                       </div>
                       <button className="bg-blue-600 text-white size-6 rounded-full flex items-center justify-center">
                          <ArrowRight className="size-3" />
                       </button>
                    </div>
                 </div>
              </div>
           </div>

           <div>
              <h3 className="text-xl font-bold text-foreground mb-6">Escalation Flowchart</h3>
              <div className="bg-[#1F3A33] text-white rounded-xl p-8 shadow-xl">
                 <div className="space-y-4">
                    <div className="flex items-center gap-4">
                       <div className="size-3 rounded-full bg-red-500" />
                       <p className="text-xs font-bold">Level 1: Broadcast to Radius 5km</p>
                    </div>
                    <div className="h-4 w-[2px] bg-white/20 ml-1.5" />
                    <div className="flex items-center gap-4">
                       <div className="size-3 rounded-full bg-orange-500" />
                       <p className="text-xs font-bold text-white/70">Level 2: Activate High-Reliability NGOs</p>
                    </div>
                    <div className="h-4 w-[2px] bg-white/20 ml-1.5" />
                    <div className="flex items-center gap-4">
                       <div className="size-3 rounded-full bg-yellow-500" />
                       <p className="text-xs font-bold text-white/40">Level 3: Global Platform Escalation</p>
                    </div>
                 </div>
              </div>
           </div>
        </div>
      </div>
    </motion.div>
  );
}
