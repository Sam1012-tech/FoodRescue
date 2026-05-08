import { Target, Clock, Award, Leaf } from 'lucide-react';

const matches = [
  { ngo: 'Akshaya Patra', score: 98, eta: '8 min', reliability: 'Excellent', freshness: 'High' },
  { ngo: 'Feeding India', score: 94, eta: '12 min', reliability: 'Excellent', freshness: 'High' },
  { ngo: 'Robin Hood Army', score: 89, eta: '15 min', reliability: 'Very Good', freshness: 'Medium' },
];

export function SmartMatching() {
  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <div className="flex items-center gap-3 mb-6">
        <div className="size-10 rounded-lg bg-primary/10 flex items-center justify-center">
          <Target className="size-5 text-primary" />
        </div>
        <div>
          <h3 className="text-foreground mb-0.5 font-semibold">Smart Matching Engine</h3>
          <p className="text-muted-foreground text-sm">AI-optimized NGO selection</p>
        </div>
      </div>
      <div className="space-y-4">
        {matches.map((match, index) => (
          <div key={index} className="p-4 rounded-lg bg-muted/30 border border-border hover:border-primary/30 transition-all">
            <div className="flex items-center justify-between mb-3">
              <p className="text-foreground font-medium">{match.ngo}</p>
              <div className="flex items-center gap-2">
                <span className="text-xs text-muted-foreground">AI Score</span>
                <div className="px-2 py-1 rounded-full bg-primary/10 text-primary text-sm font-medium">
                  {match.score}%
                </div>
              </div>
            </div>
            <div className="grid grid-cols-3 gap-3">
              <div className="flex items-center gap-2">
                <Clock className="size-4 text-primary" />
                <div>
                  <p className="text-xs text-muted-foreground">ETA</p>
                  <p className="text-sm text-foreground">{match.eta}</p>
                </div>
              </div>
              <div className="flex items-center gap-2">
                <Award className="size-4 text-primary" />
                <div>
                  <p className="text-xs text-muted-foreground">Reliability</p>
                  <p className="text-sm text-foreground">{match.reliability}</p>
                </div>
              </div>
              <div className="flex items-center gap-2">
                <Leaf className="size-4 text-primary" />
                <div>
                  <p className="text-xs text-muted-foreground">Freshness</p>
                  <p className="text-sm text-foreground">{match.freshness}</p>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
