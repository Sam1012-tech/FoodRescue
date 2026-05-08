import { FileText, Download, BarChart2, Leaf } from 'lucide-react';

export function CSRReporting() {
  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <div className="size-10 rounded-lg bg-primary/10 flex items-center justify-center">
            <FileText className="size-5 text-primary" />
          </div>
          <div>
            <h3 className="text-foreground mb-0.5 font-semibold">CSR Impact Reporting</h3>
            <p className="text-muted-foreground text-sm">Enterprise sustainability metrics</p>
          </div>
        </div>
        <button className="px-4 py-2 rounded-lg bg-primary text-primary-foreground hover:bg-primary/90 transition-all shadow-sm hover:shadow flex items-center gap-2">
          <Download className="size-4" />
          <span>Generate Report</span>
        </button>
      </div>
      <div className="grid grid-cols-2 gap-4 mb-6">
        <div className="p-4 rounded-lg bg-muted/30 border border-border">
          <div className="flex items-center gap-2 mb-2">
            <BarChart2 className="size-4 text-primary" />
            <span className="text-muted-foreground text-sm">Total Impact</span>
          </div>
          <p className="text-2xl text-foreground font-semibold">32,847</p>
          <p className="text-xs text-muted-foreground">Meals rescued this month</p>
        </div>
        <div className="p-4 rounded-lg bg-muted/30 border border-border">
          <div className="flex items-center gap-2 mb-2">
            <Leaf className="size-4 text-primary" />
            <span className="text-muted-foreground text-sm">CO₂ Reduction</span>
          </div>
          <p className="text-2xl text-foreground font-semibold">14.2 tons</p>
          <p className="text-xs text-muted-foreground">Carbon footprint saved</p>
        </div>
      </div>
      <div className="space-y-3">
        <button className="w-full p-3 rounded-lg border border-border hover:border-primary/30 hover:bg-muted/30 transition-all text-left">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-foreground text-sm mb-1 font-medium">Q1 2026 Impact Report</p>
              <p className="text-muted-foreground text-xs">January - March 2026</p>
            </div>
            <Download className="size-4 text-muted-foreground" />
          </div>
        </button>
        <button className="w-full p-3 rounded-lg border border-border hover:border-primary/30 hover:bg-muted/30 transition-all text-left">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-foreground text-sm mb-1 font-medium">Annual Sustainability Report 2025</p>
              <p className="text-muted-foreground text-xs">Full year review</p>
            </div>
            <Download className="size-4 text-muted-foreground" />
          </div>
        </button>
      </div>
    </div>
  );
}
