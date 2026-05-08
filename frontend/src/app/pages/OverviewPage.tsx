import React from 'react';
import { MetricsGrid } from '@/app/components/MetricsGrid';
import { OperationsMap } from '@/app/components/OperationsMap';
import { ActivityFeed } from '@/app/components/ActivityFeed';
import { motion } from 'motion/react';

export function OverviewPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <MetricsGrid />
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <OperationsMap />
        </div>
        <div>
          <ActivityFeed />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-card border border-border rounded-xl p-6">
          <h3 className="text-foreground font-semibold mb-4">Rescue Response Trends</h3>
          <div className="h-64 bg-muted/20 rounded-lg flex items-center justify-center border border-dashed border-border">
            <p className="text-muted-foreground text-sm italic">Simulated Trend Chart</p>
          </div>
        </div>
        <div className="bg-card border border-border rounded-xl p-6">
          <h3 className="text-foreground font-semibold mb-4">Regional Impact</h3>
          <div className="h-64 bg-muted/20 rounded-lg flex items-center justify-center border border-dashed border-border">
            <p className="text-muted-foreground text-sm italic">Simulated Impact Heatmap</p>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
