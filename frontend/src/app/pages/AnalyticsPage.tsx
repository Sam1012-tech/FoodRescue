import React from 'react';
import { motion } from 'motion/react';
import { WasteAnalytics } from '@/app/components/WasteAnalytics';
import { BarChart3, TrendingDown, Leaf, Users2, Download, Filter, Calendar } from 'lucide-react';

export function AnalyticsPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <div className="bg-primary/10 p-2 rounded-lg">
            <BarChart3 className="size-6 text-primary" />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-foreground">Operational Analytics</h2>
            <p className="text-muted-foreground">Deep insights into impact, efficiency, and growth</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <button className="bg-card border border-border px-4 py-2 rounded-lg text-sm font-medium hover:bg-accent flex items-center gap-2">
            <Calendar className="size-4" />
            Last 30 Days
          </button>
          <button className="bg-primary text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-primary/90 flex items-center gap-2">
            <Download className="size-4" />
            Export Data
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {[
          { label: 'Total Meals Rescued', value: '42,842', trend: '+12.5%', icon: Users2 },
          { label: 'CO2 Emissions Saved', value: '14,230kg', trend: '+8.2%', icon: Leaf },
          { label: 'Food Waste Prevented', value: '28.4 tons', trend: '+15.1%', icon: TrendingDown },
          { label: 'Volunteer Efficiency', value: '94.2%', trend: '+4.3%', icon: BarChart3 },
        ].map((stat, i) => (
          <div key={i} className="bg-card border border-border rounded-xl p-6 shadow-sm">
            <div className="flex items-center justify-between mb-4">
              <div className="p-2 bg-muted/50 rounded-lg">
                <stat.icon className="size-5 text-primary" />
              </div>
              <span className="text-xs font-bold text-green-600 bg-green-50 px-2 py-0.5 rounded-full">{stat.trend}</span>
            </div>
            <p className="text-sm font-medium text-muted-foreground mb-1">{stat.label}</p>
            <p className="text-2xl font-bold text-foreground">{stat.value}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <WasteAnalytics />

          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="font-semibold mb-6">Impact Growth (Year over Year)</h3>
            <div className="h-[300px] bg-muted/10 rounded-lg flex items-end justify-between px-8 pb-4 border border-dashed border-border">
              {Array.from({ length: 12 }).map((_, i) => (
                <div key={i} className="w-8 bg-primary/20 rounded-t-sm relative group cursor-pointer" style={{ height: `${20 + Math.random() * 70}%` }}>
                  <div className="absolute inset-0 bg-primary opacity-0 group-hover:opacity-40 transition-opacity" />
                  <div className="absolute -top-8 left-1/2 -translate-x-1/2 bg-foreground text-background text-[10px] px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap">
                    {Math.floor(Math.random() * 5000)} meals
                  </div>
                </div>
              ))}
            </div>
            <div className="flex justify-between px-8 mt-4">
              {['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'].map(m => (
                <span key={m} className="text-[10px] text-muted-foreground uppercase font-bold">{m}</span>
              ))}
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-card border border-border rounded-xl p-6">
            <h3 className="text-sm font-semibold mb-6 text-muted-foreground uppercase tracking-widest">Efficiency Metrics</h3>
            <div className="space-y-8">
              {[
                { label: 'Avg Pickup Time', val: '12m 42s', target: '15m 00s', percent: 84 },
                { label: 'NGO Acceptance Rate', val: '98.2%', target: '95.0%', percent: 98 },
                { label: 'Freshness Retention', val: '92.5%', target: '90.0%', percent: 92 },
              ].map((item, i) => (
                <div key={i} className="space-y-2">
                  <div className="flex justify-between items-end">
                    <div>
                      <p className="text-xs text-muted-foreground">{item.label}</p>
                      <p className="text-lg font-bold text-foreground">{item.val}</p>
                    </div>
                    <p className="text-[10px] text-muted-foreground">Target: {item.target}</p>
                  </div>
                  <div className="h-1.5 bg-muted rounded-full overflow-hidden">
                    <motion.div
                      className="h-full bg-primary"
                      initial={{ width: 0 }}
                      animate={{ width: `${item.percent}%` }}
                      transition={{ duration: 1, delay: i * 0.2 }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-[#1F3A33] text-white rounded-xl p-6">
            <h3 className="font-bold mb-4">Geographic Insights</h3>
            <p className="text-xs text-white/70 mb-6 leading-relaxed">
              East Bengaluru has shown a 24% increase in surplus donations this quarter. Recommend expanding NGO network in the Indiranagar-Whitefield corridor.
            </p>
            <div className="space-y-3">
              {['Indiranagar', 'Whitefield', 'Koramangala'].map(loc => (
                <div key={loc} className="flex justify-between items-center text-xs">
                  <span className="text-white/60">{loc}</span>
                  <span className="font-bold text-primary">High Growth</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
