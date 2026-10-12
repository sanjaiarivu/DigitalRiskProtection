import apiClient from './client';
import type {
  ThreatDetectionResponse,
  ThreatDetectionDetailResponse,
  StatusUpdateRequest,
  NoteCreateRequest,
  InvestigationNoteResponse,
  AiExplanationResponse,
} from '../types/api';

export interface DetectionFilters {
  brandId?: number;
  status?: string;
  riskLevel?: string;
  candidateType?: string;
}

export const detectionsApi = {
  list: (filters?: DetectionFilters) =>
    apiClient.get<ThreatDetectionResponse[]>('/detections', { params: filters }).then(r => r.data),
  get: (id: number) => apiClient.get<ThreatDetectionDetailResponse>(`/detections/${id}`).then(r => r.data),
  updateStatus: (id: number, data: StatusUpdateRequest) =>
    apiClient.patch<ThreatDetectionResponse>(`/detections/${id}/status`, data).then(r => r.data),
  addNote: (id: number, data: NoteCreateRequest) =>
    apiClient.post<InvestigationNoteResponse>(`/detections/${id}/notes`, data).then(r => r.data),
  getExplanation: (id: number) =>
    apiClient.post<AiExplanationResponse>(`/detections/${id}/explanation`).then(r => r.data),
};
