// frontend/src/app/pages/AnalyticsPage.tsx
import React, { useState } from 'react';
import { motion } from 'motion/react';
import { WasteAnalytics } from '@/app/components/WasteAnalytics';
import { OverOrderingInsights } from '@/app/components/OverOrderingInsights';
import {
  BarChart3, TrendingDown, Leaf, Users2, Download, Calendar,
  Utensils, Award, Flame
} from 'lucide-react';
import {
  AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip,
  ResponsiveContainer, PieChart, Pie, Cell, Legend
} from 'recharts';

// ─── Mock data (replace with Firestore live queries) ─────────────────────────

const monthlyMeals = [
  { month: 'Jan', meals: 2840, co2: 7100 },
  { month: 'Feb', meals: 3120, co2: 7800 },
  { month: 'Mar', meals: 2900, co2: 7250 },
  { month: 'Apr', meals: 3580, co2: 8950 },
  { month: 'May', meals: 4200, co2: 10500 },
  { month: 'Jun', meals: 3960, co2: 9900 },
  { month: 'Jul', meals: 4340, co2: 10850 },
  { month: 'Aug', meals: 4120, co2: 10300 },
  { month: 'Sep', meals: 3880, co2: 9700 },
  { month: 'Oct', meals: 4560, co2: 11400 },
  { month: 'Nov', meals: 4780, co2: 11950 },
  { month: 'Dec', meals: 5100, co2: 12750 },
];

const pieData = [
  { name: 'Lunch events', value: 42, color: '#5E9B84' },
  { name: 'All-hands',    value: 28, color: '#D8A569' },
  { name: 'Team dinners', value: 18, color: '#9B8BC5' },
  { name: 'Other',        value: 12, color: '#6B8F71' },
];

const KPI_STATS = [
  {
    label: 'Meals rescued this month',
    value: '4,780',
    trend: '+12.5%',
    sub: 'vs last month',
    icon: Utensils,
    accent: 'text-emerald-400',
    bg: 'bg-emerald-500/10',
  },
  {
    label: 'CO₂ saved this year',
    value: '107.8 t',
    trend: '+8.2%',
    sub: 'vs same period last year',
    icon: Leaf,
    accent: 'text-green-400',
    bg: 'bg-green-500/10',
  },
  {
    label: 'Beneficiaries reached',
    value: '14,240',
    trend: '+15.1%',
    sub: 'cumulative this year',
    icon: Users2,
    accent: 'text-blue-400',
    bg: 'bg-blue-500/10',
  },
  {
    label: 'Waste diversion rate',
    value: '94.2%',
    trend: '+4.3%',
    sub: 'target: 90%',
    icon: TrendingDown,
    accent: 'text-amber-400',
    bg: 'bg-amber-500/10',
  },
];

// Period tab options
const PERIODS = ['This month', 'This quarter', 'This year', 'All time'] as const;
type Period = typeof PERIODS[number];

// ─────────────────────────────────────────────────────────────────────────────

