import apiClient from './client';
import type { AppCandidateResponse, ScanResultResponse } from '../types/api';

export const appsApi = {
  getCandidates: () => apiClient.get<AppCandidateResponse[]>('/apps/candidates').then(r => r.data),
  scan: () => apiClient.post<ScanResultResponse>('/apps/scan').then(r => r.data),
};
