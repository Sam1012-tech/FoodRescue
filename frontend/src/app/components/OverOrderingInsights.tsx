// frontend/src/app/components/OverOrderingInsights.tsx
import React, { useState } from 'react';
import { motion, AnimatePresence } from 'motion/react';
import { AlertTriangle, TrendingDown, IndianRupee, Lightbulb, ChevronDown, ChevronUp, Leaf, X } from 'lucide-react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell, ReferenceLine
} from 'recharts';

// ─── Types ────────────────────────────────────────────────────────────────────

interface OverOrderEvent {
  event: string;
  dayOfWeek: string;
  avgPortions: number;
  avgWaste: number;
  frequency: number; // times per month
  costPerPortion: number; // INR
}

interface InsightCardProps {
  insight: OverOrderEvent;
  onDismiss: (event: string) => void;
}

// ─── Mock data (replace with Firestore aggregation) ──────────────────────────

const overOrderingEvents: OverOrderEvent[] = [
  {
    event: 'Wednesday All-Hands',
    dayOfWeek: 'Wednesday',
    avgPortions: 220,
    avgWaste: 52,
    frequency: 4,
    costPerPortion: 180,
  },
  {
    event: 'Friday Team Lunch',
    dayOfWeek: 'Friday',
    avgPortions: 90,
    avgWaste: 31,
    frequency: 4,
    costPerPortion: 150,
  },
  {
    event: 'Monthly Review (Last Friday)',
    dayOfWeek: 'Friday',
    avgPortions: 140,
    avgWaste: 44,
    frequency: 1,
    costPerPortion: 210,
  },
];

// Donation history by week (last 8 weeks)
const weeklyTrendData = [
  { week: 'W-8', portions: 168, waste: 48 },
  { week: 'W-7', portions: 192, waste: 57 },
  { week: 'W-6', portions: 180, waste: 53 },
  { week: 'W-5', portions: 155, waste: 43 },
  { week: 'W-4', portions: 205, waste: 62 },
  { week: 'W-3', portions: 190, waste: 58 },
  { week: 'W-2', portions: 178, waste: 52 },
  { week: 'W-1', portions: 165, waste: 47 },
];

// Per-day-of-week breakdown
const dayWasteData = [
  { day: 'Mon', waste: 12, optimal: 10 },
  { day: 'Tue', waste: 18, optimal: 10 },
  { day: 'Wed', waste: 52, optimal: 15 },
  { day: 'Thu', waste: 14, optimal: 10 },
  { day: 'Fri', waste: 38, optimal: 15 },
  { day: 'Sat', waste: 8, optimal: 8 },
  { day: 'Sun', waste: 6, optimal: 6 },
];

// ─── Helper ───────────────────────────────────────────────────────────────────

function computeInsight(ev: OverOrderEvent) {
  const annualWastePortions = ev.avgWaste * ev.frequency * 12;
  const annualWasteKg       = annualWastePortions * 0.35; // ~350g per portion
  const annualCostINR       = annualWastePortions * ev.costPerPortion;
  const suggestedCutPct     = Math.round((ev.avgWaste / ev.avgPortions) * 100 * 0.9); // 90% of waste %
  const annualSavingINR     = Math.round(annualCostINR * 0.9);
  return { annualWastePortions, annualWasteKg, annualCostINR, suggestedCutPct, annualSavingINR };
}

// ─── Insight card ─────────────────────────────────────────────────────────────

