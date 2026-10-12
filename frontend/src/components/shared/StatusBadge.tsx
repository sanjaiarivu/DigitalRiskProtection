import React from 'react';
import type { DetectionStatus } from '../../types/api';

interface StatusBadgeProps {
  status: DetectionStatus;
}

export default function StatusBadge({ status }: StatusBadgeProps) {
  switch (status) {
    case 'NEW':
      return (
        <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-blue-50 text-blue-700 border border-blue-200">
          NEW
        </span>
      );
    case 'UNDER_INVESTIGATION':
      return (
        <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-amber-50 text-amber-700 border border-amber-200">
          UNDER INVESTIGATION
        </span>
      );
    case 'LEGITIMATE':
      return (
        <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
          LEGITIMATE
        </span>
      );
    case 'DISMISSED':
      return (
        <span className="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-semibold bg-slate-100 text-slate-600 border border-slate-200">
          DISMISSED
        </span>
      );
    default:
      return <span className="text-xs text-slate-500">{status}</span>;
  }
}
