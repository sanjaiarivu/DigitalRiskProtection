import apiClient from './client';
import type { SocialCandidateResponse, ScanResultResponse } from '../types/api';

export const socialApi = {
  getCandidates: () => apiClient.get<SocialCandidateResponse[]>('/social/candidates').then(r => r.data),
  addCandidate: (candidate: Omit<SocialCandidateResponse, 'id' | 'synthetic' | 'source' | 'importedAt'>) =>
    apiClient.post<SocialCandidateResponse>('/social/candidates', candidate).then(r => r.data),
  scan: () => apiClient.post<ScanResultResponse>('/social/scan').then(r => r.data),
};
