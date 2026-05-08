import React, { useState } from 'react';
import { TopNav } from '@/app/components/TopNav';
import { Sidebar } from '@/app/components/Sidebar';
import { OverviewPage } from '@/app/pages/OverviewPage';
import { LiveOperationsPage } from '@/app/pages/LiveOperationsPage';
import { AIIntelligencePage } from '@/app/pages/AIIntelligencePage';
import { PredictionsPage } from '@/app/pages/PredictionsPage';
import { NGONetworkPage } from '@/app/pages/NGONetworkPage';
import { LogisticsPage } from '@/app/pages/LogisticsPage';
import { AnalyticsPage } from '@/app/pages/AnalyticsPage';
import { EmergencyModePage } from '@/app/pages/EmergencyModePage';
import { CSRReportsPage } from '@/app/pages/CSRReportsPage';
import { SettingsPage } from '@/app/pages/SettingsPage';
import { AnimatePresence, motion } from 'motion/react';

export default function App() {
  const [activeItem, setActiveItem] = useState(0);

  const renderPage = () => {
    switch (activeItem) {
      case 0: return <OverviewPage />;
      case 1: return <LiveOperationsPage />;
      case 2: return <AIIntelligencePage />;
      case 3: return <PredictionsPage />;
      case 4: return <NGONetworkPage />;
      case 5: return <LogisticsPage />;
      case 6: return <AnalyticsPage />;
      case 7: return <EmergencyModePage />;
      case 8: return <CSRReportsPage />;
      case 9: return <SettingsPage />;
      default: return <OverviewPage />;
    }
  };

  return (
    <div className="size-full h-screen flex flex-col bg-background text-foreground overflow-hidden" style={{ fontFamily: 'Inter, sans-serif' }}>
      <TopNav />
      <div className="flex-1 flex overflow-hidden">
        <Sidebar activeItem={activeItem} onSelect={setActiveItem} />
        <main className="flex-1 overflow-y-auto bg-background/50">
          <AnimatePresence mode="wait">
            <motion.div
              key={activeItem}
              initial={{ opacity: 0, x: 10 }}
              animate={{ opacity: 1, x: 0 }}
              exit={{ opacity: 0, x: -10 }}
              transition={{ duration: 0.2 }}
              className="h-full"
            >
              {renderPage()}
            </motion.div>
          </AnimatePresence>
        </main>
      </div>
    </div>
  );
}