export type AssetType = 'SOCIAL_ACCOUNT' | 'MOBILE_APP' | 'DOMAIN' | 'LOGO';
export type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export type DetectionStatus = 'NEW' | 'UNDER_INVESTIGATION' | 'LEGITIMATE' | 'DISMISSED';
export type CandidateType = 'SOCIAL' | 'APP';
export type DetectionType = 'BRAND_IMPERSONATION' | 'LOOK_ALIKE' | 'PHISHING_INDICATOR' | 'PUBLISHER_MISMATCH' | 'SUSPICIOUS_CONTENT';

export interface BrandResponse {
  id: number;
  name: string;
  description: string | null;
  website: string | null;
  logoUrl: string | null;
  aliases: string[];
  assetCount: number;
  createdAt: string;
}

export interface BrandDetailResponse extends BrandResponse {
  assets: BrandAssetResponse[];
  updatedAt: string;
}

export interface BrandAssetResponse {
  id: number;
  assetType: AssetType;
  platform: string | null;
  identifier: string;
  displayName: string | null;
  canonicalUrl: string | null;
  verified: boolean;
  createdAt: string;
}

export interface BrandCreateRequest {
  name: string;
  description?: string;
  website?: string;
  logoUrl?: string;
  aliases?: string[];
}

export interface BrandAssetCreateRequest {
  assetType: AssetType;
  platform?: string;
  identifier: string;
  displayName?: string;
  canonicalUrl?: string;
}

export interface SocialCandidateResponse {
  id: number;
  platform: string;
  username: string | null;
  displayName: string | null;
  accountId: string | null;
  profileUrl: string | null;
  description: string | null;
  avatarUrl: string | null;
  externalLinks: string | null;
  followerCount: number | null;
  synthetic: boolean;
  source: string;
  importedAt: string;
}

export interface AppCandidateResponse {
  id: number;
  appName: string;
  packageId: string | null;
  bundleId: string | null;
  publisherName: string | null;
  publisherId: string | null;
  storePlatform: string | null;
  storeUrl: string | null;
  description: string | null;
  iconUrl: string | null;
  externalLinks: string | null;
  downloadCount: string | null;
  rating: number | null;
  synthetic: boolean;
  source: string;
  importedAt: string;
}

export interface ThreatDetectionResponse {
  id: number;
  brandId: number;
  brandName: string;
  detectionType: DetectionType;
  candidateType: CandidateType;
  riskScore: number;
  riskLevel: RiskLevel;
  status: DetectionStatus;
  officialAsset: boolean;
  summary: string | null;
  candidateName: string | null;
  candidatePlatform: string | null;
  detectedAt: string;
  updatedAt: string;
}

export interface ThreatDetectionDetailResponse extends ThreatDetectionResponse {
  evidence: DetectionEvidenceResponse[];
  notes: InvestigationNoteResponse[];
  history: InvestigationHistoryResponse[];
  aiExplanation: string | null;
  socialCandidate: SocialCandidateResponse | null;
  appCandidate: AppCandidateResponse | null;
}

export interface DetectionEvidenceResponse {
  id: number;
  signalName: string;
  signalCategory: string | null;
  observedValue: string | null;
  expectedValue: string | null;
  evidenceExplanation: string;
  scoreContribution: number;
  confidence: string;
}

export interface InvestigationNoteResponse {
  id: number;
  author: string;
  noteText: string;
  createdAt: string;
}

export interface InvestigationHistoryResponse {
  id: number;
  previousStatus: string | null;
  newStatus: string;
  changedBy: string;
  changeReason: string | null;
  changedAt: string;
}

export interface DashboardSummaryResponse {
  totalBrands: number;
  totalCandidates: number;
  totalDetections: number;
  newDetections: number;
  highRiskDetections: number;
  criticalDetections: number;
  riskDistribution: Record<string, number>;
  detectionsByType: Record<string, number>;
  recentDetections: ThreatDetectionResponse[];
}

export interface AiExplanationResponse {
  summary: string;
  observedFacts: string[];
  reasonsForConcern: string[];
  uncertainties: string[];
  recommendedNextSteps: string[];
  generatedAt: string;
  modelUsed: string;
  fallback: boolean;
}

export interface ScanResultResponse {
  candidatesScanned: number;
  detectionsCreated: number;
  officialAssetsExcluded: number;
  scanDuration: string;
}

export interface StatusUpdateRequest {
  status: DetectionStatus;
  reason?: string;
}

export interface NoteCreateRequest {
  noteText: string;
  author?: string;
}
