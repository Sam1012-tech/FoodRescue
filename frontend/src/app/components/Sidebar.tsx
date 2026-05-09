import { LayoutDashboard, Radio, Brain, TrendingUp, Users, Truck, BarChart3, AlertCircle, FileText, Settings, ChevronRight } from 'lucide-react';
import { cn } from '@/app/imports/utils';
import { motion } from 'motion/react';


export const navItems = [
  { icon: LayoutDashboard, label: 'Overview' },
  { icon: Radio, label: 'Live Operations' },
  { icon: Brain, label: 'AI Intelligence' },
  { icon: TrendingUp, label: 'Predictions' },
  { icon: Users, label: 'NGO Network' },
  { icon: Truck, label: 'Logistics' },
  { icon: BarChart3, label: 'Analytics' },
  { icon: AlertCircle, label: 'Emergency Mode' },
  { icon: FileText, label: 'CSR Reports' },
  { icon: Settings, label: 'Settings' },
];

interface SidebarProps {
  activeItem: number;
  onSelect: (index: number) => void;
}

export function Sidebar({ activeItem, onSelect }: SidebarProps) {
  return (
    <div className="w-72 border-r border-sidebar-border bg-sidebar flex flex-col h-full">
      <div className="flex-1 py-8 space-y-2 px-4">
        <p className="px-4 text-[10px] font-black uppercase tracking-[0.2em] text-sidebar-foreground/40 mb-4">Operations</p>
        {navItems.map((item, index) => (
          <button
            key={index}
            onClick={() => onSelect(index)}
            className={cn(
              "w-full flex items-center justify-between px-4 py-3 rounded-xl transition-all duration-300 group relative overflow-hidden",
              activeItem === index
                ? 'bg-sidebar-primary/20 text-white shadow-[0_0_20px_rgba(107,165,143,0.1)] border border-sidebar-border/50'
                : 'text-sidebar-foreground/60 hover:bg-sidebar-accent/50 hover:text-sidebar-foreground'
            )}
          >
            <div className="flex items-center gap-3.5 relative z-10">
              <item.icon className={cn(
                "size-5 transition-transform duration-300 group-hover:scale-110",
                activeItem === index ? "text-primary" : "text-inherit"
              )} />
              <span className="text-sm font-bold tracking-tight">{item.label}</span>
            </div>
            {activeItem === index ? (
              <motion.div 
                layoutId="active-indicator"
                className="absolute left-0 top-0 bottom-0 w-1 bg-primary"
              />
            ) : (
              <ChevronRight className="size-3 opacity-0 -translate-x-2 group-hover:opacity-40 group-hover:translate-x-0 transition-all duration-300" />
            )}
          </button>
        ))}
      </div>
      
      <div className="p-6">
        <div className="bg-gradient-to-br from-sidebar-accent/50 to-sidebar-accent/10 rounded-2xl p-4 border border-sidebar-border/50 shadow-inner">
          <p className="text-xs font-bold text-white mb-2">Operational Hub</p>
          <div className="flex items-center justify-between mb-3">
             <div className="flex -space-x-2">
                {[1,2,3].map(i => (
                  <div key={i} className="size-6 rounded-full border-2 border-sidebar bg-muted overflow-hidden">
                    <img src={`https://api.dicebear.com/7.x/avataaars/svg?seed=${i}`} alt="user" />
                  </div>
                ))}
             </div>
             <span className="text-[10px] text-muted-foreground">+12 Online</span>
          </div>
          <button className="w-full py-2 bg-background/50 hover:bg-background border border-border rounded-lg text-[10px] font-black uppercase tracking-widest text-foreground transition-colors">
            Team Chat
          </button>
        </div>
      </div>
    </div>
  );
}
