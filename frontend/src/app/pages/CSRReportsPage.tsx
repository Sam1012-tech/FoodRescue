// frontend/src/app/pages/CSRReportsPage.tsx
import React, { useRef, useState } from 'react';
import { motion } from 'motion/react';
import {
  FileText, Download, Share2, ShieldCheck, TrendingUp, History,
  Printer, Leaf, Users2, Award, Package, CheckCircle2, Loader2
} from 'lucide-react';

// ─── Report data (replace with Firestore live queries) ────────────────────────

const COMPANY = 'Google Bangalore';
const REPORT_MONTH = 'April 2026';

const IMPACT = {
  totalMeals:         4780,
  totalWeightKg:      1435,         // ~300g/meal
  totalDonations:     42,
  beneficiaries:      14240,
  co2Kg:              11950,        // 2.5 kg/meal
  avgPickupMins:      12.7,
  volunteersEngaged:  8,
  ngoPartners:        5,
};

const DONATION_LOG = [
  { date: 'Apr 02, 2026', event: 'All-Hands Meeting', portions: 220, ngo: 'Hope Shelter', volunteer: 'Arjun Kumar', status: 'Delivered' },
  { date: 'Apr 09, 2026', event: 'All-Hands Meeting', portions: 248, ngo: 'Akshaya Patra', volunteer: 'Priya S.',    status: 'Delivered' },
  { date: 'Apr 12, 2026', event: 'Investor Lunch',    portions: 90,  ngo: 'Feeding India', volunteer: 'Ravi M.',    status: 'Delivered' },
  { date: 'Apr 16, 2026', event: 'All-Hands Meeting', portions: 232, ngo: 'Hope Shelter',  volunteer: 'Arjun Kumar', status: 'Delivered' },
  { date: 'Apr 19, 2026', event: 'Team Dinner',       portions: 85,  ngo: 'Robin Hood',   volunteer: 'Lakshmi V.',  status: 'Delivered' },
  { date: 'Apr 23, 2026', event: 'All-Hands Meeting', portions: 241, ngo: 'Akshaya Patra', volunteer: 'Ravi M.',    status: 'Delivered' },
  { date: 'Apr 26, 2026', event: 'Leadership Summit', portions: 140, ngo: 'Feeding India', volunteer: 'Priya S.',   status: 'Delivered' },
];

const PREVIOUS_REPORTS = [
  { name: 'Q1_2026_Sustainability_Summary.pdf', date: 'April 02, 2026', size: '2.4 MB', type: 'Quarterly' },
  { name: 'March_Impact_Certificate.pdf',       date: 'April 01, 2026', size: '1.1 MB', type: 'Monthly'   },
  { name: 'Annual_Waste_Reduction_Audit_2025.pdf', date: 'Jan 15, 2026', size: '8.7 MB', type: 'Annual'   },
];

// ─── PDF printable report ─────────────────────────────────────────────────────

