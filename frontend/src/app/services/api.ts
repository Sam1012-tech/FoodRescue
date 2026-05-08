import axios from 'axios';

const API_BASE_URL = 'http://localhost:8000';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getMetrics = async () => {
  const response = await api.get('/dashboard/metrics');
  return response.data;
};

export const getPredictions = async () => {
  const response = await api.get('/dashboard/predictions');
  return response.data;
};

export const getDecisionLogs = async () => {
  const response = await api.get('/dashboard/decision-logs');
  return response.data;
};

export const triggerMatch = async (donationId: string) => {
  const response = await api.post(`/donations/${donationId}/match`);
  return response.data;
};

export const runEscalations = async () => {
  const response = await api.post('/system/maintenance/check-escalations');
  return response.data;
};

export const getNGOs = async () => {
  const response = await api.get('/ngos');
  return response.data;
};

export const getVolunteers = async () => {
  const response = await api.get('/volunteers');
  return response.data;
};

export const getDonations = async () => {
  const response = await api.get('/donations');
  return response.data;
};
