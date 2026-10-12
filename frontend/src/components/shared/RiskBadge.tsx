import React from 'react';
import type { RiskLevel } from '../../types/api';

interface RiskBadgeProps {
  level: RiskLevel;
  score?: number;
}

export default function RiskBadge({ level, score }: RiskBadgeProps) {
  let style = 'bg-slate-50 text-slate-700 border-slate-200';
  if (level === 'LOW') style = 'bg-emerald-50 text-emerald-700 border-emerald-200';
  if (level === 'MEDIUM') style = 'bg-amber-50 text-amber-700 border-amber-200';
  if (level === 'HIGH') style = 'bg-orange-50 text-orange-700 border-orange-200';
  if (level === 'CRITICAL') style = 'bg-rose-50 text-rose-700 border-rose-200';

  return (
    <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded text-[11px] font-semibold border ${style}`}>
      <span className="w-1.5 h-1.5 rounded-full bg-current"></span>
      {level}
      {score !== undefined && <span className="opacity-75 font-mono">({score})</span>}
    </span>
  );
}
