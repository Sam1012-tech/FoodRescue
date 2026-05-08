import { Camera, CheckCircle } from 'lucide-react';

export function FoodDetection() {
  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <div className="flex items-center gap-3 mb-6">
        <div className="size-10 rounded-lg bg-primary/10 flex items-center justify-center">
          <Camera className="size-5 text-primary" />
        </div>
        <div>
          <h3 className="text-foreground mb-0.5 font-semibold">Food Detection AI</h3>
          <p className="text-muted-foreground text-sm">Computer vision analysis</p>
        </div>
      </div>
      <div className="aspect-video bg-muted/30 rounded-lg mb-4 relative overflow-hidden border border-border">
        <div className="absolute inset-0 flex items-center justify-center">
          <Camera className="size-12 text-muted-foreground/30" />
        </div>
        <div className="absolute top-3 left-3 px-2 py-1 rounded bg-primary/10 backdrop-blur-sm border border-primary/20">
          <span className="text-primary text-xs font-medium">Analyzing...</span>
        </div>
      </div>
      <div className="space-y-2">
        <div className="flex items-center justify-between p-3 rounded-lg bg-muted/30 border border-transparent hover:border-border transition-all">
          <span className="text-foreground text-sm">Food Classification</span>
          <div className="flex items-center gap-2">
            <span className="text-muted-foreground text-sm">Rice & Curry</span>
            <CheckCircle className="size-4 text-primary" />
          </div>
        </div>
        <div className="flex items-center justify-between p-3 rounded-lg bg-muted/30 border border-transparent hover:border-border transition-all">
          <span className="text-foreground text-sm">Quantity Estimation</span>
          <div className="flex items-center gap-2">
            <span className="text-muted-foreground text-sm">45-52 meals</span>
            <div className="h-1.5 w-24 bg-muted rounded-full overflow-hidden">
              <div className="h-full w-[92%] bg-primary" />
            </div>
          </div>
        </div>
        <div className="flex items-center justify-between p-3 rounded-lg bg-muted/30 border border-transparent hover:border-border transition-all">
          <span className="text-foreground text-sm">Freshness Confidence</span>
          <div className="flex items-center gap-2">
            <span className="text-primary text-sm font-medium">96%</span>
            <div className="h-1.5 w-24 bg-muted rounded-full overflow-hidden">
              <div className="h-full w-[96%] bg-primary" />
            </div>
          </div>
        </div>
        <div className="flex items-center justify-between p-3 rounded-lg bg-muted/30 border border-transparent hover:border-border transition-all">
          <span className="text-foreground text-sm">Packaging Type</span>
          <div className="flex items-center gap-2">
            <span className="text-muted-foreground text-sm">Sealed Containers</span>
            <CheckCircle className="size-4 text-primary" />
          </div>
        </div>
      </div>
    </div>
  );
}
