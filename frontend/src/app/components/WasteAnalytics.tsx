import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { TrendingDown } from 'lucide-react';

const data = [
  { day: 'Mon', waste: 420, rescued: 380 },
  { day: 'Tue', waste: 380, rescued: 350 },
  { day: 'Wed', waste: 520, rescued: 480 },
  { day: 'Thu', waste: 350, rescued: 330 },
  { day: 'Fri', waste: 460, rescued: 440 },
  { day: 'Sat', waste: 290, rescued: 270 },
  { day: 'Sun', waste: 310, rescued: 290 },
];

export function WasteAnalytics() {
  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-foreground mb-1 font-semibold">Waste Analytics</h3>
          <p className="text-muted-foreground text-sm">Weekly trends and patterns</p>
        </div>
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-primary/10 text-primary text-sm">
          <TrendingDown className="size-4" />
          <span>12% reduction</span>
        </div>
      </div>
      <div className="h-64 mb-4">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data}>
            <defs>
              <linearGradient id="colorWaste" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#D97D6F" stopOpacity={0.3} />
                <stop offset="95%" stopColor="#D97D6F" stopOpacity={0} />
              </linearGradient>
              <linearGradient id="colorRescued" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#5E9B84" stopOpacity={0.3} />
                <stop offset="95%" stopColor="#5E9B84" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="rgba(31, 41, 51, 0.1)" />
            <XAxis dataKey="day" stroke="#5F6C7B" style={{ fontSize: '12px' }} />
            <YAxis stroke="#5F6C7B" style={{ fontSize: '12px' }} />
            <Tooltip
              contentStyle={{
                backgroundColor: '#FFFFFF',
                border: '1px solid rgba(31, 58, 51, 0.12)',
                borderRadius: '8px',
                color: '#1F2933',
              }}
            />
            <Area
              type="monotone"
              dataKey="waste"
              stroke="#D97D6F"
              fillOpacity={1}
              fill="url(#colorWaste)"
              strokeWidth={2}
            />
            <Area
              type="monotone"
              dataKey="rescued"
              stroke="#5E9B84"
              fillOpacity={1}
              fill="url(#colorRescued)"
              strokeWidth={2}
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
      <div className="p-4 rounded-lg bg-primary/5 border border-primary/20">
        <div className="flex items-start gap-3">
          <div className="size-8 rounded-lg bg-primary/10 flex items-center justify-center flex-shrink-0">
            <TrendingDown className="size-4 text-primary" />
          </div>
          <div>
            <p className="text-foreground text-sm mb-1 font-medium">AI Recommendation</p>
            <p className="text-muted-foreground text-sm">
              Reduce Wednesday preparation by 15% to minimize waste. Historical data shows consistent overproduction.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
