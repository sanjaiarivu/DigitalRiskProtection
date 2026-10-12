import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  Building2,
  Users,
  Smartphone,
  ShieldAlert,
  Settings,
  Shield,
  X
} from 'lucide-react';

interface SidebarProps {
  onClose?: () => void;
}

const navItems = [
  { name: 'Overview', to: '/', icon: LayoutDashboard },
  { name: 'Brand Profiles', to: '/brands', icon: Building2 },
  { name: 'Social Monitoring', to: '/social', icon: Users },
  { name: 'App Monitoring', to: '/apps', icon: Smartphone },
  { name: 'Threat Investigations', to: '/investigations', icon: ShieldAlert },
  { name: 'Settings', to: '/settings', icon: Settings },
];

export default function Sidebar({ onClose }: SidebarProps) {
  return (
    <div className="flex h-full flex-col bg-slate-900 text-slate-300">
      {/* Brand Header */}
      <div className="flex items-center justify-between px-6 py-5 border-b border-slate-800">
        <div className="flex items-center gap-3">
          <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-tr from-blue-600 to-cyan-500 text-white shadow-lg shadow-blue-500/25">
            <Shield className="h-5 w-5" />
          </div>
          <div>
            <h1 className="text-sm font-bold tracking-tight text-white flex items-center gap-1.5">
              BrandShield <span className="text-xs px-1.5 py-0.5 rounded bg-blue-500/20 text-cyan-300 font-semibold uppercase">AI</span>
            </h1>
            <p className="text-[10px] text-slate-400 font-medium">Digital Risk Protection</p>
          </div>
        </div>
        {onClose && (
          <button onClick={onClose} className="p-1 rounded text-slate-400 hover:text-white md:hidden">
            <X className="w-5 h-5" />
          </button>
        )}
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1.5 px-3 py-6">
        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={onClose}
              className={({ isActive }) =>
                `group flex items-center gap-3 rounded-lg px-3.5 py-2.5 text-xs font-semibold transition-all duration-150 ${
                  isActive
                    ? 'bg-blue-600/15 text-blue-400 border-l-2 border-blue-500 pl-3'
                    : 'text-slate-400 hover:bg-slate-800/60 hover:text-slate-200'
                }`
              }
            >
              <Icon className="h-4 w-4 shrink-0" />
              <span>{item.name}</span>
            </NavLink>
          );
        })}
      </nav>

      {/* Version badge */}
      <div className="p-4 border-t border-slate-800">
        <div className="rounded-lg bg-slate-800/60 p-3 border border-slate-700/50">
          <div className="flex items-center justify-between text-[11px] text-slate-400 mb-1">
            <span>Detection Engine</span>
            <span className="font-semibold text-cyan-400">v1.2.0</span>
          </div>
          <p className="text-[10px] text-slate-400">Explainable Algorithmic Risk Assessment</p>
        </div>
      </div>
    </div>
  );
}
