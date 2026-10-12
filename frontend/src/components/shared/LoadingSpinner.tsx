import React from 'react';
import { Loader2 } from 'lucide-react';

export default function LoadingSpinner({ message = 'Loading data...' }: { message?: string }) {
  return (
    <div className="flex flex-col items-center justify-center p-12 text-slate-400 gap-3">
      <Loader2 className="w-7 h-7 animate-spin text-blue-600" />
      <p className="text-xs font-medium text-slate-500">{message}</p>
    </div>
  );
}
