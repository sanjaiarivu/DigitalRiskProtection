import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { socialApi } from '../api/social';
import PageHeader from '../components/layout/PageHeader';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import { Users, Radar, ExternalLink, CheckCircle } from 'lucide-react';

export default function SocialMonitoringPage() {
  const queryClient = useQueryClient();

  const { data: candidates, isLoading, error, refetch } = useQuery({
    queryKey: ['social-candidates'],
    queryFn: socialApi.getCandidates,
  });

  const scanMutation = useMutation({
    mutationFn: socialApi.scan,
    onSuccess: (result) => {
      alert(`Social scan complete!\nScanned: ${result.candidatesScanned}\nDetections created: ${result.detectionsCreated}\nOfficial assets whitelisted/excluded: ${result.officialAssetsExcluded}`);
      queryClient.invalidateQueries();
    },
    onError: (err: any) => {
      alert(`Scan failed: ${err.message}`);
    }
  });

  if (isLoading) return <LoadingSpinner message="Scanning social media candidate feeds..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;

  return (
    <div className="space-y-6">
      <PageHeader
        title="Social Media Monitoring"
        description="Monitor, analyze, and detect profile impersonations across major social platforms."
        actions={
          <button
            onClick={() => scanMutation.mutate()}
            disabled={scanMutation.isPending}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold shadow-xs disabled:opacity-50"
          >
            <Radar className={`w-3.5 h-3.5 ${scanMutation.isPending ? 'animate-spin' : ''}`} />
            {scanMutation.isPending ? 'Running Engine...' : 'Run Social Scan'}
          </button>
        }
      />

      <div className="bg-white rounded-xl border border-slate-200/90 overflow-hidden shadow-xs">
        <div className="p-4 border-b border-slate-200 flex items-center justify-between">
          <div>
            <h2 className="text-sm font-bold text-slate-900">Ingested Social Profiles</h2>
            <p className="text-xs text-slate-500">Live feeds and candidate accounts analyzed by BrandShield</p>
          </div>
          <span className="text-xs font-semibold text-slate-600 bg-slate-100 px-2.5 py-1 rounded-full">
            {candidates?.length || 0} Profiles Loaded
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50/75 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
              <tr>
                <th className="px-4 py-3">Platform</th>
                <th className="px-4 py-3">Display Name</th>
                <th className="px-4 py-3">Username / Handle</th>
                <th className="px-4 py-3">Account ID</th>
                <th className="px-4 py-3">Description / Bio</th>
                <th className="px-4 py-3">Dataset Source</th>
                <th className="px-4 py-3 text-right">Profile Link</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {candidates && candidates.length > 0 ? (
                candidates.map((c) => (
                  <tr key={c.id} className="hover:bg-slate-50/60">
                    <td className="px-4 py-3">
                      <span className="px-2 py-0.5 rounded bg-blue-50 text-blue-700 font-semibold text-[10px] border border-blue-200">
                        {c.platform}
                      </span>
                    </td>
                    <td className="px-4 py-3 font-semibold text-slate-900">{c.displayName || '—'}</td>
                    <td className="px-4 py-3 font-mono text-slate-700">@{c.username || '—'}</td>
                    <td className="px-4 py-3 font-mono text-slate-500 text-[11px]">{c.accountId || '—'}</td>
                    <td className="px-4 py-3 text-slate-600 max-w-xs truncate">{c.description || '—'}</td>
                    <td className="px-4 py-3">
                      {c.synthetic ? (
                        <span className="px-2 py-0.5 rounded bg-purple-50 text-purple-700 border border-purple-200 font-medium text-[10px]">
                          SYNTHETIC (DEMO)
                        </span>
                      ) : (
                        <span className="px-2 py-0.5 rounded bg-slate-100 text-slate-600 font-medium text-[10px]">
                          {c.source}
                        </span>
                      )}
                    </td>
                    <td className="px-4 py-3 text-right">
                      {c.profileUrl ? (
                        <a
                          href={c.profileUrl}
                          target="_blank"
                          rel="noreferrer"
                          className="inline-flex items-center gap-1 text-blue-600 hover:text-blue-700 font-medium"
                        >
                          Visit <ExternalLink className="w-3 h-3" />
                        </a>
                      ) : (
                        <span className="text-slate-400">—</span>
                      )}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={7} className="px-4 py-8 text-center text-slate-400">
                    No social media candidates loaded. Use "Load Demo Data" on the Overview page or import external profiles.
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
