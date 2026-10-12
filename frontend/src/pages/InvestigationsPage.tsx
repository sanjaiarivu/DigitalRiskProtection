import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { detectionsApi } from '../api/detections';
import PageHeader from '../components/layout/PageHeader';
import RiskBadge from '../components/shared/RiskBadge';
import StatusBadge from '../components/shared/StatusBadge';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import { ShieldAlert, ExternalLink, Filter, Search } from 'lucide-react';
import { Link } from 'react-router-dom';
import type { DetectionStatus, RiskLevel, CandidateType } from '../types/api';

export default function InvestigationsPage() {
  const [statusFilter, setStatusFilter] = useState<string>('');
  const [riskFilter, setRiskFilter] = useState<string>('');
  const [candidateTypeFilter, setCandidateTypeFilter] = useState<string>('');
  const [searchQuery, setSearchQuery] = useState<string>('');

  const { data: detections, isLoading, error, refetch } = useQuery({
    queryKey: ['detections', statusFilter, riskFilter, candidateTypeFilter],
    queryFn: () =>
      detectionsApi.list({
        status: statusFilter || undefined,
        riskLevel: riskFilter || undefined,
        candidateType: candidateTypeFilter || undefined,
      }),
  });

  if (isLoading) return <LoadingSpinner message="Loading threat detections..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;

  const filteredDetections = (detections || []).filter((d) => {
    if (!searchQuery) return true;
    const q = searchQuery.toLowerCase();
    return (
      d.candidateName?.toLowerCase().includes(q) ||
      d.brandName?.toLowerCase().includes(q) ||
      d.detectionType?.toLowerCase().includes(q)
    );
  });

  return (
    <div className="space-y-6">
      <PageHeader
        title="Threat Investigations"
        description="Review algorithmic detections, examine explainable scoring signals, request AI analysis, and track incident status."
      />

      {/* Filter Toolbar */}
      <div className="bg-white rounded-xl border border-slate-200/90 p-4 shadow-xs flex flex-wrap gap-3 items-center justify-between">
        <div className="flex items-center gap-2 flex-1 min-w-[200px]">
          <Search className="w-4 h-4 text-slate-400" />
          <input
            type="text"
            placeholder="Search candidates or brands..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full text-xs rounded-lg border border-slate-300 px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        <div className="flex flex-wrap gap-2 items-center">
          <div className="flex items-center gap-1 text-xs text-slate-500">
            <Filter className="w-3.5 h-3.5" /> Filter:
          </div>

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="text-xs rounded-lg border border-slate-300 px-2.5 py-1.5 bg-white text-slate-700"
          >
            <option value="">All Statuses</option>
            <option value="NEW">NEW</option>
            <option value="UNDER_INVESTIGATION">UNDER INVESTIGATION</option>
            <option value="LEGITIMATE">LEGITIMATE</option>
            <option value="DISMISSED">DISMISSED</option>
          </select>

          <select
            value={riskFilter}
            onChange={(e) => setRiskFilter(e.target.value)}
            className="text-xs rounded-lg border border-slate-300 px-2.5 py-1.5 bg-white text-slate-700"
          >
            <option value="">All Risk Levels</option>
            <option value="CRITICAL">CRITICAL (76-100)</option>
            <option value="HIGH">HIGH (51-75)</option>
            <option value="MEDIUM">MEDIUM (26-50)</option>
            <option value="LOW">LOW (0-25)</option>
          </select>

          <select
            value={candidateTypeFilter}
            onChange={(e) => setCandidateTypeFilter(e.target.value)}
            className="text-xs rounded-lg border border-slate-300 px-2.5 py-1.5 bg-white text-slate-700"
          >
            <option value="">All Attack Vectors</option>
            <option value="SOCIAL">Social Media</option>
            <option value="APP">Mobile App Stores</option>
          </select>
        </div>
      </div>

      {/* Detections Table */}
      <div className="bg-white rounded-xl border border-slate-200/90 overflow-hidden shadow-xs">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50/75 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
              <tr>
                <th className="px-4 py-3">Threat ID</th>
                <th className="px-4 py-3">Suspected Candidate</th>
                <th className="px-4 py-3">Target Brand</th>
                <th className="px-4 py-3">Detection Category</th>
                <th className="px-4 py-3">Vector</th>
                <th className="px-4 py-3">Risk Assessment</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3 text-right">Investigation</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredDetections.length > 0 ? (
                filteredDetections.map((d) => (
                  <tr key={d.id} className="hover:bg-slate-50/60 transition-colors">
                    <td className="px-4 py-3 font-mono text-slate-400 text-[11px]">#{d.id}</td>
                    <td className="px-4 py-3 font-bold text-slate-900">
                      {d.candidateName || 'Unnamed Candidate'}
                    </td>
                    <td className="px-4 py-3 text-slate-700 font-medium">{d.brandName}</td>
                    <td className="px-4 py-3">
                      <span className="text-[11px] font-medium text-slate-600">
                        {d.detectionType.replace(/_/g, ' ')}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <span className="px-2 py-0.5 rounded bg-slate-100 text-slate-600 font-medium text-[10px]">
                        {d.candidateType}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <RiskBadge level={d.riskLevel} score={d.riskScore} />
                    </td>
                    <td className="px-4 py-3">
                      <StatusBadge status={d.status} />
                    </td>
                    <td className="px-4 py-3 text-right">
                      <Link
                        to={`/investigations/${d.id}`}
                        className="inline-flex items-center gap-1 text-blue-600 hover:text-blue-700 font-bold"
                      >
                        Open Case <ExternalLink className="w-3 h-3" />
                      </Link>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={8} className="px-4 py-8 text-center text-slate-400">
                    No detections match the selected filters.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
