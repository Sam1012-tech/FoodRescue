# AnnaSetu

> Bridging surplus to scarcity, one meal at a time.

An AI-orchestrated food redistribution network for India. The system that turns the food being thrown away into meals appearing on tables within 90 minutes.

---

## The Problem We Refuse to Accept

India today lives a contradiction.

**On one side:**

- 74 million tonnes of food is wasted in India every year. ₹92,000 crore worth, landfilled or incinerated.
- Indian weddings alone waste enough food to feed 2 crore people daily. Corporate cafeterias in Bengaluru throw away 20-30% of every catered meal.
- The food is fresh. It is cooked. It is paid for. It simply arrives too late for anyone to eat it.

**On the other side:**

- 195 million Indians go to bed hungry every night.
- 1 in 5 Indian children is undernourished.
- Bengaluru alone has over 9,000 children in registered orphanages and shelters where the next meal is uncertain. They live within 5 kilometres of the food being discarded.

The contradiction is not economic. It is logistical. The food and hunger are separated by less than an hour, less than 5 km, and a problem nobody has solved at scale.

**AnnaSetu is that bridge.**

---

## What AnnaSetu Does

A corporate cafeteria finishes lunch service. Kitchen staff take one photo of leftover food. Within seconds, a Gemini Vision AI model identifies what the food is, counts portions, estimates freshness. The system instantly ranks nearby NGOs and orphanages by demand, distance, storage capacity, and operational reliability. If one NGO can't absorb the full donation, the system splits it across two or three, notifying all simultaneously.

A volunteer is dispatched in real-time. GPS coordinates, ETA, route optimization—all automated. The donation status flows live: _pickup in progress → in transit → delivered_. The donor sees impact immediately: _"Your 45 meals reached Akshaya Patra at 2:47 PM. 32 children fed."_

Every transaction is logged—photo, AI analysis, GPS coordinates, volunteer chain of custody, recipient verification. The donor downloads a monthly CSR impact report that is FSSAI-compliant and qualifies for Section 80G tax deduction.

The entire flow: photo to confirmed pickup, under 2 minutes. Zero human matching. Zero paperwork.

---

## How It Works — Technical Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE ANNASETU FLOW                             │
└─────────────────────────────────────────────────────────────────┘

1. DONOR CAPTURES IMAGE
   ↓
   Android App (Kotlin/Jetpack Compose)
   → Firebase Storage (photo upload)
   → Firestore Document created (status: "analyzing")

2. CLOUD FUNCTION TRIGGERS AUTOMATICALLY
   ↓
   Cloud Function on `onDonationCreated` (TypeScript)
   → Fetches image from Storage
   → Sends to Gemini 2.5 Flash Vision API

3. VISION ANALYSIS (Gemini API)
   ↓
   Structured JSON output:
   {
     items: [food names, quantities],
     estimatedMeals: number,
     freshness: "good" | "fair" | "poor",
     foodType: "veg" | "non-veg" | "jain" | "mixed",
     confidence: 0-1
   }
   
   Status tier computed:
   - "green" if <60 min old (safe 90 min window)
   - "yellow" if 60-180 min old (urgent)
   - "delist" if >180 min old (unsafe)

4. MATCHING ENGINE (Python FastAPI Backend)
   ↓
   Multi-factor NGO Scoring:
   - Distance (25%) — Haversine formula
   - Current Demand (20%) — meals needed vs capacity
   - Available Storage (15%) — can NGO hold this food?
   - Reliability Score (10%) — historical acceptance rate
   - Food Freshness (15%) — emergency boost if expiring
   - Volunteer Proximity (10%)
   - Live Traffic Penalty (5%)
   
   Result: Ranked list of NGOs with scores 0-100
   
   If top match has capacity < 100%, system auto-splits:
   → NGO #1 takes 60 portions
   → NGO #2 takes 35 portions
   → Both notified simultaneously

