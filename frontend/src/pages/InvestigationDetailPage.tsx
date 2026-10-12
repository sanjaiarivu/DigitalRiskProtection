import React, { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { detectionsApi } from '../api/detections';
import PageHeader from '../components/layout/PageHeader';
import RiskBadge from '../components/shared/RiskBadge';
import StatusBadge from '../components/shared/StatusBadge';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import {
  ArrowLeft,
  ShieldAlert,
  Sparkles,
  MessageSquare,
  History,
  CheckCircle2,
  ExternalLink,
  Shield,
  HelpCircle,
  AlertTriangle
} from 'lucide-react';
import type { DetectionStatus } from '../types/api';

export default function InvestigationDetailPage() {
  const { id } = useParams<{ id: string }>();
  const detectionId = Number(id);
  const queryClient = useQueryClient();

  const [noteText, setNoteText] = useState('');
  const [authorName, setAuthorName] = useState('Analyst');
  const [newStatus, setNewStatus] = useState<DetectionStatus>('UNDER_INVESTIGATION');
  const [statusReason, setStatusReason] = useState('');

  const { data: detection, isLoading, error, refetch } = useQuery({
    queryKey: ['detection', detectionId],
    queryFn: () => detectionsApi.get(detectionId),
    enabled: !isNaN(detectionId),
  });

  const explanationMutation = useMutation({
    mutationFn: () => detectionsApi.getExplanation(detectionId),
    onSuccess: (res) => {
      queryClient.invalidateQueries({ queryKey: ['detection', detectionId] });
    },
    onError: (err: any) => {
      alert(`AI explanation failed: ${err.message}`);
    }
  });

  const updateStatusMutation = useMutation({
    mutationFn: (data: { status: DetectionStatus; reason?: string }) =>
      detectionsApi.updateStatus(detectionId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['detection', detectionId] });
      setStatusReason('');
    },
  });

  const addNoteMutation = useMutation({
    mutationFn: (data: { noteText: string; author?: string }) =>
      detectionsApi.addNote(detectionId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['detection', detectionId] });
      setNoteText('');
    },
  });

  if (isLoading) return <LoadingSpinner message="Loading full investigation case record..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;
  if (!detection) return null;

  return (
    <div className="space-y-6">
      <Link
        to="/investigations"
        className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-500 hover:text-slate-800"
      >
        <ArrowLeft className="w-3.5 h-3.5" /> Back to All Investigations
      </Link>

      <PageHeader
        title={`Investigation Case #${detection.id}: ${detection.candidateName || 'Unknown Candidate'}`}
        description={detection.summary || 'Algorithmic threat assessment and digital risk profile'}
        badge={
          <div className="flex items-center gap-2">
            <RiskBadge level={detection.riskLevel} score={detection.riskScore} />
            <StatusBadge status={detection.status} />
          </div>
        }
        actions={
          <button
            onClick={() => explanationMutation.mutate()}
            disabled={explanationMutation.isPending}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white text-xs font-semibold shadow-xs disabled:opacity-50"
          >
            <Sparkles className="w-3.5 h-3.5" />
            {explanationMutation.isPending ? 'Consulting AI...' : 'Generate AI Threat Analysis'}
          </button>
        }
      />

      {/* Grid: Overview and Target Info */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs">
          <p className="text-[11px] font-semibold uppercase text-slate-400">Target Protected Brand</p>
          <h3 className="text-base font-bold text-slate-900 mt-1">{detection.brandName}</h3>
          <p className="text-xs text-slate-500 mt-1">Brand ID #{detection.brandId}</p>
        </div>

        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs">
          <p className="text-[11px] font-semibold uppercase text-slate-400">Attack Surface Vector</p>
          <h3 className="text-base font-bold text-slate-900 mt-1">
            {detection.candidateType} ({detection.candidatePlatform || 'Unknown'})
          </h3>
          <p className="text-xs text-slate-500 mt-1 font-mono">Category: {detection.detectionType}</p>
        </div>

        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs">
          <p className="text-[11px] font-semibold uppercase text-slate-400">Official Whitelist Status</p>
          <h3 className="text-base font-bold mt-1">
            {detection.officialAsset ? (
              <span className="text-emerald-600 flex items-center gap-1">
                <CheckCircle2 className="w-4 h-4" /> Official Brand Asset
              </span>
            ) : (
              <span className="text-amber-600 flex items-center gap-1">
                <AlertTriangle className="w-4 h-4" /> Unregistered Candidate
              </span>
            )}
          </h3>
          <p className="text-xs text-slate-500 mt-1">
            {detection.officialAsset ? 'Risk automatically set to 0' : 'Evaluated by detection engine'}
          </p>
        </div>
      </div>

      {/* AI Explanation Box */}
      {(detection.aiExplanation || explanationMutation.data) && (
        <div className="bg-gradient-to-br from-indigo-50/70 via-white to-blue-50/70 rounded-2xl border border-indigo-200/80 p-6 shadow-xs">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2">
              <div className="p-2 bg-indigo-600 rounded-lg text-white">
                <Sparkles className="w-4 h-4" />
              </div>
              <div>
                <h3 className="text-sm font-bold text-slate-900">AI Threat Intelligence Summary</h3>
                <p className="text-[11px] text-slate-500">
                  Grounded strictly in observable algorithmic evidence signals
                </p>
              </div>
            </div>
            {explanationMutation.data && (
              <span className="text-[10px] font-semibold px-2 py-0.5 rounded bg-indigo-100 text-indigo-700">
                Model: {explanationMutation.data.modelUsed}
              </span>
            )}
          </div>

          <div className="text-xs text-slate-700 space-y-3 leading-relaxed">
            <p className="font-medium bg-white/70 p-3 rounded-lg border border-indigo-100">
              {explanationMutation.data?.summary || detection.aiExplanation}
            </p>

            {explanationMutation.data && (
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-3 pt-3 border-t border-indigo-100">
                {explanationMutation.data.reasonsForConcern?.length > 0 && (
                  <div>
                    <h4 className="font-bold text-rose-800 text-xs mb-1">Key Reasons for Concern:</h4>
                    <ul className="list-disc list-inside space-y-1 text-slate-600 text-[11px]">
                      {explanationMutation.data.reasonsForConcern.map((r, i) => (
                        <li key={i}>{r}</li>
                      ))}
                    </ul>
                  </div>
                )}

                {explanationMutation.data.recommendedNextSteps?.length > 0 && (
                  <div>
                    <h4 className="font-bold text-blue-800 text-xs mb-1">Recommended Analyst Action:</h4>
                    <ul className="list-disc list-inside space-y-1 text-slate-600 text-[11px]">
                      {explanationMutation.data.recommendedNextSteps.map((s, i) => (
                        <li key={i}>{s}</li>
                      ))}
                    </ul>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      )}

      {/* Evidence Breakdown */}
      <div className="bg-white rounded-xl border border-slate-200/90 overflow-hidden shadow-xs">
        <div className="p-4 border-b border-slate-200">
          <h2 className="text-sm font-bold text-slate-900">Explainable Detection Evidence Signals</h2>
          <p className="text-xs text-slate-500">
            Weighted contributions computed by the deterministic threat scoring engine. Every point has a traceable reason.
          </p>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50/75 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
              <tr>
                <th className="px-4 py-3">Signal Name</th>
                <th className="px-4 py-3">Category</th>
                <th className="px-4 py-3">Observed Value</th>
                <th className="px-4 py-3">Expected / Benchmark</th>
                <th className="px-4 py-3">Evidence Explanation</th>
                <th className="px-4 py-3 text-right">Score Contribution</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {detection.evidence && detection.evidence.length > 0 ? (
                detection.evidence.map((e) => (
                  <tr key={e.id} className="hover:bg-slate-50/60">
                    <td className="px-4 py-3 font-semibold text-slate-900">{e.signalName}</td>
                    <td className="px-4 py-3">
                      <span className="px-2 py-0.5 rounded bg-slate-100 text-slate-700 font-mono text-[10px]">
                        {e.signalCategory}
                      </span>
                    </td>
                    <td className="px-4 py-3 font-mono text-slate-800 max-w-xs truncate">{e.observedValue || '—'}</td>
                    <td className="px-4 py-3 font-mono text-slate-500 max-w-xs truncate">{e.expectedValue || '—'}</td>
                    <td className="px-4 py-3 text-slate-600">{e.evidenceExplanation}</td>
                    <td className="px-4 py-3 text-right font-bold text-rose-600">+{e.scoreContribution} pts</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={6} className="px-4 py-6 text-center text-slate-400">
                    No individual threat signals detected.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Case Management: Status Update & Analyst Notes */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Update Status */}
        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs">
          <h3 className="text-sm font-bold text-slate-900 mb-1">Update Investigation Status</h3>
          <p className="text-xs text-slate-500 mb-4">
            Mark confirmed phishing/impersonation, flag as legitimate, or dismiss.
          </p>

          <form
            onSubmit={(e) => {
              e.preventDefault();
              updateStatusMutation.mutate({ status: newStatus, reason: statusReason });
            }}
            className="space-y-3"
          >
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">New Status</label>
              <select
                value={newStatus}
                onChange={(e) => setNewStatus(e.target.value as DetectionStatus)}
                className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2 bg-white"
              >
                <option value="UNDER_INVESTIGATION">UNDER INVESTIGATION (Case Active)</option>
                <option value="LEGITIMATE">LEGITIMATE (Verified benign)</option>
                <option value="DISMISSED">DISMISSED (False positive / noise)</option>
                <option value="NEW">NEW (Pending review)</option>
              </select>
            </div>
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1">
                Reason / Action Taken
              </label>
              <textarea
                rows={2}
                placeholder="Document verification findings, takedown requests sent, etc."
                value={statusReason}
                onChange={(e) => setStatusReason(e.target.value)}
                className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2"
              />
            </div>
            <button
              type="submit"
              disabled={updateStatusMutation.isPending}
              className="px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold disabled:opacity-50"
            >
              {updateStatusMutation.isPending ? 'Updating...' : 'Commit Status Change'}
            </button>
          </form>

          {/* Audit History Timeline */}
          {detection.history && detection.history.length > 0 && (
            <div className="mt-5 pt-4 border-t border-slate-100">
              <h4 className="text-xs font-bold text-slate-700 flex items-center gap-1.5 mb-2">
                <History className="w-3.5 h-3.5" /> Audit History
              </h4>
              <div className="space-y-2">
                {detection.history.map((h) => (
                  <div key={h.id} className="text-[11px] p-2 rounded bg-slate-50 border border-slate-100">
                    <span className="font-semibold text-slate-700">{h.changedBy}</span> changed status to{' '}
                    <span className="font-bold text-blue-600">{h.newStatus}</span>
                    {h.changeReason && <p className="text-slate-500 mt-0.5 italic">"{h.changeReason}"</p>}
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Analyst Notes */}
        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs flex flex-col justify-between">
          <div>
            <h3 className="text-sm font-bold text-slate-900 mb-1">Investigation Notes</h3>
            <p className="text-xs text-slate-500 mb-4">Collaborative SOC notebook and audit trail.</p>

            <div className="space-y-2.5 max-h-56 overflow-y-auto mb-4 pr-1">
              {detection.notes && detection.notes.length > 0 ? (
                detection.notes.map((n) => (
                  <div key={n.id} className="p-3 rounded-lg bg-slate-50 border border-slate-100 text-xs">
                    <div className="flex items-center justify-between text-[10px] text-slate-400 mb-1">
                      <span className="font-semibold text-slate-700">{n.author}</span>
                      <span>{new Date(n.createdAt).toLocaleString()}</span>
                    </div>
                    <p className="text-slate-700">{n.noteText}</p>
                  </div>
                ))
              ) : (
                <div className="text-xs text-slate-400 p-4 text-center">No notes recorded yet.</div>
              )}
            </div>
          </div>

          <form
            onSubmit={(e) => {
              e.preventDefault();
              if (!noteText.trim()) return;
              addNoteMutation.mutate({ noteText: noteText.trim(), author: authorName });
            }}
            className="space-y-2 pt-3 border-t border-slate-100"
          >
            <div className="flex gap-2">
              <input
                type="text"
                placeholder="Author name"
                value={authorName}
                onChange={(e) => setAuthorName(e.target.value)}
                className="w-1/3 text-xs rounded-lg border border-slate-300 px-3 py-1.5"
              />
              <input
                type="text"
                required
                placeholder="Write an investigation note..."
                value={noteText}
                onChange={(e) => setNoteText(e.target.value)}
                className="flex-1 text-xs rounded-lg border border-slate-300 px-3 py-1.5"
              />
            </div>
            <div className="flex justify-end">
              <button
                type="submit"
                disabled={addNoteMutation.isPending}
                className="px-3 py-1.5 rounded-lg bg-slate-900 hover:bg-slate-800 text-white text-xs font-semibold disabled:opacity-50 flex items-center gap-1"
              >
                <MessageSquare className="w-3.5 h-3.5" />
                {addNoteMutation.isPending ? 'Posting...' : 'Add Note'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
