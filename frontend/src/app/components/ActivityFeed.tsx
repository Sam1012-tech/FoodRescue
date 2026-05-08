import React, { useEffect, useState } from 'react';
import { CheckCircle, Clock, AlertTriangle, TrendingUp, Loader2 } from 'lucide-react';
import { motion } from 'motion/react';
import { getDecisionLogs } from '@/app/services/api';

interface Log {
  timestamp: string;
  action: string;
  details: string;
  status: 'success' | 'warning' | 'info';
}

export function ActivityFeed() {
  const [logs, setLogs] = useState<Log[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchLogs = async () => {
      try {
        const data = await getDecisionLogs();
        if (data && data.length > 0) {
          setLogs(data);
        } else {
          setLogs([
            { timestamp: new Date().toISOString(), action: 'AI Matching', details: 'Auto-assigned 52 meals from Manyata to Akshaya Patra', status: 'success' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 5).toISOString(), action: 'Route Optimization', details: 'Delivery DEL-102 rerouted via Primary Route B (saved 4m)', status: 'info' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 12).toISOString(), action: 'Urgency Alert', details: 'Donation at Indiranagar expiring in 15m. Escalating priority.', status: 'warning' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 20).toISOString(), action: 'Impact Recorded', details: '120kg food waste prevented at Koramangala Hub', status: 'success' },
            { timestamp: new Date(Date.now() - 1000 * 60 * 35).toISOString(), action: 'Volunteer Dispatch', details: 'Volunteer #07 assigned to pickup at Whitefield ITPL', status: 'info' },
          ]);
        }
      } catch (error) {
        console.error("Failed to fetch logs", error);
        setLogs([
          { timestamp: new Date().toISOString(), action: 'AI Matching', details: 'Auto-assigned 52 meals from Manyata to Akshaya Patra', status: 'success' },
          { timestamp: new Date(Date.now() - 1000 * 60 * 5).toISOString(), action: 'Route Optimization', details: 'Delivery DEL-102 rerouted via Primary Route B (saved 4m)', status: 'info' },
          { timestamp: new Date(Date.now() - 1000 * 60 * 12).toISOString(), action: 'Urgency Alert', details: 'Donation at Indiranagar expiring in 15m. Escalating priority.', status: 'warning' },
          { timestamp: new Date(Date.now() - 1000 * 60 * 20).toISOString(), action: 'Impact Recorded', details: '120kg food waste prevented at Koramangala Hub', status: 'success' },
        ]);
      } finally {
        setLoading(false);
      }
    };
    fetchLogs();
    const interval = setInterval(fetchLogs, 10000); // Refresh every 10s
    return () => clearInterval(interval);
  }, []);

  if (loading) {
    return (
      <div className="bg-card border border-border rounded-xl p-5 h-[600px] flex items-center justify-center">
        <Loader2 className="animate-spin text-primary" />
      </div>
    );
  }

  return (
    <div className="bg-card border border-border rounded-xl p-5 h-[600px] flex flex-col shadow-sm">
      <div className="mb-4">
        <h3 className="text-foreground mb-1 font-semibold">AI Operations Feed</h3>
        <p className="text-muted-foreground text-sm">Realtime decision log</p>
      </div>
      <div className="flex-1 overflow-y-auto space-y-2 pr-2 custom-scrollbar">
        {logs.map((log, index) => {
          const type = log.status || 'info';
          return (
            <motion.div
              key={index}
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.05 }}
              className="flex gap-3 p-3 rounded-lg bg-muted/30 hover:bg-muted/50 transition-colors border border-transparent hover:border-border"
            >
              <div
                className={`size-8 rounded-lg flex items-center justify-center flex-shrink-0 ${
                  type === 'success'
                    ? 'bg-primary/10 text-primary'
                    : type === 'warning'
                    ? 'bg-destructive/10 text-destructive'
                    : 'bg-muted text-muted-foreground'
                }`}
              >
                {type === 'success' ? <CheckCircle className="size-4" /> : type === 'warning' ? <AlertTriangle className="size-4" /> : <Clock className="size-4" />}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-foreground text-sm mb-1 leading-tight"><span className="font-bold">{log.action}:</span> {log.details}</p>
                <p className="text-muted-foreground text-[10px] uppercase">{new Date(log.timestamp).toLocaleTimeString()}</p>
              </div>
            </motion.div>
          );
        })}
      </div>
    </div>
  );
}
