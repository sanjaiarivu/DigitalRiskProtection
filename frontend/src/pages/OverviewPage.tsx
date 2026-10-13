import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { dashboardApi } from '../api/dashboard';
import { demoApi } from '../api/demo';
import PageHeader from '../components/layout/PageHeader';
import StatCard from '../components/shared/StatCard';
import ThreatPulseCard from '../components/shared/ThreatPulseCard';
import RiskBadge from '../components/shared/RiskBadge';
import StatusBadge from '../components/shared/StatusBadge';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import {
  Building2,
  Users,
  ShieldAlert,
  AlertTriangle,
  Play,
  RotateCcw,
  ExternalLink,
  ChevronRight
} from 'lucide-react';
import { ResponsiveContainer, PieChart, Pie, Cell, Tooltip, BarChart, Bar, XAxis, YAxis } from 'recharts';
import { Link } from 'react-router-dom';

const RISK_COLORS: Record<string, string> = {
  LOW: '#10b981',
  MEDIUM: '#f59e0b',
  HIGH: '#f97316',
  CRITICAL: '#ef4444'
};

export default function OverviewPage() {
  const queryClient = useQueryClient();

  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['dashboard-summary'],
    queryFn: dashboardApi.getSummary,
  });

  const seedMutation = useMutation({
    mutationFn: demoApi.seed,
    onSuccess: () => {
      queryClient.invalidateQueries();
    }
  });

  const clearMutation = useMutation({
    mutationFn: demoApi.clear,
    onSuccess: () => {
      queryClient.invalidateQueries();
    }
  });

  if (isLoading) return <LoadingSpinner message="Calculating real-time threat metrics..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;
  if (!data) return null;

  const pieData = Object.entries(data.riskDistribution || {}).map(([key, value]) => ({
    name: key,
    value: Number(value),
  }));

  const typeData = Object.entries(data.detectionsByType || {}).map(([key, value]) => ({
    name: key === 'SOCIAL' ? 'Social Impersonation' : 'App Stores',
    count: Number(value),
  }));

  return (
    <div className="space-y-6">
      <PageHeader
        title="Threat Overview"
        description="Real-time algorithmic monitoring of brand impersonation across social platforms and application marketplaces."
        actions={
          <div className="flex items-center gap-2">
            <button
              onClick={() => seedMutation.mutate()}
              disabled={seedMutation.isPending}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold shadow-xs disabled:opacity-50"
            >
              <Play className="w-3.5 h-3.5 fill-current" />
              {seedMutation.isPending ? 'Seeding...' : 'Load Demo Data'}
            </button>
            <button
              onClick={() => clearMutation.mutate()}
              disabled={clearMutation.isPending}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-white hover:bg-slate-50 border border-slate-200 text-slate-700 text-xs font-semibold disabled:opacity-50"
            >
              <RotateCcw className="w-3.5 h-3.5" />
              Reset
            </button>
          </div>
        }
      />

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Monitored Brands"
          value={data.totalBrands}
          subtitle="Active profiles"
          icon={Building2}
          color="blue"
        />
        <StatCard
          title="Candidates Analyzed"
          value={data.totalCandidates}
          subtitle="Profiles & Apps ingested"
          icon={Users}
          color="slate"
        />
        <StatCard
          title="Active Detections"
          value={data.newDetections}
          subtitle="Requiring investigation"
          icon={ShieldAlert}
          color="amber"
        />
        <StatCard
          title="Critical Threats"
          value={data.criticalDetections}
          subtitle="High confidence impersonation"
          icon={AlertTriangle}
          color="rose"
        />
      </div>

      <ThreatPulseCard data={data} />

      {/* Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Risk Distribution */}
        <div className="bg-white rounded-xl border border-slate-200/80 p-5 shadow-xs">
          <h2 className="text-sm font-bold text-slate-900 mb-1">Risk Severity Distribution</h2>
          <p className="text-xs text-slate-500 mb-4">Calculated by weighted explainable scoring engine</p>
          <div className="h-56">
            {pieData.length > 0 && pieData.some(d => d.value > 0) ? (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={pieData}
                    cx="50%"
                    cy="50%"
                    innerRadius={55}
                    outerRadius={80}
                    paddingAngle={4}
                    dataKey="value"
                  >
                    {pieData.map((entry) => (
                      <Cell key={entry.name} fill={RISK_COLORS[entry.name] || '#64748b'} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <div className="flex h-full items-center justify-center text-xs text-slate-400">
                No threat detections recorded yet.
              </div>
            )}
          </div>
          <div className="flex justify-center gap-4 mt-2">
            {Object.keys(RISK_COLORS).map((k) => (
              <div key={k} className="flex items-center gap-1.5 text-xs text-slate-600">
                <span className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: RISK_COLORS[k] }}></span>
                <span>{k}</span>
              </div>
            ))}
          </div>
        </div>

        {/* Vector Distribution */}
        <div className="bg-white rounded-xl border border-slate-200/80 p-5 shadow-xs">
          <h2 className="text-sm font-bold text-slate-900 mb-1">Attack Surface Vectors</h2>
          <p className="text-xs text-slate-500 mb-4">Social media profiles vs mobile app store listings</p>
          <div className="h-56">
            {typeData.length > 0 && typeData.some(d => d.count > 0) ? (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={typeData} margin={{ top: 20, right: 20, bottom: 20, left: 0 }}>
                  <XAxis dataKey="name" tick={{ fontSize: 11 }} />
                  <YAxis allowDecimals={false} tick={{ fontSize: 11 }} />
                  <Tooltip />
                  <Bar dataKey="count" fill="#2563eb" radius={[6, 6, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <div className="flex h-full items-center justify-center text-xs text-slate-400">
                No threat detections recorded yet.
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Recent Detections Table */}
      <div className="bg-white rounded-xl border border-slate-200/80 overflow-hidden shadow-xs">
        <div className="p-4 border-b border-slate-200 flex items-center justify-between">
          <div>
            <h2 className="text-sm font-bold text-slate-900">Recent Threat Detections</h2>
            <p className="text-xs text-slate-500">Latest flagged candidates across all monitored vectors</p>
          </div>
          <Link
            to="/investigations"
            className="text-xs font-semibold text-blue-600 hover:text-blue-700 flex items-center gap-1"
          >
            View all <ChevronRight className="w-3.5 h-3.5" />
          </Link>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50/75 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
              <tr>
                <th className="px-4 py-3">Candidate</th>
                <th className="px-4 py-3">Target Brand</th>
                <th className="px-4 py-3">Vector</th>
                <th className="px-4 py-3">Risk Level</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {data.recentDetections && data.recentDetections.length > 0 ? (
                data.recentDetections.map((d) => (
                  <tr key={d.id} className="hover:bg-slate-50/60 transition-colors">
                    <td className="px-4 py-3 font-semibold text-slate-900">
                      {d.candidateName || 'Unknown candidate'}
                    </td>
                    <td className="px-4 py-3 text-slate-600">{d.brandName}</td>
                    <td className="px-4 py-3">
                      <span className="px-2 py-0.5 rounded bg-slate-100 text-slate-600 font-medium">
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
                        className="inline-flex items-center gap-1 text-blue-600 hover:text-blue-700 font-semibold"
                      >
                        Investigate <ExternalLink className="w-3 h-3" />
                      </Link>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={6} className="px-4 py-8 text-center text-slate-400">
                    No detections generated yet. Click "Load Demo Data" above to initialize synthetic assets and run detection.
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
