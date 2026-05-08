import React from 'react';
import { OperationsMap } from '@/app/components/OperationsMap';
import { motion } from 'motion/react';
import { Search, Filter, Shield, MoreVertical } from 'lucide-react';

export function LiveOperationsPage() {
  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="h-[calc(100vh-64px)] flex overflow-hidden"
    >
      <div className="flex-1 relative">
        <div className="absolute top-6 left-6 z-10 flex gap-3">
          <div className="bg-background/80 backdrop-blur-md border border-border rounded-lg px-4 py-2 shadow-lg flex items-center gap-3">
            <Search className="size-4 text-muted-foreground" />
            <input 
              type="text" 
              placeholder="Search missions, volunteers..." 
              className="bg-transparent border-none outline-none text-sm w-64"
            />
          </div>
          <button className="bg-background/80 backdrop-blur-md border border-border rounded-lg px-4 py-2 shadow-lg flex items-center gap-2 hover:bg-accent transition-colors">
            <Filter className="size-4 text-muted-foreground" />
            <span className="text-sm font-medium">Filters</span>
          </button>
        </div>

        <div className="absolute top-6 right-6 z-10 flex flex-col gap-3">
          <div className="bg-background/80 backdrop-blur-md border border-border rounded-lg p-3 shadow-lg">
            <div className="flex flex-col gap-4">
              <div className="flex items-center gap-3 text-xs font-medium text-muted-foreground">
                <div className="size-2 rounded-full bg-green-500 animate-pulse" />
                SYSTEM LIVE
              </div>
              <div className="space-y-3">
                <div className="flex items-center justify-between gap-8">
                  <span className="text-xs text-muted-foreground">Active Missions</span>
                  <span className="text-xs font-bold">18</span>
                </div>
                <div className="flex items-center justify-between gap-8">
                  <span className="text-xs text-muted-foreground">Pending Pickups</span>
                  <span className="text-xs font-bold">12</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="h-full w-full">
          {/* We'll modify OperationsMap later to handle fullscreen better, but for now we'll use it as is */}
          <OperationsMap isFullscreen />
        </div>
      </div>

      <div className="w-80 border-l border-border bg-card overflow-y-auto">
        <div className="p-4 border-b border-border">
          <h3 className="font-semibold text-foreground">Mission Queue</h3>
          <p className="text-xs text-muted-foreground">Realtime priority sorting</p>
        </div>
        <div className="p-4 space-y-4">
          {[1, 2, 3, 4, 5].map((i) => (
            <div key={i} className="p-3 border border-border rounded-lg hover:border-primary/50 transition-colors cursor-pointer group">
              <div className="flex items-start justify-between mb-2">
                <div className={`px-2 py-0.5 rounded text-[10px] font-bold uppercase ${i === 1 ? 'bg-red-100 text-red-600' : 'bg-orange-100 text-orange-600'}`}>
                  {i === 1 ? 'Critical' : 'High Priority'}
                </div>
                <Shield className="size-3 text-muted-foreground group-hover:text-primary transition-colors" />
              </div>
              <p className="text-sm font-semibold mb-1">MTP-RESCUE-{2400 + i}</p>
              <p className="text-xs text-muted-foreground mb-2">Manyata Tech Park • 52 meals</p>
              <div className="flex items-center justify-between">
                <div className="flex -space-x-2">
                  <div className="size-6 rounded-full bg-muted border-2 border-card" />
                  <div className="size-6 rounded-full bg-muted border-2 border-card" />
                </div>
                <span className="text-[10px] text-muted-foreground font-medium">ETA: 12m</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </motion.div>
  );
}