function PrintableReport() {
  return (
    <div id="csr-report-printable" className="hidden print:block p-12 text-black bg-white min-h-screen font-sans">
      {/* Cover header */}
      <div className="border-b-4 border-emerald-600 pb-6 mb-8">
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-3xl font-black text-gray-900">{COMPANY}</h1>
            <h2 className="text-xl font-semibold text-emerald-700 mt-1">Monthly CSR Impact Report</h2>
            <p className="text-gray-500 mt-1">{REPORT_MONTH} · Certified by AnnaSetu Food Rescue</p>
          </div>
          <div className="text-right">
            <div className="text-4xl font-black text-emerald-600">{IMPACT.totalMeals.toLocaleString()}</div>
            <div className="text-sm text-gray-500">meals rescued this month</div>
          </div>
        </div>
      </div>

      {/* Impact stats grid */}
      <div className="grid grid-cols-4 gap-4 mb-8">
        {[
          { label: 'Total donations',      value: IMPACT.totalDonations          },
          { label: 'Weight (kg)',          value: `${IMPACT.totalWeightKg} kg`    },
          { label: 'CO₂ saved',            value: `${IMPACT.co2Kg.toLocaleString()} kg` },
          { label: 'Beneficiaries',        value: IMPACT.beneficiaries.toLocaleString() },
          { label: 'NGO Partners',         value: IMPACT.ngoPartners             },
          { label: 'Volunteers engaged',   value: IMPACT.volunteersEngaged       },
          { label: 'Avg pickup time',      value: `${IMPACT.avgPickupMins} min`  },
          { label: 'Waste diversion rate', value: '94.2%'                        },
        ].map((s, i) => (
          <div key={i} className="border border-gray-200 rounded-lg p-3">
            <p className="text-xs text-gray-400 mb-1 uppercase font-semibold tracking-wide">{s.label}</p>
            <p className="text-xl font-black text-gray-900">{s.value}</p>
          </div>
        ))}
      </div>

      {/* Donation log table */}
      <h3 className="text-sm font-bold uppercase tracking-widest text-gray-500 mb-3">Donation Log</h3>
      <table className="w-full text-sm border-collapse mb-8">
        <thead>
          <tr className="bg-gray-100">
            {['Date', 'Event', 'Portions', 'NGO Partner', 'Volunteer', 'Status'].map(h => (
              <th key={h} className="text-left p-2 text-xs font-bold text-gray-500 uppercase border border-gray-200">{h}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {DONATION_LOG.map((row, i) => (
            <tr key={i} className={i % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
              <td className="p-2 border border-gray-200 text-xs">{row.date}</td>
              <td className="p-2 border border-gray-200 text-xs font-medium">{row.event}</td>
              <td className="p-2 border border-gray-200 text-xs text-center font-bold">{row.portions}</td>
              <td className="p-2 border border-gray-200 text-xs">{row.ngo}</td>
              <td className="p-2 border border-gray-200 text-xs">{row.volunteer}</td>
              <td className="p-2 border border-gray-200 text-xs text-emerald-700 font-semibold">{row.status}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* FSSAI compliance note */}
      <div className="border border-emerald-200 bg-emerald-50 rounded-lg p-4 mb-6">
        <p className="text-xs font-bold text-emerald-800 mb-1">FSSAI Compliance Certification</p>
        <p className="text-xs text-emerald-700">
          All donations tracked herein were facilitated in compliance with the Food Safety and Standards Authority
          of India (FSSAI) Food Donation Guidelines 2023. Each transfer was logged with GPS coordinates, timestamp,
          volunteer chain-of-custody, and recipient NGO acknowledgement. This report can be used for Section 80G
          corporate tax deduction claims.
        </p>
      </div>

      {/* Signature block */}
      <div className="flex justify-between mt-8 pt-4 border-t border-gray-200">
        <div>
          <p className="text-xs text-gray-400">Generated by AnnaSetu Dashboard</p>
          <p className="text-xs text-gray-400">{new Date().toLocaleDateString('en-IN', { day: 'numeric', month: 'long', year: 'numeric' })}</p>
        </div>
        <div className="text-right">
          <div className="border-t border-gray-400 w-40 mb-1" />
          <p className="text-xs text-gray-500">Authorised Signatory</p>
        </div>
      </div>
    </div>
  );
}

// ─── Main page ────────────────────────────────────────────────────────────────

export function CSRReportsPage() {
  const [generating, setGenerating] = useState(false);

  const handleGeneratePDF = async () => {
    setGenerating(true);

    // Brief delay for UX, then trigger browser print dialog
    // In production: swap this for jsPDF / html2canvas or a backend PDF endpoint
    await new Promise(r => setTimeout(r, 1200));

    // Make the printable section visible, trigger print, then hide
    const el = document.getElementById('csr-report-printable');
    if (el) {
      el.classList.remove('hidden');
      el.classList.add('block');
    }

    window.print();

    if (el) {
      el.classList.add('hidden');
      el.classList.remove('block');
    }

    setGenerating(false);
  };

  return (
    <>
      {/* Hidden print-only DOM */}
      <PrintableReport />

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="p-6 max-w-[1800px] mx-auto space-y-6"
      >
        {/* ── Header ──────────────────────────────────────────────────────── */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <div className="p-2.5 rounded-xl bg-primary/10">
              <FileText className="size-6 text-primary" />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-foreground tracking-tight">Corporate Impact & CSR</h1>
              <p className="text-muted-foreground text-sm">Certified sustainability reporting · FSSAI-compliant · ESG-ready</p>
            </div>
          </div>

          <button
            onClick={handleGeneratePDF}
            disabled={generating}
            className="flex items-center gap-2 bg-primary text-white px-6 py-2.5 rounded-xl font-bold shadow-md hover:bg-primary/90 transition-all disabled:opacity-70"
          >
            {generating ? (
              <><Loader2 className="size-4 animate-spin" /> Generating…</>
            ) : (
              <><Printer className="size-4" /> Download {REPORT_MONTH} Report</>
            )}
          </button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* ── Left: main content 3/4 ─────────────────────────────────────── */}
          <div className="lg:col-span-3 space-y-6">

            {/* Impact stat tiles */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {[
                { label: 'Meals rescued',   value: IMPACT.totalMeals.toLocaleString(),   icon: Package,  accent: 'text-emerald-400', bg: 'bg-emerald-500/10' },
                { label: 'CO₂ offset',      value: `${IMPACT.co2Kg.toLocaleString()} kg`, icon: Leaf,    accent: 'text-green-400',   bg: 'bg-green-500/10'   },
                { label: 'Beneficiaries',   value: IMPACT.beneficiaries.toLocaleString(), icon: Users2,  accent: 'text-blue-400',    bg: 'bg-blue-500/10'    },
                { label: 'Food weight',     value: `${IMPACT.totalWeightKg} kg`,          icon: TrendingUp, accent: 'text-amber-400', bg: 'bg-amber-500/10' },
              ].map((s, i) => (
                <motion.div
                  key={i}
                  initial={{ opacity: 0, y: 12 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: i * 0.07 }}
                  className="bg-card border border-border rounded-xl p-4 shadow-sm"
                >
                  <div className={`p-2 rounded-lg ${s.bg} inline-flex mb-3`}>
                    <s.icon className={`size-4 ${s.accent}`} />
                  </div>
                  <p className="text-xl font-black text-foreground">{s.value}</p>
                  <p className="text-xs text-muted-foreground mt-0.5">{s.label} · {REPORT_MONTH}</p>
                </motion.div>
              ))}
            </div>

            {/* Donation log table */}
            <div className="bg-card border border-border rounded-xl p-6">
              <div className="flex items-center justify-between mb-5">
                <h3 className="font-semibold text-foreground">Verified Donation Log</h3>
                <span className="text-[10px] font-bold text-primary bg-primary/10 px-2 py-1 rounded">
                  FSSAI COMPLIANT
                </span>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-border">
                      {['Date', 'Event', 'Portions', 'NGO Partner', 'Volunteer', 'Status'].map(h => (
                        <th key={h} className="text-left pb-3 text-[10px] font-bold text-muted-foreground uppercase tracking-widest pr-4">{h}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    {DONATION_LOG.map((row, i) => (
                      <tr key={i} className="border-b border-border/30 hover:bg-muted/20 transition-colors">
                        <td className="py-3 pr-4 text-xs text-muted-foreground">{row.date}</td>
                        <td className="py-3 pr-4 text-xs font-medium text-foreground">{row.event}</td>
                        <td className="py-3 pr-4 text-xs font-bold text-foreground">{row.portions}</td>
                        <td className="py-3 pr-4 text-xs text-foreground">{row.ngo}</td>
                        <td className="py-3 pr-4 text-xs text-muted-foreground">{row.volunteer}</td>
                        <td className="py-3">
                          <span className="flex items-center gap-1 text-emerald-500 text-[10px] font-bold">
                            <CheckCircle2 className="size-3" /> {row.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Previous reports */}
            <div className="bg-card border border-border rounded-xl p-6">
              <h3 className="font-semibold mb-5 flex items-center justify-between">
                Previous Reports
                <History className="size-4 text-muted-foreground" />
              </h3>
              <div className="space-y-3">
                {PREVIOUS_REPORTS.map((doc, i) => (
                  <div key={i} className="flex items-center gap-4 p-4 border border-border rounded-xl hover:bg-accent/40 transition-colors group cursor-pointer">
                    <div className="size-10 rounded-xl bg-muted flex items-center justify-center flex-shrink-0">
                      <FileText className="size-5 text-muted-foreground group-hover:text-primary transition-colors" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-bold text-foreground truncate">{doc.name}</p>
                      <p className="text-[10px] text-muted-foreground">{doc.date} · {doc.size} · {doc.type}</p>
                    </div>
                    <div className="flex gap-1">
                      <button className="p-2 hover:bg-background rounded-lg transition-colors">
                        <Download className="size-4 text-muted-foreground hover:text-primary transition-colors" />
                      </button>
                      <button className="p-2 hover:bg-background rounded-lg transition-colors">
                        <Share2 className="size-4 text-muted-foreground hover:text-primary transition-colors" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* ── Right sidebar 1/4 ─────────────────────────────────────────── */}
          <div className="space-y-6">
            {/* ESG compliance card */}
            <div className="bg-[#1F3A33] text-white rounded-xl p-6 shadow-xl">
              <div className="flex items-center gap-2 mb-5">
                <ShieldCheck className="size-5 text-primary" />
                <h3 className="text-xs font-bold uppercase tracking-widest text-primary/80">Compliance Status</h3>
              </div>
              <div className="text-center py-4 border-b border-white/10 mb-5">
                <p className="text-5xl font-black mb-1">A+</p>
                <p className="text-[10px] text-white/50 uppercase">Global ESG Rating</p>
              </div>
              <div className="space-y-4 text-xs">
                {[
                  { label: 'FSSAI Certified',      value: '✓ Active',    color: 'text-emerald-400' },
                  { label: 'Waste Diversion',       value: '94%',         color: 'text-white' },
                  { label: 'Carbon Offset',         value: '11,950 kg',   color: 'text-white' },
                  { label: 'Donation Accuracy',     value: '99.8%',       color: 'text-white' },
                  { label: 'Section 80G Eligible',  value: '✓ Yes',       color: 'text-emerald-400' },
                ].map((s, i) => (
                  <div key={i} className="flex justify-between items-center">
                    <span className="text-white/60">{s.label}</span>
                    <span className={`font-bold ${s.color}`}>{s.value}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Top NGO partners */}
            <div className="bg-card border border-border rounded-xl p-6">
              <div className="flex items-center gap-2 mb-4">
                <Award className="size-4 text-primary" />
                <h3 className="text-xs font-bold uppercase tracking-widest text-muted-foreground">NGO Partners</h3>
              </div>
              <div className="space-y-4">
                {[
                  { name: 'Hope Shelter',  meals: 1240 },
                  { name: 'Akshaya Patra', meals: 1089 },
                  { name: 'Feeding India', meals: 892  },
                  { name: 'Robin Hood Army', meals: 782 },
                  { name: 'Others',        meals: 777  },
                ].map((ngo, i) => {
                  const pct = Math.round((ngo.meals / IMPACT.totalMeals) * 100);
                  return (
                    <div key={i}>
                      <div className="flex justify-between text-xs mb-1">
                        <span className="text-foreground font-medium">{ngo.name}</span>
                        <span className="text-muted-foreground">{ngo.meals} meals</span>
                      </div>
                      <div className="h-1.5 bg-muted rounded-full overflow-hidden">
                        <motion.div
                          className="h-full bg-primary rounded-full"
                          initial={{ width: 0 }}
                          animate={{ width: `${pct}%` }}
                          transition={{ duration: 0.8, delay: i * 0.1 }}
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>

            {/* Generate shortcut */}
            <button
              onClick={handleGeneratePDF}
              disabled={generating}
              className="w-full flex flex-col items-center gap-2 p-5 rounded-xl border border-dashed border-primary/40 hover:border-primary hover:bg-primary/5 transition-all group"
            >
              <Printer className="size-6 text-primary group-hover:scale-110 transition-transform" />
              <p className="text-xs font-bold text-foreground">Download Full PDF Report</p>
              <p className="text-[10px] text-muted-foreground text-center">
                Includes donation log, GPS data, volunteer chain-of-custody & FSSAI certification
              </p>
            </button>
          </div>
        </div>
      </motion.div>
    </>
  );
}
