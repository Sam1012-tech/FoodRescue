import React from 'react';
import { cn } from '@/app/imports/utils';

interface GlassCardProps {
  children: React.ReactNode;
  className?: string;
}

export function GlassCard({ children, className }: GlassCardProps) {
  return (
    <div className={cn(
      "bg-card/40 backdrop-blur-md border border-border/50 rounded-2xl p-6 shadow-sm hover:shadow-md transition-all duration-300",
      className
    )}>
      {children}
    </div>
  );
}
