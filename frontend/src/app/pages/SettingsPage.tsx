import React from 'react';
import { motion } from 'motion/react';
import { Settings, User, Bell, Shield, Brain, Cpu, Database, Save } from 'lucide-react';

export function SettingsPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1000px] mx-auto space-y-8"
    >
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="bg-primary/10 p-2 rounded-lg">
            <Settings className="size-6 text-primary" />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-foreground">Platform Settings</h2>
            <p className="text-muted-foreground">Manage your configurations and operational thresholds</p>
          </div>
        </div>
        <button className="bg-primary text-white px-6 py-2 rounded-lg text-sm font-bold shadow-md hover:bg-primary/90 transition-colors flex items-center gap-2">
          <Save className="size-4" />
          Save Changes
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="col-span-1 space-y-2">
           {[
             { label: 'Profile Settings', icon: User, active: true },
             { label: 'Notifications', icon: Bell },
             { label: 'Security & Privacy', icon: Shield },
             { label: 'AI Configuration', icon: Brain },
             { label: 'API Integrations', icon: Database },
           ].map((item, i) => (
             <button key={i} className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-semibold transition-all ${item.active ? 'bg-primary text-white shadow-md' : 'text-muted-foreground hover:bg-accent'}`}>
                <item.icon className="size-4" />
                {item.label}
             </button>
           ))}
        </div>

        <div className="md:col-span-2 space-y-8">
           <div className="bg-card border border-border rounded-2xl p-8 shadow-sm">
              <h3 className="text-lg font-bold mb-6">AI Decision Thresholds</h3>
              <div className="space-y-8">
                 {[
                   { label: 'Auto-Match Confidence', desc: 'Minimum score required for automatic NGO pairing', val: '85%' },
                   { label: 'Emergency Escalation', desc: 'Time threshold for expiring donations (minutes)', val: '45m' },
                   { label: 'Freshness Priority', desc: 'Weight given to food freshness in routing', val: '0.92' },
                 ].map((setting, i) => (
                   <div key={i} className="space-y-4">
                      <div className="flex justify-between items-start">
                         <div>
                            <p className="font-bold text-foreground">{setting.label}</p>
                            <p className="text-xs text-muted-foreground">{setting.desc}</p>
                         </div>
                         <span className="text-sm font-bold text-primary">{setting.val}</span>
                      </div>
                      <div className="h-1.5 bg-muted rounded-full relative">
                         <div className="absolute top-1/2 -translate-y-1/2 left-[80%] size-4 bg-white border-2 border-primary rounded-full shadow-md cursor-pointer hover:scale-110 transition-transform" />
                         <div className="h-full bg-primary rounded-full" style={{ width: '80%' }} />
                      </div>
                   </div>
                 ))}
              </div>
           </div>

           <div className="bg-card border border-border rounded-2xl p-8 shadow-sm">
              <h3 className="text-lg font-bold mb-6">Global Preferences</h3>
              <div className="space-y-6">
                 {[
                   { label: 'Realtime Map Sync', active: true },
                   { label: 'Anonymous NGO Reporting', active: false },
                   { label: 'Daily Impact Summaries', active: true },
                   { label: 'SMS Volunteer Alerts', active: true },
                 ].map((pref, i) => (
                   <div key={i} className="flex items-center justify-between">
                      <div>
                         <p className="text-sm font-bold text-foreground">{pref.label}</p>
                      </div>
                      <button className={`w-10 h-6 rounded-full relative transition-colors ${pref.active ? 'bg-primary' : 'bg-muted'}`}>
                         <div className={`absolute top-1 size-4 bg-white rounded-full transition-all ${pref.active ? 'left-5' : 'left-1'}`} />
                      </button>
                   </div>
                 ))}
              </div>
           </div>
        </div>
      </div>
    </motion.div>
  );
}
