import { User, MapPin, Clock, CheckCircle } from 'lucide-react';

const volunteers = [
  { id: '#12', name: 'Rahul Kumar', status: 'delivering', from: 'Manyata Tech Park', to: 'Akshaya Patra', eta: '8 min', progress: 65 },
  { id: '#07', name: 'Priya Singh', status: 'pickup', from: 'Koramangala Office', to: 'Feeding India', eta: '12 min', progress: 35 },
  { id: '#23', name: 'Amit Patel', status: 'completed', from: 'Whitefield', to: 'Robin Hood Army', eta: 'Done', progress: 100 },
];

export function VolunteerLogistics() {
  return (
    <div className="bg-card border border-border rounded-xl p-6 shadow-sm">
      <div className="mb-6">
        <h3 className="text-foreground mb-1 font-semibold">Volunteer Logistics</h3>
        <p className="text-muted-foreground text-sm">Active rescue missions</p>
      </div>
      <div className="space-y-4">
        {volunteers.map((volunteer) => (
          <div key={volunteer.id} className="p-4 rounded-lg bg-muted/30 border border-border hover:border-primary/30 transition-all">
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center gap-3">
                <div className="size-10 rounded-full bg-primary/10 flex items-center justify-center">
                  <User className="size-5 text-primary" />
                </div>
                <div>
                  <p className="text-foreground font-medium">{volunteer.name}</p>
                  <p className="text-muted-foreground text-sm">Volunteer {volunteer.id}</p>
                </div>
              </div>
              <div
                className={`px-3 py-1 rounded-full text-xs font-medium ${
                  volunteer.status === 'completed'
                    ? 'bg-primary/10 text-primary'
                    : volunteer.status === 'delivering'
                    ? 'bg-[#D8A569]/10 text-[#D8A569]'
                    : 'bg-muted text-muted-foreground'
                }`}
              >
                {volunteer.status === 'completed' ? 'Completed' : volunteer.status === 'delivering' ? 'Delivering' : 'En Route'}
              </div>
            </div>
            <div className="space-y-2 mb-3">
              <div className="flex items-center gap-2 text-sm text-muted-foreground">
                <MapPin className="size-4 text-primary" />
                <span>{volunteer.from}</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-muted-foreground">
                <CheckCircle className="size-4 text-primary" />
                <span>{volunteer.to}</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-foreground">
                <Clock className="size-4 text-primary" />
                <span>ETA: {volunteer.eta}</span>
              </div>
            </div>
            <div className="relative h-2 bg-muted rounded-full overflow-hidden">
              <div
                className="absolute inset-y-0 left-0 bg-primary rounded-full transition-all duration-500"
                style={{ width: `${volunteer.progress}%` }}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
