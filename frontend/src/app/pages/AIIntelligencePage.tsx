import React, { useEffect, useState } from 'react';
import { motion } from 'motion/react';
import { SmartMatching } from '@/app/components/SmartMatching';
import { FoodDetection } from '@/app/components/FoodDetection';
import { GlassCard } from '@/app/components/GlassCard';
import { Brain, Cpu, Database, Activity, Terminal, Shield, Zap, Info } from 'lucide-react';
import { getDecisionLogs } from '@/app/services/api';

export function AIIntelligencePage() {
  const [logs, setLogs] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchLogs = async () => {
      try {
        const data = await getDecisionLogs();
        if (data && data.length > 0) {
          setLogs(data);
        } else {
          setLogs([
            { timestamp: new Date().toISOString(), action: 'Neural Scan', details: 'Scanning Manyata Tech Park for surplus anomalies...' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 2).toISOString(), action: 'Object Detection', details: 'CV identified 52 meals (Rice/Dal) with 96% confidence.' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 5).toISOString(), action: 'NGO Matching', details: 'Optimizing for Akshaya Patra (Reliability: 0.98, Distance: 2.4km).' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 8).toISOString(), action: 'Ethics Check', details: 'Verified compliance with food safety and equitable distribution protocols.' },
          ]);
        }
      } catch (error) {
        console.error("Failed to fetch logs", error);
        setLogs([
          { timestamp: new Date().toISOString(), action: 'Neural Scan', details: 'Scanning Manyata Tech Park for surplus anomalies...' },
          { timestamp: new Date(Date.now() - 1000 * 60 * 2).toISOString(), action: 'Object Detection', details: 'CV identified 52 meals (Rice/Dal) with 96% confidence.' },
          { timestamp: new Date(Date.now() - 1000 * 60 * 5).toISOString(), action: 'NGO Matching', details: 'Optimizing for Akshaya Patra (Reliability: 0.98, Distance: 2.4km).' },
        ]);
      } finally {
        setLoading(false);
      }
    };
    fetchLogs();
  }, []);

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex items-center justify-between mb-2">
        <div className="flex items-center gap-3">
          <div className="bg-primary/20 p-2 rounded-xl border border-primary/30">
            <Brain className="size-8 text-primary" />
          </div>
          <div>
            <h2 className="text-3xl font-bold text-foreground tracking-tight">Neural Core</h2>
            <p className="text-muted-foreground flex items-center gap-2">
              <Shield className="size-3" />
              Autonomous decision orchestration and ethical alignment
            </p>
          </div>
        </div>
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-accent/10 border border-accent/20 text-accent text-xs font-bold uppercase tracking-widest">
           Inference Mode: Optimized
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <GlassCard className="relative overflow-hidden group">
            <div className="absolute top-0 right-0 p-8 opacity-10 group-hover:opacity-20 transition-opacity">
              <Cpu className="size-48 text-primary" />
            </div>
            <h3 className="text-xl font-bold mb-8 flex items-center gap-3">
              <div className="p-1.5 bg-primary/20 rounded-lg">
                <Activity className="size-5 text-primary" />
              </div>
              Agent Orchestration Flow
            </h3>
            
            <div className="relative h-80 flex items-center justify-center">
               {/* Visual Flow Background */}
               <div className="absolute inset-0 opacity-10 bg-[radial-gradient(circle_at_center,var(--primary)_0%,transparent_70%)]" />
               
               <div className="flex items-center gap-6 relative z-10">
                 <div className="flex flex-col items-center gap-2">
                    <div className="bg-background border-2 border-border p-5 rounded-2xl shadow-xl hover:border-primary/50 transition-all cursor-default">
                      <Zap className="size-6 text-primary mb-2" />
                      <span className="font-bold text-sm">Detection</span>
                    </div>
                    <span className="text-[10px] uppercase font-bold text-muted-foreground">Inbound</span>
                 </div>
                 
                 <div className="w-16 h-[2px] bg-gradient-to-r from-border via-primary to-border relative">
                    <motion.div 
                      className="absolute top-1/2 left-0 size-1.5 bg-primary rounded-full -translate-y-1/2" 
                      animate={{ left: ['0%', '100%'] }}
                      transition={{ duration: 1.5, repeat: Infinity, ease: "linear" }}
                    />
                 </div>

                 <div className="bg-primary/10 border-2 border-primary/40 p-8 rounded-[2.5rem] shadow-2xl relative">
                    <div className="absolute -inset-1 bg-primary/20 blur-xl rounded-full" />
                    <div className="relative flex flex-col items-center">
                      <Brain className="size-10 text-primary mb-2 animate-pulse" />
                      <span className="font-black text-primary tracking-tighter">AI ENGINE</span>
                    </div>
                 </div>

                 <div className="w-16 h-[2px] bg-gradient-to-r from-border via-primary to-border relative">
                    <motion.div 
                      className="absolute top-1/2 left-0 size-1.5 bg-primary rounded-full -translate-y-1/2" 
                      animate={{ left: ['0%', '100%'] }}
                      transition={{ duration: 1.5, repeat: Infinity, ease: "linear", delay: 0.75 }}
                    />
                 </div>

                 <div className="flex flex-col items-center gap-2">
                    <div className="bg-background border-2 border-border p-5 rounded-2xl shadow-xl hover:border-primary/50 transition-all cursor-default">
                      <Truck className="size-6 text-primary mb-2" />
                      <span className="font-bold text-sm">Dispatch</span>
                    </div>
                    <span className="text-[10px] uppercase font-bold text-muted-foreground">Outbound</span>
                 </div>
               </div>
            </div>
          </GlassCard>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <SmartMatching />
            <FoodDetection />
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-[#0F1713] text-white rounded-[2rem] p-8 shadow-2xl border border-white/5 relative overflow-hidden">
            <div className="absolute top-0 right-0 p-4">
               <Terminal className="size-5 text-primary/40" />
            </div>
            <h3 className="text-xs font-black uppercase tracking-[0.2em] mb-6 text-primary flex items-center gap-2">
              <span className="size-1.5 rounded-full bg-primary animate-ping" />
              Live Inference Logs
            </h3>
            <div className="space-y-5 font-mono text-[11px] h-[400px] overflow-y-auto custom-scrollbar pr-2">
              {logs.length > 0 ? logs.map((log, i) => (
                <motion.div 
                  key={i} 
                  initial={{ opacity: 0, x: -10 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ delay: i * 0.05 }}
                  className="border-l border-primary/20 pl-4 py-0.5"
                >
                  <p className="text-primary/40 mb-1 flex items-center justify-between">
                    <span>[{new Date(log.timestamp).toLocaleTimeString()}]</span>
                    <span className="text-[9px] bg-primary/10 px-1 rounded">INF_0{i}</span>
                  </p>
                  <p className="text-white/80 leading-relaxed"><span className="text-accent/80 font-bold">{log.action}:</span> {log.details}</p>
                </motion.div>
              )) : (
                <div className="flex flex-col items-center justify-center h-full opacity-30 italic">
                   <p>Connecting to neural stream...</p>
                </div>
              )}
            </div>
            <button className="w-full mt-8 py-3 bg-primary/10 hover:bg-primary/20 border border-primary/20 rounded-xl text-xs font-bold transition-all flex items-center justify-center gap-3">
              <div className="size-1.5 rounded-full bg-primary" />
              Initialize Full Audit Trace
            </button>
          </div>

          <GlassCard>
            <h3 className="text-sm font-bold mb-6 text-muted-foreground uppercase tracking-widest flex items-center gap-2">
              <Info className="size-4" />
              Explainability Metrics
            </h3>
            <div className="space-y-6">
              {[
                { label: 'Decision Confidence', val: 98.2, color: 'bg-primary' },
                { label: 'Ethical Alignment', val: 100, color: 'bg-accent' },
                { label: 'Route Efficiency', val: 94.5, color: 'bg-primary' },
                { label: 'Food Safety Score', val: 99.8, color: 'bg-primary' }
              ].map((item) => (
                <div key={item.label} className="space-y-2">
                  <div className="flex justify-between text-xs font-medium">
                    <span className="text-muted-foreground">{item.label}</span>
                    <span className="text-foreground font-bold">{item.val}%</span>
                  </div>
                  <div className="h-2 bg-muted rounded-full overflow-hidden">
                    <motion.div 
                      className={`h-full ${item.color}`}
                      initial={{ width: 0 }}
                      animate={{ width: `${item.val}%` }}
                      transition={{ duration: 1.5, delay: 0.5, ease: "circOut" }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </GlassCard>
        </div>
      </div>
    </motion.div>
  );
}