export function AnalyticsPage() {
  const [activePeriod, setActivePeriod] = useState<Period>('This year');

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-8"
    >
      {/* ── Page header ─────────────────────────────────────────────────────── */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <div className="bg-primary/10 p-2.5 rounded-xl">
            <BarChart3 className="size-6 text-primary" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-foreground tracking-tight">Company Impact Analytics</h1>
            <p className="text-muted-foreground text-sm">Google Bangalore — Donation history and sustainability metrics</p>
          </div>
        </div>

        {/* Period tabs */}
        <div className="flex items-center gap-1 bg-muted/30 p-1 rounded-xl border border-border/50">
          {PERIODS.map(p => (
            <button
              key={p}
              onClick={() => setActivePeriod(p)}
              className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-all duration-200 ${
                activePeriod === p
                  ? 'bg-primary text-white shadow-sm'
                  : 'text-muted-foreground hover:text-foreground'
              }`}
            >
              {p}
            </button>
          ))}
        </div>
      </div>

      {/* ── KPI stat row ─────────────────────────────────────────────────────── */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {KPI_STATS.map((stat, i) => (
          <motion.div
            key={i}
            initial={{ opacity: 0, y: 16 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.07 }}
            className="bg-card border border-border rounded-xl p-5 shadow-sm hover:shadow-md transition-shadow"
          >
            <div className="flex items-center justify-between mb-3">
              <div className={`p-2 rounded-lg ${stat.bg}`}>
                <stat.icon className={`size-4 ${stat.accent}`} />
              </div>
              <span className="text-[10px] font-bold text-emerald-500 bg-emerald-50 dark:bg-emerald-500/10 px-2 py-0.5 rounded-full">
                {stat.trend}
              </span>
            </div>
            <p className="text-xs text-muted-foreground mb-1">{stat.label}</p>
            <p className="text-2xl font-black text-foreground">{stat.value}</p>
            <p className="text-[10px] text-muted-foreground mt-1">{stat.sub}</p>
          </motion.div>
        ))}
      </div>

      {/* ── Main charts row ───────────────────────────────────────────────────── */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Monthly trend — 2/3 width */}
        <div className="lg:col-span-2 bg-card border border-border rounded-xl p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="font-semibold text-foreground">Meals Rescued & CO₂ Saved</h3>
              <p className="text-xs text-muted-foreground">Monthly breakdown — {activePeriod}</p>
            </div>
            <button className="flex items-center gap-2 text-xs font-medium text-muted-foreground hover:text-foreground border border-border px-3 py-1.5 rounded-lg">
              <Download className="size-3" /> Export
            </button>
          </div>
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={monthlyMeals}>
                <defs>
                  <linearGradient id="gMeals" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%"  stopColor="#5E9B84" stopOpacity={0.35} />
                    <stop offset="95%" stopColor="#5E9B84" stopOpacity={0} />
                  </linearGradient>
                  <linearGradient id="gCO2" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%"  stopColor="#D8A569" stopOpacity={0.25} />
                    <stop offset="95%" stopColor="#D8A569" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(255,255,255,0.05)" />
                <XAxis dataKey="month" tick={{ fontSize: 10, fill: 'rgba(255,255,255,0.4)' }} axisLine={false} tickLine={false} />
                <YAxis hide />
                <Tooltip
                  contentStyle={{ backgroundColor: '#1A1D1B', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', fontSize: '11px' }}
                  formatter={(v: any, n: any) => [n === 'meals' ? `${v} meals` : `${v} kg CO₂`, n === 'meals' ? 'Meals rescued' : 'CO₂ saved']}
                />
                <Area type="monotone" dataKey="meals" stroke="#5E9B84" strokeWidth={2} fillOpacity={1} fill="url(#gMeals)" />
                <Area type="monotone" dataKey="co2"   stroke="#D8A569" strokeWidth={2} fillOpacity={1} fill="url(#gCO2)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
          <div className="flex gap-6 mt-3">
            <div className="flex items-center gap-1.5"><span className="size-2.5 rounded-full bg-[#5E9B84]" /><span className="text-[10px] text-muted-foreground">Meals rescued</span></div>
            <div className="flex items-center gap-1.5"><span className="size-2.5 rounded-full bg-[#D8A569]" /><span className="text-[10px] text-muted-foreground">CO₂ saved (kg)</span></div>
          </div>
        </div>

        {/* Donation type pie — 1/3 width */}
        <div className="bg-card border border-border rounded-xl p-6">
          <h3 className="font-semibold text-foreground mb-1">Donations by Event Type</h3>
          <p className="text-xs text-muted-foreground mb-4">Where your food waste comes from</p>
          <div className="h-48">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={pieData} cx="50%" cy="50%" innerRadius={50} outerRadius={78} paddingAngle={3} dataKey="value">
                  {pieData.map((entry, i) => <Cell key={i} fill={entry.color} />)}
                </Pie>
                <Tooltip
                  contentStyle={{ backgroundColor: '#1A1D1B', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', fontSize: '11px' }}
                  formatter={(v: any) => [`${v}%`, '']}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>
          <div className="space-y-2 mt-2">
            {pieData.map((d, i) => (
              <div key={i} className="flex items-center justify-between text-xs">
                <div className="flex items-center gap-2">
                  <span className="size-2 rounded-full" style={{ backgroundColor: d.color }} />
                  <span className="text-muted-foreground">{d.name}</span>
                </div>
                <span className="font-bold text-foreground">{d.value}%</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ── Waste analytics (existing component) ─────────────────────────────── */}
      <WasteAnalytics />

      {/* ── Over-ordering insights — KILLER FEATURE ──────────────────────────── */}
      <div className="bg-card border border-border rounded-xl p-6">
        <OverOrderingInsights />
      </div>

      {/* ── Efficiency metrics ───────────────────────────────────────────────── */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {[
          { label: 'Avg Pickup Time', val: '12m 42s', target: '15m', pct: 84, icon: Flame, color: 'bg-emerald-500' },
          { label: 'NGO Acceptance Rate', val: '98.2%', target: '95%', pct: 98, icon: Award, color: 'bg-primary' },
          { label: 'Freshness Retention', val: '92.5%', target: '90%', pct: 93, icon: Leaf, color: 'bg-green-500' },
        ].map((m, i) => (
          <div key={i} className="bg-card border border-border rounded-xl p-5">
            <div className="flex items-center gap-2 mb-3">
              <m.icon className="size-4 text-primary" />
              <p className="text-xs text-muted-foreground font-medium">{m.label}</p>
            </div>
            <div className="flex items-end justify-between mb-3">
              <p className="text-2xl font-black text-foreground">{m.val}</p>
              <p className="text-[10px] text-muted-foreground pb-1">Target: {m.target}</p>
            </div>
            <div className="h-1.5 bg-muted rounded-full overflow-hidden">
              <motion.div
                className={`h-full rounded-full ${m.color}`}
                initial={{ width: 0 }}
                animate={{ width: `${m.pct}%` }}
                transition={{ duration: 1, delay: i * 0.15 }}
              />
            </div>
          </div>
        ))}
      </div>
    </motion.div>
  );
}
