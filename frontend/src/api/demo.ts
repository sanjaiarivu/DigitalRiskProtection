import apiClient from './client';

export const demoApi = {
  seed: () => apiClient.post<{ message: string }>('/demo/seed').then(r => r.data),
  clear: () => apiClient.post<{ message: string }>('/demo/clear').then(r => r.data),
};