function InsightCard({ insight, onDismiss }: InsightCardProps) {
  const [expanded, setExpanded] = useState(false);
  const stats = computeInsight(insight);
  const wastePercent = Math.round((insight.avgWaste / insight.avgPortions) * 100);

  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: -10 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, scale: 0.97 }}
      className="relative rounded-xl border border-amber-500/30 bg-amber-500/5 overflow-hidden"
    >
      {/* Left accent bar */}
      <div className="absolute left-0 top-0 bottom-0 w-1 bg-amber-500 rounded-l-xl" />

      <div className="pl-5 pr-4 py-4">
        {/* Header row */}
        <div className="flex items-start justify-between gap-3">
          <div className="flex items-start gap-3">
            <div className="mt-0.5 p-1.5 rounded-lg bg-amber-500/15">
              <AlertTriangle className="size-4 text-amber-400" />
            </div>
            <div>
              <p className="text-sm font-bold text-foreground">{insight.event}</p>
              <p className="text-xs text-muted-foreground mt-0.5">
                Every {insight.dayOfWeek} · {insight.frequency}× / month ·{' '}
                <span className="text-amber-400 font-semibold">{wastePercent}% wasted on avg</span>
              </p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={() => setExpanded(e => !e)}
              className="text-muted-foreground hover:text-foreground transition-colors"
            >
              {expanded ? <ChevronUp className="size-4" /> : <ChevronDown className="size-4" />}
            </button>
            <button
              onClick={() => onDismiss(insight.event)}
              className="text-muted-foreground hover:text-foreground transition-colors"
            >
              <X className="size-4" />
            </button>
          </div>
        </div>

        {/* Headline recommendation */}
        <div className="mt-3 ml-10 p-3 rounded-lg bg-background/50 border border-border/50">
          <p className="text-sm text-foreground leading-relaxed">
            <span className="font-semibold text-amber-400">AI Insight: </span>
            Reduce catering order by <strong>{stats.suggestedCutPct}%</strong> (~{insight.avgWaste} fewer portions).
            Projected savings: <strong>₹{(stats.annualSavingINR / 100000).toFixed(1)}L/year</strong> and{' '}
            <strong>{Math.round(stats.annualWasteKg)} kg</strong> less food waste annually.
          </p>
        </div>

        {/* Expandable details */}
        <AnimatePresence>
          {expanded && (
            <motion.div
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: 'auto', opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              transition={{ duration: 0.25 }}
              className="overflow-hidden"
            >
              <div className="mt-4 ml-10 grid grid-cols-2 md:grid-cols-4 gap-3">
                {[
                  { label: 'Avg wasted', value: `${insight.avgWaste} portions`, icon: TrendingDown, color: 'text-red-400' },
                  { label: 'Annual waste', value: `${stats.annualWastePortions} portions`, icon: TrendingDown, color: 'text-orange-400' },
                  { label: 'Food wasted', value: `${stats.annualWasteKg.toFixed(0)} kg/yr`, icon: Leaf, color: 'text-emerald-400' },
                  { label: 'Cost wasted', value: `₹${(stats.annualCostINR / 100000).toFixed(1)}L/yr`, icon: IndianRupee, color: 'text-amber-400' },
                ].map((s, i) => (
                  <div key={i} className="rounded-lg bg-muted/30 p-3 border border-border/30">
                    <s.icon className={`size-3.5 mb-1.5 ${s.color}`} />
                    <p className={`text-base font-bold ${s.color}`}>{s.value}</p>
                    <p className="text-[10px] text-muted-foreground mt-0.5">{s.label}</p>
                  </div>
                ))}
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    </motion.div>
  );
}

// ─── Main component ───────────────────────────────────────────────────────────

