import React from 'react';
import PageHeader from '../components/layout/PageHeader';
import { Settings, Shield, Cpu, Database, CheckCircle2 } from 'lucide-react';

export default function SettingsPage() {
  return (
    <div className="space-y-6">
      <PageHeader
        title="System Configuration & Architecture"
        description="Review active risk scoring weights, AI provider integration, and environment settings."
      />

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Engine Parameters */}
        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs">
          <div className="flex items-center gap-2 mb-3">
            <Cpu className="w-4 h-4 text-blue-600" />
            <h3 className="text-sm font-bold text-slate-900">Detection Engine Weights</h3>
          </div>
          <p className="text-xs text-slate-500 mb-4">
            Category multipliers applied to raw signal contributions (capped at 100).
          </p>
          <div className="space-y-2 text-xs">
            <div className="flex justify-between py-1.5 border-b border-slate-100">
              <span className="text-slate-600">IDENTIFIER_CONFLICT</span>
              <span className="font-mono font-bold text-blue-600">1.5x</span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-100">
              <span className="text-slate-600">PUBLISHER_MISMATCH</span>
              <span className="font-mono font-bold text-blue-600">1.3x</span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-100">
              <span className="text-slate-600">CHARACTER_SUBSTITUTION</span>
              <span className="font-mono font-bold text-blue-600">1.2x</span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-100">
              <span className="text-slate-600">SUSPICIOUS_LINK</span>
              <span className="font-mono font-bold text-blue-600">1.1x</span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-100">
              <span className="text-slate-600">NAME_SIMILARITY</span>
              <span className="font-mono font-bold text-blue-600">1.0x</span>
            </div>
            <div className="flex justify-between py-1.5 border-b border-slate-100">
              <span className="text-slate-600">DESCRIPTION_SIMILARITY</span>
              <span className="font-mono font-bold text-blue-600">0.8x</span>
            </div>
          </div>
        </div>

        {/* AI & Infrastructure */}
        <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs">
          <div className="flex items-center gap-2 mb-3">
            <Shield className="w-4 h-4 text-emerald-600" />
            <h3 className="text-sm font-bold text-slate-900">AI Intelligence & Fallback</h3>
          </div>
          <p className="text-xs text-slate-500 mb-4">
            Dual-mode threat analysis: OpenAI integration with deterministic rule-grounded fallback.
          </p>
          <div className="space-y-3 text-xs text-slate-600">
            <div className="p-3 rounded-lg bg-slate-50 border border-slate-100">
              <p className="font-semibold text-slate-800">Primary Provider: OpenAI (gpt-4o-mini)</p>
              <p className="text-[11px] text-slate-500 mt-0.5">
                Enabled when OPENAI_API_KEY is configured in backend environment.
              </p>
            </div>
            <div className="p-3 rounded-lg bg-emerald-50 border border-emerald-100">
              <p className="font-semibold text-emerald-900 flex items-center gap-1">
                <CheckCircle2 className="w-3.5 h-3.5 text-emerald-600" /> Deterministic Fallback Engine
              </p>
              <p className="text-[11px] text-emerald-700 mt-0.5">
                Always active. Automatically synthesizes evidence-based reports when API key is omitted.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
