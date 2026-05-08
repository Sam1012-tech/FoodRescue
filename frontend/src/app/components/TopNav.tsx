import { Bell, Search, Command, Zap } from 'lucide-react';

export function TopNav() {
  return (
    <div className="h-16 border-b border-border/50 bg-card/80 backdrop-blur-md flex items-center justify-between px-8 sticky top-0 z-50">
      <div className="flex items-center gap-10">
        <div className="flex items-center gap-3">
          <div className="size-9 rounded-xl bg-gradient-to-br from-primary to-accent flex items-center justify-center shadow-lg shadow-primary/20">
            <Zap className="size-5 text-white fill-white" />
          </div>
          <div className="flex flex-col">
            <span className="text-foreground font-black tracking-tight leading-none text-lg">RescueBite <span className="text-primary">AI</span></span>
            <span className="text-[10px] text-muted-foreground uppercase font-bold tracking-[0.1em]">Operations Suite</span>
          </div>
        </div>
        
        <div className="hidden md:flex items-center gap-2 px-4 py-2 rounded-xl bg-muted/30 border border-border/50 text-muted-foreground w-80 group hover:border-primary/30 transition-all cursor-text">
          <Search className="size-4 group-hover:text-primary transition-colors" />
          <span className="text-sm flex-1">Search missions, NGOs...</span>
          <div className="flex items-center gap-1 px-1.5 py-0.5 rounded bg-muted-foreground/10 border border-border/50">
            <Command className="size-2.5" />
            <span className="text-[9px] font-bold">K</span>
          </div>
        </div>
      </div>

      <div className="flex items-center gap-6">
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-primary/10 border border-primary/20">
          <div className="size-2 rounded-full bg-primary animate-pulse" />
          <span className="text-primary text-xs font-bold uppercase tracking-wider">Neural Core Active</span>
        </div>
        
        <div className="flex items-center gap-4 border-l border-border/50 pl-6">
          <button className="relative p-2 rounded-xl hover:bg-muted transition-all group">
            <Bell className="size-5 text-muted-foreground group-hover:text-foreground transition-colors" />
            <div className="absolute top-2 right-2 size-2 rounded-full bg-destructive border-2 border-card" />
          </button>
          
          <div className="flex items-center gap-3 pl-2">
            <div className="text-right hidden sm:block">
              <p className="text-sm font-bold text-foreground leading-none">Admin Controller</p>
              <p className="text-[10px] text-muted-foreground font-medium">Operations Lead</p>
            </div>
            <div className="size-10 rounded-2xl bg-gradient-to-tr from-primary/20 to-accent/20 border border-primary/30 flex items-center justify-center p-0.5">
               <div className="size-full rounded-[0.6rem] bg-card flex items-center justify-center overflow-hidden">
                 <img src="https://api.dicebear.com/7.x/avataaars/svg?seed=Felix" alt="avatar" className="size-full object-cover" />
               </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