export function OverOrderingInsights() {
  const [dismissed, setDismissed] = useState<string[]>([]);
  const visibleInsights = overOrderingEvents.filter(e => !dismissed.includes(e.event));

  const totalAnnualWaste = overOrderingEvents.reduce((sum, e) => {
    const s = computeInsight(e);
    return sum + s.annualCostINR;
  }, 0);

  const totalAnnualKg = overOrderingEvents.reduce((sum, e) => {
    const s = computeInsight(e);
    return sum + s.annualWasteKg;
  }, 0);

  return (
    <div className="space-y-6">
      {/* Section header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="p-2 rounded-xl bg-amber-500/10">
            <Lightbulb className="size-5 text-amber-400" />
          </div>
          <div>
            <h3 className="font-bold text-foreground">Over-Ordering Insights</h3>
            <p className="text-xs text-muted-foreground">AI detected {overOrderingEvents.length} recurring patterns from your donation history</p>
          </div>
        </div>
        <div className="text-right">
          <p className="text-xs text-muted-foreground">Total recoverable annual cost</p>
          <p className="text-lg font-black text-amber-400">₹{(totalAnnualWaste / 100000).toFixed(1)}L</p>
        </div>
      </div>

      {/* Insight cards */}
      <div className="space-y-3">
        <AnimatePresence>
          {visibleInsights.map(ev => (
            <InsightCard
              key={ev.event}
              insight={ev}
              onDismiss={name => setDismissed(d => [...d, name])}
            />
          ))}
        </AnimatePresence>
        {visibleInsights.length === 0 && (
          <div className="text-center py-8 text-muted-foreground text-sm">
            All insights dismissed. Refresh tomorrow for new patterns.
          </div>
        )}
      </div>

      {/* Charts side-by-side */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Weekly portions vs waste trend */}
        <div className="bg-card border border-border rounded-xl p-5">
          <h4 className="text-sm font-semibold mb-4 text-foreground">Weekly Donation Trend (last 8 weeks)</h4>
          <div className="h-48">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={weeklyTrendData} barGap={4}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(255,255,255,0.05)" />
                <XAxis dataKey="week" tick={{ fontSize: 10, fill: 'rgba(255,255,255,0.4)' }} axisLine={false} tickLine={false} />
                <YAxis hide />
                <Tooltip
                  contentStyle={{ backgroundColor: '#1A1D1B', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', fontSize: '11px' }}
                  formatter={(v, name) => [`${v} portions`, name === 'portions' ? 'Total donated' : 'Waste estimated']}
                />
                <Bar dataKey="portions" fill="#5E9B84" radius={[4, 4, 0, 0]} barSize={18} name="portions" />
                <Bar dataKey="waste"    fill="#D97D6F" radius={[4, 4, 0, 0]} barSize={18} name="waste" />
              </BarChart>
            </ResponsiveContainer>
          </div>
          <div className="flex gap-4 mt-2">
            <div className="flex items-center gap-1.5"><span className="size-2.5 rounded-full bg-[#5E9B84]" /><span className="text-[10px] text-muted-foreground">Donated</span></div>
            <div className="flex items-center gap-1.5"><span className="size-2.5 rounded-full bg-[#D97D6F]" /><span className="text-[10px] text-muted-foreground">Estimated waste</span></div>
          </div>
        </div>

        {/* Per-day-of-week waste vs optimal */}
        <div className="bg-card border border-border rounded-xl p-5">
          <h4 className="text-sm font-semibold mb-4 text-foreground">Waste by Day of Week (avg portions)</h4>
          <div className="h-48">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={dayWasteData}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(255,255,255,0.05)" />
                <XAxis dataKey="day" tick={{ fontSize: 10, fill: 'rgba(255,255,255,0.4)' }} axisLine={false} tickLine={false} />
                <YAxis hide />
                <ReferenceLine y={15} stroke="#5E9B84" strokeDasharray="4 4" label={{ value: 'Target', position: 'insideTopRight', fontSize: 9, fill: '#5E9B84' }} />
                <Tooltip
                  contentStyle={{ backgroundColor: '#1A1D1B', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', fontSize: '11px' }}
                />
                <Bar dataKey="waste" radius={[4, 4, 0, 0]} barSize={30}>
                  {dayWasteData.map((entry, i) => (
                    <Cell key={i} fill={entry.waste > 20 ? '#D97D6F' : '#5E9B84'} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
          <p className="text-[10px] text-muted-foreground mt-2">🔴 Red bars exceed sustainability target</p>
        </div>
      </div>
    </div>
  );
}
