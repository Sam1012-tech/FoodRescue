import { CheckCircle, Clock, AlertTriangle, TrendingUp } from 'lucide-react';
import { motion } from 'motion/react';

const activities = [
  { id: 1, type: 'success', message: '52 meals detected at Manyata Tech Park', time: '2 min ago', icon: CheckCircle },
  { id: 2, type: 'progress', message: 'Volunteer #12 assigned to pickup', time: '4 min ago', icon: Clock },
  { id: 3, type: 'success', message: 'NGO matched successfully - Akshaya Patra', time: '6 min ago', icon: CheckCircle },
  { id: 4, type: 'warning', message: 'Emergency escalation triggered', time: '8 min ago', icon: AlertTriangle },
  { id: 5, type: 'success', message: 'Pickup ETA optimized to 12 minutes', time: '11 min ago', icon: TrendingUp },
  { id: 6, type: 'success', message: '38 meals delivered to Feeding India', time: '15 min ago', icon: CheckCircle },
  { id: 7, type: 'progress', message: 'Route recalculated for efficiency', time: '18 min ago', icon: Clock },
  { id: 8, type: 'success', message: 'Quality verification completed', time: '22 min ago', icon: CheckCircle },
];

export function ActivityFeed() {
  return (
    <div className="bg-card border border-border rounded-xl p-5 h-[600px] flex flex-col shadow-sm">
      <div className="mb-4">
        <h3 className="text-foreground mb-1 font-semibold">AI Operations Feed</h3>
        <p className="text-muted-foreground text-sm">Realtime activity stream</p>
      </div>
      <div className="flex-1 overflow-y-auto space-y-2 pr-2">
        {activities.map((activity, index) => {
          const Icon = activity.icon;
          return (
            <motion.div
              key={activity.id}
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.1 }}
              className="flex gap-3 p-3 rounded-lg bg-muted/30 hover:bg-muted/50 transition-colors border border-transparent hover:border-border"
            >
              <div
                className={`size-8 rounded-lg flex items-center justify-center flex-shrink-0 ${
                  activity.type === 'success'
                    ? 'bg-primary/10 text-primary'
                    : activity.type === 'warning'
                    ? 'bg-destructive/10 text-destructive'
                    : 'bg-muted text-muted-foreground'
                }`}
              >
                <Icon className="size-4" />
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-foreground text-sm mb-1">{activity.message}</p>
                <p className="text-muted-foreground text-xs">{activity.time}</p>
              </div>
            </motion.div>
          );
        })}
      </div>
    </div>
  );
}
