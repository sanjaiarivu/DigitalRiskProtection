import React, { useState } from 'react';
import { ArrowUpRight, Radar } from 'lucide-react';
import { Link } from 'react-router-dom';
import type { DashboardSummaryResponse } from '../../types/api';

interface ThreatPulseCardProps {
  data: DashboardSummaryResponse;
}

export default function ThreatPulseCard({ data }: ThreatPulseCardProps) {
  const [tilt, setTilt] = useState({ x: 0, y: 0 });
  const social = Number(data.detectionsByType?.SOCIAL || 0);
  const apps = Number(data.detectionsByType?.APP || 0);
  const maxSurface = Math.max(social, apps, 1);

  const handlePointerMove = (event: React.PointerEvent<HTMLElement>) => {
    if (event.pointerType !== 'mouse' || window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;
    const bounds = event.currentTarget.getBoundingClientRect();
    const x = (event.clientX - bounds.left) / bounds.width - 0.5;
    const y = (event.clientY - bounds.top) / bounds.height - 0.5;
    setTilt({ x: y * -3, y: x * 4 });
  };

  return (
    <section
      className="threat-pulse-card rounded-2xl border border-slate-200 bg-gradient-to-br from-white via-white to-blue-50/80 p-5 shadow-sm"
      onPointerMove={handlePointerMove}
      onPointerLeave={() => setTilt({ x: 0, y: 0 })}
      style={{ transform: `perspective(1100px) rotateX(${tilt.x}deg) rotateY(${tilt.y}deg)` }}
      aria-labelledby="threat-pulse-title"
    >
      <div className="grid gap-6 md:grid-cols-[1.1fr_1fr] md:items-center">
        <div>
          <div className="flex items-center gap-2 text-blue-700">
            <span className="rounded-lg bg-blue-100 p-2"><Radar className="h-4 w-4" aria-hidden="true" /></span>
            <p className="text-[11px] font-bold uppercase tracking-[0.16em]">Exposure pulse</p>
          </div>
          <h2 id="threat-pulse-title" className="mt-3 text-lg font-bold tracking-tight text-slate-900">Current threat surface</h2>
          <p className="mt-1 text-xs leading-5 text-slate-500">A snapshot of detections recorded in this workspace.</p>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div className="rounded-xl border border-rose-100 bg-rose-50/80 p-3">
            <p className="text-[10px] font-semibold uppercase tracking-wider text-rose-700">Critical</p>
            <p className="mt-1 text-2xl font-bold tabular-nums text-rose-900">{data.criticalDetections}</p>
          </div>
          <div className="rounded-xl border border-amber-100 bg-amber-50/80 p-3">
            <p className="text-[10px] font-semibold uppercase tracking-wider text-amber-700">Needs review</p>
            <p className="mt-1 text-2xl font-bold tabular-nums text-amber-900">{data.newDetections}</p>
          </div>
        </div>

        <div className="min-w-44 space-y-3">
          <p className="text-[10px] font-bold uppercase tracking-wider text-slate-500">Detection surface</p>
          {[['Social', social, 'bg-blue-500'], ['Apps', apps, 'bg-violet-500']].map(([label, value, color]) => (
            <div key={label}>
              <div className="mb-1 flex justify-between text-[11px] text-slate-600"><span>{label}</span><span className="font-semibold tabular-nums">{value}</span></div>
              <div className="h-1.5 overflow-hidden rounded-full bg-slate-100" role="img" aria-label={`${label}: ${value} detections`}>
                <div className={`h-full rounded-full ${color}`} style={{ width: `${Math.max(value === 0 ? 0 : 8, (Number(value) / maxSurface) * 100)}%` }} />
              </div>
            </div>
          ))}
        </div>

        <Link to="/investigations" className="inline-flex min-h-11 items-center justify-center gap-2 rounded-lg bg-slate-900 px-4 py-2 text-xs font-semibold text-white transition-colors hover:bg-slate-700 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600 md:col-start-2 md:row-start-2">
          Review cases <ArrowUpRight className="h-3.5 w-3.5" aria-hidden="true" />
        </Link>
      </div>
    </section>
  );
}
