import apiClient from './client';
import type { BrandResponse, BrandDetailResponse, BrandCreateRequest, BrandAssetResponse, BrandAssetCreateRequest } from '../types/api';

export const brandsApi = {
  list: () => apiClient.get<BrandResponse[]>('/brands').then(r => r.data),
  get: (id: number) => apiClient.get<BrandDetailResponse>(`/brands/${id}`).then(r => r.data),
  create: (data: BrandCreateRequest) => apiClient.post<BrandResponse>('/brands', data).then(r => r.data),
  update: (id: number, data: BrandCreateRequest) => apiClient.put<BrandResponse>(`/brands/${id}`, data).then(r => r.data),
  delete: (id: number) => apiClient.delete(`/brands/${id}`),
  getAssets: (id: number) => apiClient.get<BrandAssetResponse[]>(`/brands/${id}/assets`).then(r => r.data),
  addAsset: (id: number, data: BrandAssetCreateRequest) => apiClient.post<BrandAssetResponse>(`/brands/${id}/assets`, data).then(r => r.data),
  deleteAsset: (brandId: number, assetId: number) => apiClient.delete(`/brands/${brandId}/assets/${assetId}`),
};
