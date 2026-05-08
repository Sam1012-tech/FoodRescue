import { TrendingUp, TrendingDown } from 'lucide-react';

const metrics = [
  { label: 'Meals Rescued Today', value: '1,247', change: '+12%', up: true, color: 'primary' },
  { label: 'Active Rescue Missions', value: '18', change: '+3', up: true, color: 'primary' },
  { label: 'Avg Response Time', value: '8.2min', change: '-15%', up: true, color: 'primary' },
  { label: 'CO₂ Saved', value: '423kg', change: '+8%', up: true, color: 'primary' },
  { label: 'Food Waste Prevented', value: '842kg', change: '+18%', up: true, color: 'primary' },
  { label: 'Volunteers Active', value: '34', change: '+6', up: true, color: 'primary' },
];

export function MetricsGrid() {
  return (
    <div className="grid grid-cols-3 gap-4 mb-6">
      {metrics.map((metric, index) => (
        <div
          key={index}
          className="bg-card border border-border rounded-xl p-6 hover:shadow-md hover:border-primary/30 transition-all"
        >
          <div className="flex items-start justify-between mb-3">
            <span className="text-muted-foreground text-sm">{metric.label}</span>
            <div className={`flex items-center gap-1 text-xs px-2 py-1 rounded-full ${metric.up ? 'bg-primary/10 text-primary' : 'bg-destructive/10 text-destructive'}`}>
              {metric.up ? <TrendingUp className="size-3" /> : <TrendingDown className="size-3" />}
              <span>{metric.change}</span>
            </div>
          </div>
          <div className="text-3xl text-foreground tracking-tight font-semibold">{metric.value}</div>
        </div>
      ))}
    </div>
  );
}
