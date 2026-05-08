import { Brain, TrendingUp } from 'lucide-react';

const predictions = [
  { location: 'Manyata Tech Park', probability: 78, meals: '40–60', time: '8:30 PM' },
  { location: 'Electronic City', probability: 65, meals: '30–45', time: '9:00 PM' },
  { location: 'Koramangala Offices', probability: 82, meals: '50–70', time: '8:45 PM' },
  { location: 'Whitefield Campus', probability: 71, meals: '35–50', time: '9:15 PM' },
];

export function PredictiveSurplus() {
  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <div className="size-10 rounded-lg bg-primary/10 flex items-center justify-center">
            <Brain className="size-5 text-primary" />
          </div>
          <div>
            <h3 className="text-foreground mb-0.5 font-semibold">Predictive Surplus Analysis</h3>
            <p className="text-muted-foreground text-sm">AI-powered forecasting</p>
          </div>
        </div>
        <span className="text-xs text-muted-foreground px-2 py-1 bg-muted/50 rounded">Next 3 hours</span>
      </div>
      <div className="space-y-3">
        {predictions.map((prediction, index) => (
          <div
            key={index}
            className="flex items-center justify-between p-4 rounded-lg bg-muted/30 hover:bg-muted/50 transition-colors border border-transparent hover:border-border"
          >
            <div className="flex-1">
              <div className="flex items-center gap-3 mb-2">
                <p className="text-foreground font-medium">{prediction.location}</p>
                <div className="flex items-center gap-1.5 px-2 py-0.5 rounded-full bg-primary/10">
                  <TrendingUp className="size-3 text-primary" />
                  <span className="text-xs text-primary font-medium">{prediction.probability}%</span>
                </div>
              </div>
              <div className="flex items-center gap-4 text-sm text-muted-foreground">
                <span>{prediction.meals} meals</span>
                <span>•</span>
                <span>{prediction.time}</span>
              </div>
            </div>
            <div className="relative w-16 h-16">
              <svg className="w-full h-full -rotate-90">
                <circle
                  cx="32"
                  cy="32"
                  r="28"
                  stroke="currentColor"
                  strokeWidth="4"
                  fill="none"
                  className="text-muted"
                />
                <circle
                  cx="32"
                  cy="32"
                  r="28"
                  stroke="currentColor"
                  strokeWidth="4"
                  fill="none"
                  strokeDasharray={`${prediction.probability * 1.76} 176`}
                  className="text-primary"
                />
              </svg>
              <div className="absolute inset-0 flex items-center justify-center">
                <span className="text-xs text-foreground font-medium">{prediction.probability}%</span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
