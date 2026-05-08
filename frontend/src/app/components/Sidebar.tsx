import { LayoutDashboard, Radio, Brain, TrendingUp, Users, Truck, BarChart3, AlertCircle, FileText, Settings } from 'lucide-react';

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
    <div className="w-64 border-r border-sidebar-border bg-sidebar flex flex-col">
      <div className="flex-1 py-6 space-y-1 px-3">
        {navItems.map((item, index) => (
          <button
            key={index}
            onClick={() => onSelect(index)}
            className={`w-full flex items-center gap-3 px-4 py-2.5 rounded-lg transition-all duration-200 ${
              activeItem === index
                ? 'bg-sidebar-primary text-sidebar-primary-foreground shadow-sm'
                : 'text-sidebar-foreground/70 hover:bg-sidebar-accent hover:text-sidebar-foreground'
            }`}
          >
            <item.icon className="size-5" />
            <span className="text-sm font-medium">{item.label}</span>
          </button>
        ))}
      </div>
    </div>
  );
}
