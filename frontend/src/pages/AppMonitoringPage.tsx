import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { appsApi } from '../api/apps';
import PageHeader from '../components/layout/PageHeader';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import { Smartphone, Radar, ExternalLink, Plus } from 'lucide-react';

export default function AppMonitoringPage() {
  const queryClient = useQueryClient();
  const [showAdd, setShowAdd] = useState(false);
  const [candidate, setCandidate] = useState({ appName: '', packageId: '', bundleId: '', publisherName: '', publisherId: '', storePlatform: '', storeUrl: '', description: '', iconUrl: '', externalLinks: '', downloadCount: '', rating: '' });

  const { data: candidates, isLoading, error, refetch } = useQuery({
    queryKey: ['app-candidates'],
    queryFn: appsApi.getCandidates,
  });

  const scanMutation = useMutation({
    mutationFn: appsApi.scan,
    onSuccess: (result) => {
      alert(`Mobile App scan complete!\nScanned: ${result.candidatesScanned}\nDetections created: ${result.detectionsCreated}\nOfficial assets whitelisted/excluded: ${result.officialAssetsExcluded}`);
      queryClient.invalidateQueries();
    },
    onError: (err: any) => {
      alert(`Scan failed: ${err.message}`);
    }
  });

  const addMutation = useMutation({
    mutationFn: () => appsApi.addCandidate({ ...candidate, rating: candidate.rating ? Number(candidate.rating) : null }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['app-candidates'] }); setShowAdd(false); setCandidate({ appName: '', packageId: '', bundleId: '', publisherName: '', publisherId: '', storePlatform: '', storeUrl: '', description: '', iconUrl: '', externalLinks: '', downloadCount: '', rating: '' }); },
    onError: (err: Error) => alert(`Could not add app listing: ${err.message}`),
  });

  if (isLoading) return <LoadingSpinner message="Scanning app marketplace candidate listings..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;

  return (
    <div className="space-y-6">
      <PageHeader
        title="Mobile App Monitoring"
        description="Review imported app listings for impersonation signals. Add observed listings here; automatic store discovery is not connected."
        actions={
          <div className="flex gap-2"><button onClick={() => setShowAdd(v => !v)} className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-300 bg-white text-slate-700 text-xs font-semibold"><Plus className="w-3.5 h-3.5" /> Add Listing</button><button
            onClick={() => scanMutation.mutate()}
            disabled={scanMutation.isPending}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold shadow-xs disabled:opacity-50"
          >
            <Radar className={`w-3.5 h-3.5 ${scanMutation.isPending ? 'animate-spin' : ''}`} />
            {scanMutation.isPending ? 'Running Engine...' : 'Run App Scan'}
          </button></div>
        }
      />

      {showAdd && <form onSubmit={e => { e.preventDefault(); addMutation.mutate(); }} className="bg-white rounded-xl border border-slate-200 p-4 grid grid-cols-1 md:grid-cols-3 gap-3">
        {([['appName','App name *'],['packageId','Package ID'],['bundleId','Bundle ID'],['publisherName','Publisher'],['publisherId','Publisher ID'],['storePlatform','Store'],['storeUrl','Store listing URL'],['iconUrl','App icon URL'],['downloadCount','Downloads'],['rating','Rating']] as const).map(([key,label]) => <label key={key} className="text-xs font-semibold text-slate-600">{label}<input required={key === 'appName'} type={key === 'rating' ? 'number' : key.endsWith('Url') ? 'url' : 'text'} step={key === 'rating' ? '0.1' : undefined} min={key === 'rating' ? '0' : undefined} max={key === 'rating' ? '5' : undefined} value={candidate[key]} onChange={e => setCandidate({ ...candidate, [key]: e.target.value })} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 font-normal" /></label>)}
        <label className="text-xs font-semibold text-slate-600 md:col-span-3">Description<textarea value={candidate.description} onChange={e => setCandidate({ ...candidate, description: e.target.value })} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 font-normal" /></label>
        <label className="text-xs font-semibold text-slate-600 md:col-span-3">External links<input value={candidate.externalLinks} onChange={e => setCandidate({ ...candidate, externalLinks: e.target.value })} className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 font-normal" /></label>
        <div className="md:col-span-3 flex justify-end gap-2"><button type="button" onClick={() => setShowAdd(false)} className="px-3 py-2 rounded-lg border text-xs">Cancel</button><button disabled={addMutation.isPending} className="px-3 py-2 rounded-lg bg-blue-600 text-white text-xs font-semibold">{addMutation.isPending ? 'Saving…' : 'Save listing'}</button></div>
      </form>}

      <div className="bg-white rounded-xl border border-slate-200/90 overflow-hidden shadow-xs">
        <div className="p-4 border-b border-slate-200 flex items-center justify-between">
          <div>
            <h2 className="text-sm font-bold text-slate-900">Ingested Mobile App Listings</h2>
            <p className="text-xs text-slate-500">Imported store metadata, package IDs, and publishers analyzed</p>
          </div>
          <span className="text-xs font-semibold text-slate-600 bg-slate-100 px-2.5 py-1 rounded-full">
            {candidates?.length || 0} Apps Loaded
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50/75 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
              <tr>
                <th className="px-4 py-3">Store Platform</th>
                <th className="px-4 py-3">App Name</th>
                <th className="px-4 py-3">Package / Bundle ID</th>
                <th className="px-4 py-3">Publisher Name</th>
                <th className="px-4 py-3">Downloads / Rating</th>
                <th className="px-4 py-3">Dataset Source</th>
                <th className="px-4 py-3 text-right">Listing Link</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {candidates && candidates.length > 0 ? (
                candidates.map((app) => (
                  <tr key={app.id} className="hover:bg-slate-50/60">
                    <td className="px-4 py-3">
                      <span className="px-2 py-0.5 rounded bg-slate-100 text-slate-700 font-semibold text-[10px]">
                        {app.storePlatform || 'App Store'}
                      </span>
                    </td>
                    <td className="px-4 py-3 font-semibold text-slate-900">{app.appName}</td>
                    <td className="px-4 py-3 font-mono text-slate-700 text-[11px]">{app.packageId || '—'}</td>
                    <td className="px-4 py-3 text-slate-700">{app.publisherName || '—'}</td>
                    <td className="px-4 py-3 text-slate-500 text-[11px]">
                      {app.downloadCount ? `${app.downloadCount} dl` : ''} {app.rating ? `(${app.rating} ★)` : ''}
                    </td>
                    <td className="px-4 py-3">
                      {app.synthetic ? (
                        <span className="px-2 py-0.5 rounded bg-purple-50 text-purple-700 border border-purple-200 font-medium text-[10px]">
                          SYNTHETIC (DEMO)
                        </span>
                      ) : (
                        <span className="px-2 py-0.5 rounded bg-slate-100 text-slate-600 font-medium text-[10px]">
                          {app.source}
                        </span>
                      )}
                    </td>
                    <td className="px-4 py-3 text-right">
                      {app.storeUrl ? (
                        <a
                          href={app.storeUrl}
                          target="_blank"
                          rel="noreferrer"
                          className="inline-flex items-center gap-1 text-blue-600 hover:text-blue-700 font-medium"
                        >
                          Store <ExternalLink className="w-3 h-3" />
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
                    No app candidates loaded. Use "Load Demo Data" on the Overview page to initialize synthetic apps.
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
