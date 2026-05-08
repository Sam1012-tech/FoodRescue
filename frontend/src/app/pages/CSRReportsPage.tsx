import React from 'react';
import { motion } from 'motion/react';
import { CSRReporting } from '@/app/components/CSRReporting';
import { FileText, Download, Share2, ShieldCheck, TrendingUp, History } from 'lucide-react';

export function CSRReportsPage() {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-[1800px] mx-auto space-y-6"
    >
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold text-foreground">Corporate Impact & CSR</h2>
          <p className="text-muted-foreground">Certified sustainability reporting and ESG metrics</p>
        </div>
        <button className="bg-primary text-white px-6 py-2.5 rounded-xl font-bold shadow-md hover:bg-primary/90 transition-colors flex items-center gap-2">
          <FileText className="size-4" />
          Generate Impact Report
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <div className="lg:col-span-3 space-y-6">
          <CSRReporting />
          
          <div className="bg-card border border-border rounded-xl p-6">
             <h3 className="font-semibold mb-6 flex items-center justify-between">
                Recent Report History
                <History className="size-4 text-muted-foreground" />
             </h3>
             <div className="space-y-3">
                {[
                  { name: 'Q1_2026_Sustainability_Summary.pdf', date: 'April 02, 2026', size: '2.4 MB', type: 'Quarterly' },
                  { name: 'March_Impact_Certificate.pdf', date: 'April 01, 2026', size: '1.1 MB', type: 'Monthly' },
                  { name: 'Annual_Waste_Reduction_Audit_2025.pdf', date: 'Jan 15, 2026', size: '8.7 MB', type: 'Annual' },
                ].map((doc, i) => (
                  <div key={i} className="flex items-center gap-4 p-4 border border-border rounded-lg hover:bg-accent/50 transition-colors group cursor-pointer">
                     <div className="size-10 rounded-lg bg-muted flex items-center justify-center">
                        <FileText className="size-5 text-muted-foreground group-hover:text-primary transition-colors" />
                     </div>
                     <div className="flex-1">
                        <p className="text-sm font-bold text-foreground">{doc.name}</p>
                        <p className="text-[10px] text-muted-foreground">{doc.date} • {doc.size} • {doc.type}</p>
                     </div>
                     <div className="flex gap-2">
                        <button className="p-2 hover:bg-white rounded-lg transition-colors shadow-sm">
                           <Download className="size-4 text-muted-foreground" />
                        </button>
                        <button className="p-2 hover:bg-white rounded-lg transition-colors shadow-sm">
                           <Share2 className="size-4 text-muted-foreground" />
                        </button>
                     </div>
                  </div>
                ))}
             </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-[#1F3A33] text-white rounded-xl p-6 shadow-xl">
             <div className="flex items-center gap-2 mb-6">
                <ShieldCheck className="size-5 text-primary" />
                <h3 className="text-sm font-bold uppercase tracking-widest text-primary/80">Compliance Status</h3>
             </div>
             <div className="text-center py-4 border-b border-white/10 mb-6">
                <p className="text-4xl font-black mb-1">A+</p>
                <p className="text-[10px] text-white/50 uppercase">Global ESG Rating</p>
             </div>
             <div className="space-y-4">
                <div className="flex justify-between text-xs">
                   <span className="text-white/70">Waste Diversion</span>
                   <span className="font-bold">94%</span>
                </div>
                <div className="flex justify-between text-xs">
                   <span className="text-white/70">Carbon Offset</span>
                   <span className="font-bold">12,402kg</span>
                </div>
                <div className="flex justify-between text-xs">
                   <span className="text-white/70">Donation Accuracy</span>
                   <span className="font-bold">99.8%</span>
                </div>
             </div>
          </div>

          <div className="bg-card border border-border rounded-xl p-6">
             <div className="flex items-center gap-2 mb-4">
                <TrendingUp className="size-4 text-primary" />
                <h3 className="text-sm font-bold uppercase tracking-widest text-muted-foreground">Top Contributors</h3>
             </div>
             <div className="space-y-6">
                {[
                  { name: 'Google Bangalore', impact: '1.2 tons' },
                  { name: 'ITC Gardenia', impact: '840 kg' },
                  { name: 'Infosys Campus', impact: '762 kg' },
                ].map((donor, i) => (
                  <div key={i} className="flex justify-between items-center">
                     <div>
                        <p className="text-xs font-bold text-foreground">{donor.name}</p>
                        <p className="text-[10px] text-muted-foreground">Certified Donor</p>
                     </div>
                     <span className="text-xs font-black text-primary">{donor.impact}</span>
                  </div>
                ))}
             </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
