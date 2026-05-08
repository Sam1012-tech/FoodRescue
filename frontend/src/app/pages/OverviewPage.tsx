import React from 'react';
import { motion } from 'motion/react';
import { MetricsGrid } from '@/app/components/MetricsGrid';
import { OperationsMap } from '@/app/components/OperationsMap';
import { ActivityFeed } from '@/app/components/ActivityFeed';
import { GlassCard } from '@/app/components/GlassCard';
import { BarChart3, Map as MapIcon, Zap } from 'lucide-react';
import { 
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  BarChart, Bar, Cell
} from 'recharts';

const trendData = [
  { name: '08:00', value: 400 },
  { name: '10:00', value: 300 },
  { name: '12:00', value: 900 },
  { name: '14:00', value: 500 },
  { name: '16:00', value: 700 },
  { name: '18:00', value: 1200 },
  { name: '20:00', value: 800 },
];

const regionData = [
  { name: 'North', value: 85, color: '#5E9B84' },
  { name: 'South', value: 72, color: '#D8A569' },
  { name: 'East', value: 45, color: '#9B8BC5' },
  { name: 'West', value: 68, color: '#5E9B84' },
  { name: 'Central', value: 94, color: '#5E9B84' },
];

export function OverviewPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex items-center justify-between mb-2">
        <div>
          <h1 className="text-3xl font-bold text-foreground tracking-tight">Operations Command Center</h1>
          <p className="text-muted-foreground">Real-time AI matching and redistribution monitoring</p>
        </div>
        <div className="flex gap-3">
           <div className="flex items-center gap-2 px-4 py-2 rounded-lg bg-primary/10 border border-primary/20 text-primary text-sm font-medium">
             <Zap className="size-4 fill-primary" />
             AI Engine: Online
           </div>
        </div>
      </div>

      <MetricsGrid />
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <GlassCard className="h-[600px] p-0 overflow-hidden flex flex-col">
            <div className="p-5 border-b border-border/50 flex items-center justify-between bg-muted/20">
              <div className="flex items-center gap-2 font-semibold">
                <MapIcon className="size-4 text-primary" />
                Live Network Topology
              </div>
              <div className="flex gap-2">
                <span className="size-2 rounded-full bg-primary animate-pulse" />
                <span className="text-[10px] uppercase font-bold tracking-widest text-muted-foreground">Active Tracking</span>
              </div>
            </div>
            <div className="flex-1 bg-muted/5">
              <OperationsMap />
            </div>
          </GlassCard>
        </div>
        <div>
          <ActivityFeed />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <GlassCard>
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-2">
              <BarChart3 className="size-4 text-primary" />
              <h3 className="text-foreground font-semibold">Rescue Response Trends</h3>
            </div>
            <span className="text-[10px] font-bold text-primary bg-primary/10 px-2 py-1 rounded">PROJECTION: +14%</span>
          </div>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={trendData}>
                <defs>
                  <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#5E9B84" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="#5E9B84" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(255,255,255,0.05)" />
                <XAxis 
                  dataKey="name" 
                  axisLine={false} 
                  tickLine={false} 
                  tick={{ fontSize: 10, fill: 'rgba(255,255,255,0.4)' }}
                />
                <YAxis hide />
                <Tooltip 
                  contentStyle={{ backgroundColor: '#1A1D1B', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', fontSize: '12px' }}
                />
                <Area 
                  type="monotone" 
                  dataKey="value" 
                  stroke="#5E9B84" 
                  strokeWidth={2}
                  fillOpacity={1} 
                  fill="url(#colorValue)" 
                />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </GlassCard>
        
        <GlassCard>
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-2">
              <MapIcon className="size-4 text-primary" />
              <h3 className="text-foreground font-semibold">Regional Impact Analysis</h3>
            </div>
            <span className="text-[10px] font-bold text-muted-foreground tracking-widest uppercase">Coverage: 98.2%</span>
          </div>
          <div className="h-64 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={regionData}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(255,255,255,0.05)" />
                <XAxis 
                  dataKey="name" 
                  axisLine={false} 
                  tickLine={false} 
                  tick={{ fontSize: 10, fill: 'rgba(255,255,255,0.4)' }}
                />
                <YAxis hide />
                <Tooltip 
                  cursor={{ fill: 'rgba(255,255,255,0.05)' }}
                  contentStyle={{ backgroundColor: '#1A1D1B', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', fontSize: '12px' }}
                />
                <Bar dataKey="value" radius={[4, 4, 0, 0]} barSize={40}>
                  {regionData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </GlassCard>
      </div>
    </motion.div>
  );
}
