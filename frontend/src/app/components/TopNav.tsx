import { Bell, Activity } from 'lucide-react';

export function TopNav() {
  return (
    <div className="h-16 border-b border-border bg-card flex items-center justify-between px-6 shadow-sm">
      <div className="flex items-center gap-6">
        <div className="flex items-center gap-3">
          <div className="size-8 rounded-lg bg-primary flex items-center justify-center">
            <span className="text-white text-lg">◈</span>
          </div>
          <span className="text-foreground font-semibold">RescueBite AI</span>
        </div>
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-primary/10 border border-primary/20">
          <div className="size-2 rounded-full bg-primary animate-pulse" />
          <span className="text-primary text-sm">System Active</span>
        </div>
        <span className="text-muted-foreground text-sm">Bengaluru Operations</span>
      </div>
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2 text-primary text-sm">
          <Activity className="size-4" />
          <span>Live Sync</span>
        </div>
        <button className="relative p-2 rounded-lg hover:bg-muted transition-colors">
          <Bell className="size-5 text-muted-foreground" />
          <div className="absolute top-1.5 right-1.5 size-2 rounded-full bg-primary" />
        </button>
        <div className="size-9 rounded-full bg-primary flex items-center justify-center">
          <span className="text-sm text-white font-medium">AD</span>
        </div>
      </div>
    </div>
  );
}
