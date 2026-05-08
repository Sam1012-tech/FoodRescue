import React from 'react';
import { motion } from 'motion/react';
import { SmartMatching } from '@/app/components/SmartMatching';
import { FoodDetection } from '@/app/components/FoodDetection';
import { Brain, Cpu, Database, Activity, Terminal } from 'lucide-react';

export function AIIntelligencePage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex items-center gap-3 mb-2">
        <div className="bg-primary/10 p-2 rounded-lg">
          <Brain className="size-6 text-primary" />
        </div>
        <div>
          <h2 className="text-2xl font-bold text-foreground">AI Intelligence</h2>
          <p className="text-muted-foreground">Neural core and decision orchestration</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-card border border-border rounded-xl p-6 relative overflow-hidden">
            <div className="absolute top-0 right-0 p-4 opacity-5">
              <Cpu className="size-32" />
            </div>
            <h3 className="text-lg font-semibold mb-6 flex items-center gap-2">
              <Activity className="size-4 text-primary" />
              Agent Orchestration Flow
            </h3>
            <div className="h-80 bg-muted/20 rounded-lg flex items-center justify-center border border-dashed border-border relative">
              {/* Simulated Flow Diagram */}
              <div className="flex items-center gap-8">
                <div className="bg-background border border-border p-3 rounded-lg shadow-sm">Detection</div>
                <div className="w-12 h-[2px] bg-primary/30" />
                <div className="bg-primary/20 border border-primary/30 p-3 rounded-lg shadow-sm font-bold">Matching Engine</div>
                <div className="w-12 h-[2px] bg-primary/30" />
                <div className="bg-background border border-border p-3 rounded-lg shadow-sm">Optimization</div>
              </div>
            </div>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <SmartMatching />
            <FoodDetection />
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-[#1F3A33] text-white rounded-xl p-6 shadow-xl">
            <h3 className="text-sm font-bold uppercase tracking-wider mb-4 text-primary/80">AI Decision Logs</h3>
            <div className="space-y-4 font-mono text-[10px]">
              {[1, 2, 3, 4, 5, 6].map((i) => (
                <div key={i} className="border-l-2 border-primary/30 pl-3 py-1">
                  <p className="text-primary/60 mb-1">[{new Date().toLocaleTimeString()}] INF_ENGINE_0{i}</p>
                  <p className="text-white/90 line-clamp-2">Matching donor_42 with NGO_17. Confidence: 0.94. Rationale: Proximity + Capacity Match.</p>
                </div>
              ))}
            </div>
            <button className="w-full mt-6 py-2 bg-white/10 hover:bg-white/20 rounded-lg text-xs font-semibold transition-colors flex items-center justify-center gap-2">
              <Terminal className="size-3" />
              View Full Audit Log
            </button>
          </div>

          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="text-sm font-semibold mb-4 text-muted-foreground">Explainable AI Scores</h3>
            <div className="space-y-4">
              {['Freshness Accuracy', 'Route Optimization', 'Donor Reliability'].map((label) => (
                <div key={label} className="space-y-1">
                  <div className="flex justify-between text-xs">
                    <span>{label}</span>
                    <span className="font-bold">98.2%</span>
                  </div>
                  <div className="h-1.5 bg-muted rounded-full overflow-hidden">
                    <motion.div 
                      className="h-full bg-primary" 
                      initial={{ width: 0 }}
                      animate={{ width: '98.2%' }}
                      transition={{ duration: 1, delay: 0.5 }}
                    />
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