5. VOLUNTEER DISPATCH
   ↓
   Find idle volunteer nearest to both pickup and delivery
   Scoring: Distance (40%), Reliability (30%), Availability (20%), Workload (10%)
   
   Create Mission document in Firestore
   Firebase Cloud Messaging → Notify volunteer in real-time

6. REAL-TIME SYNC
   ↓
   All surfaces (donor app, NGO app, volunteer app, admin dashboard)
   listen to Firestore collection updates
   
   Status changes flow live:
   matched → in_transit → delivered
   
   Donor app shows live ETA, route, volunteer photo

7. IMPACT RECORDED
   ↓
   Firestore logs:
   - Donation timestamp + GPS coordinates
   - AI analysis (what, how much, freshness)
   - NGO(s) that received it
   - Volunteer who delivered it
   - Timestamp of delivery
   
   System calculates:
   - Weight of food diverted (kg)
   - CO₂e saved (2.5 kg per meal)
   - Beneficiaries reached
```

---

## The Technology Stack

**Mobile App** — Kotlin, Jetpack Compose, Firebase Auth, Firestore real-time listeners, Cloud Storage, FCM  
**Backend Matching Engine** — Python FastAPI, Haversine distance algorithm, weighted multi-factor scoring  
**Vision Intelligence** — Google Gemini 2.5 Flash API, structured JSON extraction from food photos  
**Cloud Infrastructure** — Firebase Cloud Functions (TypeScript), Firestore as real-time database  
**Web Dashboard** — React/TypeScript, Recharts for analytics, live Firestore subscription  
**Real-time Sync** — Firestore document listeners (all 4 surfaces consume same database)  
**Mapping & Routing** — Google Maps API integration (distance matrix, route optimization)  
**Compliance** — FSSAI Food Donation Guidelines 2023 (GPS logging, chain of custody, recipient verification)

---

## What Makes AnnaSetu Different

**1. AI That Acts, Not Just Answers**

Most food-rescue apps use a photo to notify a human coordinator, who then manually searches for NGOs. AnnaSetu replaces that human entirely. Gemini Vision extracts structured data → matching engine scores all 200+ NGOs in the city → best match is auto-notified → volunteer is dispatched. Full orchestration. No human in the loop until delivery.

**2. Multi-NGO Donation Splitting**

A donation arrives: 60 meals. No single NGO nearby has capacity for 60 today. AnnaSetu's system detects this and does what a human would have to do manually (but doesn't, because it's too friction-heavy): it splits the donation. 35 to Akshaya Patra, 25 to Feeding India. Both NGOs are notified in the same second. Two volunteers are dispatched. Two delivery routes optimized. One donation, two lives saved simultaneously.

No other food-rescue system at hackathon scale does this.

**3. Predictive Surplus Intelligence**

After 30 days of data, the backend learns patterns. Google Bangalore's cafeteria wastes 300+ meals every Wednesday at 2 PM. The system tells them: "Your Wednesday all-hands is over-catered by 15%. Reduce by this much, save ₹1.9 lakhs per year and 700 kg of food waste annually." Donors can act upstream, not just redistribute downstream.

**4. Auditable Corporate CSR**

Every donation is timestamped, GPS-located, photo-logged, and recipient-verified. The system generates monthly FSSAI-compliant impact PDFs. Companies can claim these under Section 80G tax deduction. This is what unlocks enterprise adoption. Compliance and tax incentives, not just altruism.

**5. Escalation for Edge Cases**

Real systems handle failures. Volunteer cancels mid-route → system auto-reassigns to next nearest. No NGO accepts within 5 minutes → escalate to community kitchens and BBMP partner network. Donation about to expire → emergency broadcast mode, search radius doubles. The system is resilient.

---

## What's Built vs. What's Planned

### Fully Implemented

- ✅ Donor Android app — capture, upload, metadata form, CSR report view
- ✅ NGO Android app — real-time donation feed, accept/decline, live status
- ✅ Volunteer Android app — mission dispatch, route tracking, delivery confirmation
- ✅ Admin web dashboard — live operations map, AI inference logs, impact metrics
- ✅ Gemini Vision integration — food classification, portion estimation, freshness detection
- ✅ Smart matching engine — multi-factor NGO scoring with split-donation logic
- ✅ Firestore schema + security rules — role-based access (donor, NGO, volunteer, admin)
- ✅ Cloud Function on donation creation — auto-triggers vision analysis
- ✅ Real-time sync across all surfaces — Firestore listeners
- ✅ FSSAI-compliant CSR reporting — GPS logs, photo evidence, beneficiary tracking
- ✅ Firebase integration — Auth, Storage, Messaging, Firestore

### In Progress / Planned

- 🔄 Volunteer-initiated demand signal (NGOs can broadcast "we need food now" before donation arrives)
- 🔄 Beneficiary face-count at delivery (volunteer's camera confirms children/seniors actually received)
- 🔄 Voice-based donation in Kannada/Hindi (for non-tech donors)
- 🔄 BBMP & State Food Commission integration (anonymized data becomes real-time food security map for policy)
- 🔄 Multi-city expansion (Mumbai, Delhi, Hyderabad after Bengaluru validation)

---

## Impact Projection

If 50 corporate cafeterias in Bengaluru's tech corridor adopt AnnaSetu:

- **750,000+ meals rescued per year** (15 per cafeteria per day)
- **1,800+ tonnes of CO₂e avoided** (2.5 kg CO₂ per meal)
- **₹4.5 crore of food value redirected** from landfill to plate
- **12+ lakh verified beneficiary interactions** annually
- **500+ volunteer shifts logged** with impact transparency

That's scaling at a single hackathon project. Scale to 500 corporations: 7.5 million meals. Scale to all 1,000+ food-serving businesses in the city: 15 million meals. That is meaningful.

---

## Why This Matters for the Community

AnnaSetu is not a food delivery app. It is community infrastructure.

**For NGOs:** They get free operational layers—FSSAI compliance, donor receipts, beneficiary tracking—that currently cost them 4+ volunteer hours per week in paperwork. Those hours go back to the children.

**For Corporate Donors:** They shift from "we waste food" to "we prevent waste." The CSR story changes. The tax benefit is real. The impact is auditable.

**For Volunteers:** They have clear, GPS-optimized routes. No guesswork. No failed pickups because the food wasn't ready. Real-time notifications. Impact visible on their phone: how many people were fed because of this delivery.

**For the City:** Anonymised data (with NGO consent) becomes real-time food surplus and scarcity mapping. BBMP and the Karnataka State Food Commission get actual evidence of where the problem is, where the solution is, and how fast they move. Policy becomes data-driven, not guesswork.

**For the Moral Contour of Giving:** In a country where "annadana" (the gift of food) is one of the highest forms of _seva_, AnnaSetu doesn't replace human kindness. It removes the friction that stops kind people from acting on it. A donation that would have taken 2 hours to coordinate now takes 2 minutes. Kindness scales when friction vanishes.

---

## How to Run Locally

### Prerequisites
- Firebase project (Firestore, Storage, Cloud Functions, FCM enabled)
- Gemini API key
- Android Studio (for the Kotlin app)
- Node.js 18+ (for Cloud Functions and web dashboard)
- Python 3.9+ (for backend matching engine)

### Setup

1. **Clone and configure Firebase**
   ```bash
   # Download serviceAccountKey.json from Firebase Console
   cp serviceAccountKey.json backend/
   
   # Set Gemini API key
   export GEMINI_API_KEY="your-key"
   ```

2. **Backend (Matching Engine)**
   ```bash
   cd backend
   pip install -r requirements.txt
   python -m uvicorn app.main:app --reload
   # Runs on http://localhost:8000
   ```

3. **Cloud Functions (Vision Analysis)**
   ```bash
   cd vision/functions
   npm install
   npm run deploy
   # Deploys onDonationCreated trigger
   ```

4. **Web Dashboard**
   ```bash
   cd frontend
   npm install
   npm run dev
   # Runs on http://localhost:5173
   ```

5. **Mobile App**
   ```bash
   cd bite_app
   # Open in Android Studio, sync Gradle, run on emulator or device
   ```

6. **Seed sample data**
   ```bash
   cd backend
   python -c "from app.seed_data import seed_database; seed_database()"
   # Creates sample NGOs, volunteers, donations
   ```

---

## API Endpoints (Backend)

```
GET  /                          — Health check
GET  /dashboard/metrics         — Real-time meal count, volunteers active, avg response time
GET  /dashboard/predictions     — Predictive surplus (where will food be wasted next?)
GET  /dashboard/decision-logs   — XAI logs (why was this NGO matched?)
POST /donations/{id}/match      — Manually trigger matching (for dashboard)
POST /system/maintenance/check-escalations — Monitor expiring donations
GET  /ngos                      — List active NGOs
GET  /volunteers                — List idle volunteers
GET  /donations                 — List pending/matched donations
```

---

## Firestore Schema

```
donations/{donationId}
  ├─ donorId: string (Firebase Auth UID)
  ├─ photoUrl: string (Firebase Storage URL)
  ├─ status: string ("analyzing" | "analyzed" | "matched" | "in_transit" | "delivered")
  ├─ createdAt: timestamp
  ├─ expiresAt: timestamp (90 min window from analysis)
  ├─ aiAnalysis: {
  │    items: [{name, quantity, unit}],
  │    estimatedMeals: number,
  │    freshness: string,
  │    foodType: string,
  │    confidence: number
  ├─ safetyTier: string ("green" | "yellow" | "delist")
  ├─ matchedTo: [{ngoId, status, mealsAllocated}]
  └─ donorMetadata: {
       vegStatus, urgency, weightKg, portions, shelfLifeHours, allergens, contactName, contactPhone, notes
     }

ngos/{ngoId}
  ├─ name: string
  ├─ location: GeoPoint
  ├─ capacity: number (max meals they can store)
  ├─ currentDemand: number (meals they need today)
  ├─ reliabilityScore: number (0-1, historical acceptance rate)
  ├─ isCooking: boolean (penalty if true)
  └─ isActive: boolean

volunteers/{volunteerId}
  ├─ name: string
  ├─ currentLocation: GeoPoint
  ├─ status: string ("idle" | "in_transit" | "delivered")
  ├─ reliabilityScore: number (0-1)
  ├─ currentAssignments: number
  └─ isActive: boolean

missions/{missionId}
  ├─ donationId: string
  ├─ ngoId: string
  ├─ volunteerId: string
  ├─ status: string ("matched" | "in_transit" | "delivered" | "escalated")
  ├─ assignedAt: timestamp
  └─ aiExplanation: {reason, selectedNgo, score}
```

---

## Security & Compliance

- **Firestore Rules:** Role-based access control (donors create/update their own, NGOs read and accept, backend service account can update)
- **Food Safety:** Every donation tracked with timestamp, location, photo, and recipient verification (FSSAI Guidelines 2023)
- **Data Privacy:** Beneficiary names not stored. Only counts and locations. Anonymised for policy research.
- **Tax Compliance:** CSR reports include all required fields for Section 80G deduction claims

---

## Team

Built during a hackathon sprint by Sreeya Chand and contributors.

Inspired by the work of Robin Hood Army, who have been proving for 11 years that no one in India needs to go hungry—they just needed someone to connect the food to the hunger.

---

## Acknowledgements

- **Robin Hood Army** — 11 years of teaching us that this is possible
- **FSSAI** — Food Donation Guidelines that make this legal and safe
- **Google Cloud (Gemini Vision API)** — Making food analysis accessible
- **Firebase** — Making infrastructure frictionless for a hackathon team
- **195 million Indians going to bed hungry every night** — This is for you. We're sorry this took so long to build. We're building faster now.

---

## License

MIT. Use it. Fork it. Make it better. Make it reach every city. The problem is too big for any one team to own.

