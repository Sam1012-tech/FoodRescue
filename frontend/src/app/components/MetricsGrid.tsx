import React, { useEffect, useState } from 'react';
import { TrendingUp, TrendingDown, Loader2 } from 'lucide-react';
import { getMetrics } from '@/app/services/api';

interface Metric {
  label: string;
  value: string | number;
  change: string;
  up: boolean;
  color: string;
}

export function MetricsGrid() {
  const [metricsData, setMetricsData] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMetrics = async () => {
      try {
        const data = await getMetrics();
        if (data && Object.keys(data).length > 0) {
          setMetricsData(data);
        } else {
          // Fallback to hardcoded if data is empty
          setMetricsData({
            meals_rescued: 42842,
            co2_saved_kg: 14230,
            active_deliveries: 18,
            ngo_partners: 24,
            volunteers_active: 134
          });
        }
      } catch (error) {
        console.error("Failed to fetch metrics", error);
        setMetricsData({
          meals_rescued: 42842,
          co2_saved_kg: 14230,
          active_deliveries: 18,
          ngo_partners: 24,
          volunteers_active: 134
        });
      } finally {
        setLoading(false);
      }
    };
    fetchMetrics();
    const interval = setInterval(fetchMetrics, 30000); // Refresh every 30s
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center p-12">
        <Loader2 className="animate-spin text-primary" />
      </div>
    );
  }

  const items: Metric[] = [
    { label: 'Meals Rescued', value: metricsData?.meals_rescued?.toLocaleString() || '0', change: '+12%', up: true, color: 'primary' },
    { label: 'CO₂ Saved (kg)', value: metricsData?.co2_saved_kg || '0', change: '+8%', up: true, color: 'primary' },
    { label: 'Active Missions', value: metricsData?.active_deliveries || '0', change: '+3', up: true, color: 'primary' },
    { label: 'NGO Partners', value: metricsData?.ngo_partners || '0', change: 'Stable', up: true, color: 'primary' },
    { label: 'Volunteers', value: metricsData?.volunteers_active || '0', change: '+6', up: true, color: 'primary' },
    { label: 'Avg Response', value: '8.2m', change: '-15%', up: true, color: 'primary' },
  ];

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 gap-4 mb-6">
      {items.map((metric, index) => (
        <div
          key={index}
          className="bg-card border border-border rounded-xl p-4 hover:shadow-md hover:border-primary/30 transition-all"
        >
          <div className="flex items-start justify-between mb-2">
            <span className="text-muted-foreground text-xs font-medium uppercase tracking-wider">{metric.label}</span>
            <div className={`flex items-center gap-1 text-[10px] px-1.5 py-0.5 rounded-full ${metric.up ? 'bg-primary/10 text-primary' : 'bg-destructive/10 text-destructive'}`}>
              {metric.up ? <TrendingUp className="size-3" /> : <TrendingDown className="size-3" />}
              <span>{metric.change}</span>
            </div>
          </div>
          <div className="text-2xl text-foreground tracking-tight font-bold">{metric.value}</div>
        </div>
      ))}
    </div>
  );
}
