import React from 'react';
import { motion } from 'motion/react';
import { PredictiveSurplus } from '@/app/components/PredictiveSurplus';
import { TrendingUp, Calendar, MapPin, ArrowUpRight, BarChart3 } from 'lucide-react';

export function PredictionsPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="bg-primary/10 p-2 rounded-lg">
            <TrendingUp className="size-6 text-primary" />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-foreground">Future Surplus Forecasting</h2>
            <p className="text-muted-foreground">AI-driven predictive logistics and hotspot analysis</p>
          </div>
        </div>
        <div className="flex gap-2">
          <button className="bg-background border border-border px-4 py-2 rounded-lg text-sm font-medium hover:bg-accent flex items-center gap-2">
            <Calendar className="size-4" />
            Next 7 Days
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <div className="lg:col-span-3 space-y-6">
          <PredictiveSurplus />
          
          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="text-lg font-semibold mb-6 flex items-center gap-2">
              <BarChart3 className="size-4 text-primary" />
              Surplus Probability Heatmap
            </h3>
            <div className="h-[400px] bg-muted/20 rounded-lg flex items-center justify-center border border-dashed border-border overflow-hidden">
               {/* Simulated Heatmap */}
               <div className="grid grid-cols-12 grid-rows-8 gap-1 w-full h-full p-4">
                 {Array.from({ length: 96 }).map((_, i) => (
                   <div 
                    key={i} 
                    className="rounded-sm" 
                    style={{ 
                      backgroundColor: `rgba(94, 155, 132, ${Math.random()})`,
                      opacity: 0.3 + Math.random() * 0.7
                    }} 
                   />
                 ))}
               </div>
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="text-sm font-semibold mb-4 text-muted-foreground uppercase tracking-widest">Hotspot Alerts</h3>
            <div className="space-y-4">
              {[
                { loc: 'Manyata Tech Park', prob: '78%', time: '8:30 PM', qty: '60 meals' },
                { loc: 'Electronic City Phase 1', prob: '64%', time: '7:15 PM', qty: '120 meals' },
                { loc: 'Whitefield ITPL', prob: '52%', time: '9:00 PM', qty: '45 meals' },
              ].map((item, i) => (
                <div key={i} className="p-4 bg-muted/30 rounded-lg border border-border hover:border-primary/30 transition-colors">
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center gap-2 text-xs font-bold text-primary">
                      <MapPin className="size-3" />
                      {item.loc}
                    </div>
                    <ArrowUpRight className="size-3 text-muted-foreground" />
                  </div>
                  <div className="flex items-end justify-between">
                    <div>
                      <p className="text-xl font-bold text-foreground">{item.prob}</p>
                      <p className="text-[10px] text-muted-foreground uppercase">Probability</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium text-foreground">{item.qty}</p>
                      <p className="text-[10px] text-muted-foreground uppercase">{item.time}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-primary text-white rounded-xl p-6 shadow-lg">
            <h3 className="font-bold mb-2">Proactive Prep</h3>
            <p className="text-xs text-white/80 leading-relaxed mb-4">
              Based on predicted surplus, we recommend activating 12 additional volunteers in North Bengaluru for the evening shift.
            </p>
            <button className="w-full py-2 bg-white text-primary rounded-lg text-xs font-bold hover:bg-white/90 transition-colors">
              Deploy Notifications
            </button>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
