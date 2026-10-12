import React, { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { brandsApi } from '../api/brands';
import PageHeader from '../components/layout/PageHeader';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import { Plus, ArrowLeft, ShieldCheck, Globe, Trash2, CheckCircle2 } from 'lucide-react';
import type { AssetType } from '../types/api';

export default function BrandDetailPage() {
  const { id } = useParams<{ id: string }>();
  const brandId = Number(id);
  const queryClient = useQueryClient();

  const [assetModal, setAssetModal] = useState(false);
  const [assetType, setAssetType] = useState<AssetType>('SOCIAL_ACCOUNT');
  const [platform, setPlatform] = useState('');
  const [identifier, setIdentifier] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [canonicalUrl, setCanonicalUrl] = useState('');

  const { data: brand, isLoading, error, refetch } = useQuery({
    queryKey: ['brand', brandId],
    queryFn: () => brandsApi.get(brandId),
    enabled: !isNaN(brandId),
  });

  const addAssetMutation = useMutation({
    mutationFn: (data: any) => brandsApi.addAsset(brandId, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['brand', brandId] });
      setAssetModal(false);
      setIdentifier('');
      setPlatform('');
      setDisplayName('');
      setCanonicalUrl('');
    },
  });

  const deleteAssetMutation = useMutation({
    mutationFn: (assetId: number) => brandsApi.deleteAsset(brandId, assetId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['brand', brandId] });
    },
  });

  const handleAddAsset = (e: React.FormEvent) => {
    e.preventDefault();
    if (!identifier.trim()) return;
    addAssetMutation.mutate({
      assetType,
      platform: platform.trim() || undefined,
      identifier: identifier.trim(),
      displayName: displayName.trim() || undefined,
      canonicalUrl: canonicalUrl.trim() || undefined,
    });
  };

  if (isLoading) return <LoadingSpinner message="Loading brand details..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;
  if (!brand) return null;

  return (
    <div className="space-y-6">
      <Link
        to="/brands"
        className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-500 hover:text-slate-800"
      >
        <ArrowLeft className="w-3.5 h-3.5" /> Back to Brand Profiles
      </Link>

      <PageHeader
        title={brand.name}
        description={brand.description || 'Monitored brand profile'}
        badge={
          <span className="px-2 py-0.5 rounded-full bg-blue-50 text-blue-700 border border-blue-200 text-xs font-semibold">
            ID: {brand.id}
          </span>
        }
        actions={
          <button
            onClick={() => setAssetModal(true)}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold shadow-xs"
          >
            <Plus className="w-3.5 h-3.5" />
            Register Official Asset
          </button>
        }
      />

      {/* Brand Metadata Box */}
      <div className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <p className="text-[11px] font-semibold uppercase text-slate-400">Official Website</p>
          <p className="text-xs font-medium text-slate-800 mt-1 flex items-center gap-1">
            <Globe className="w-3.5 h-3.5 text-slate-500" />
            {brand.website || 'Not specified'}
          </p>
        </div>
        <div>
          <p className="text-[11px] font-semibold uppercase text-slate-400">Registered Aliases</p>
          <div className="mt-1 flex flex-wrap gap-1">
            {brand.aliases?.length ? (
              brand.aliases.map((a, i) => (
                <span key={i} className="px-2 py-0.5 rounded bg-slate-100 text-[10px] text-slate-700 font-medium">
                  {a}
                </span>
              ))
            ) : (
              <span className="text-xs text-slate-400">None</span>
            )}
          </div>
        </div>
        <div>
          <p className="text-[11px] font-semibold uppercase text-slate-400">Verification Status</p>
          <p className="text-xs font-semibold text-emerald-600 mt-1 flex items-center gap-1">
            <CheckCircle2 className="w-3.5 h-3.5" /> Protected Identity
          </p>
        </div>
      </div>

      {/* Assets Section */}
      <div className="bg-white rounded-xl border border-slate-200/90 overflow-hidden shadow-xs">
        <div className="p-4 border-b border-slate-200">
          <h2 className="text-sm font-bold text-slate-900">Official Registered Assets</h2>
          <p className="text-xs text-slate-500">
            Exact identifier whitelist. The detection engine automatically excludes these assets from being flagged as impersonators.
          </p>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-50/75 border-b border-slate-200 text-slate-500 font-semibold uppercase tracking-wider">
              <tr>
                <th className="px-4 py-3">Asset Type</th>
                <th className="px-4 py-3">Platform</th>
                <th className="px-4 py-3">Official Identifier</th>
                <th className="px-4 py-3">Display Name / Title</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {brand.assets && brand.assets.length > 0 ? (
                brand.assets.map((asset) => (
                  <tr key={asset.id} className="hover:bg-slate-50/60">
                    <td className="px-4 py-3 font-semibold text-slate-900">{asset.assetType}</td>
                    <td className="px-4 py-3 text-slate-600">{asset.platform || 'N/A'}</td>
                    <td className="px-4 py-3 font-mono text-slate-800 font-semibold">{asset.identifier}</td>
                    <td className="px-4 py-3 text-slate-600">{asset.displayName || '—'}</td>
                    <td className="px-4 py-3">
                      <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded text-[10px] font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
                        <ShieldCheck className="w-3 h-3" /> VERIFIED
                      </span>
                    </td>
                    <td className="px-4 py-3 text-right">
                      <button
                        onClick={() => {
                          if (confirm('Remove this official registered asset?')) {
                            deleteAssetMutation.mutate(asset.id);
                          }
                        }}
                        className="p-1 text-slate-400 hover:text-rose-600 rounded hover:bg-rose-50"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={6} className="px-4 py-6 text-center text-slate-400">
                    No official assets registered. Click "Register Official Asset" to add whitelisted accounts or mobile app packages.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Asset Modal */}
      {assetModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-xs">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl border border-slate-100">
            <h2 className="text-base font-bold text-slate-900 mb-1">Register Official Brand Asset</h2>
            <p className="text-xs text-slate-500 mb-4">
              Registered assets are strictly whitelisted and never flagged as threats.
            </p>
            <form onSubmit={handleAddAsset} className="space-y-3">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Asset Type *</label>
                <select
                  value={assetType}
                  onChange={(e) => setAssetType(e.target.value as AssetType)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="SOCIAL_ACCOUNT">Official Social Account</option>
                  <option value="MOBILE_APP">Official Mobile Application</option>
                  <option value="DOMAIN">Authorized Web Domain</option>
                </select>
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Platform / Marketplace
                </label>
                <input
                  type="text"
                  placeholder="e.g. Twitter, Google Play, Apple App Store"
                  value={platform}
                  onChange={(e) => setPlatform(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Unique Identifier *
                </label>
                <input
                  type="text"
                  required
                  placeholder={
                    assetType === 'MOBILE_APP'
                      ? 'e.g. com.techvault.app'
                      : 'e.g. @techvault or account ID'
                  }
                  value={identifier}
                  onChange={(e) => setIdentifier(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Publisher / Display Name
                </label>
                <input
                  type="text"
                  placeholder="e.g. TechVault Inc"
                  value={displayName}
                  onChange={(e) => setDisplayName(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Canonical URL
                </label>
                <input
                  type="url"
                  placeholder="https://twitter.com/techvault"
                  value={canonicalUrl}
                  onChange={(e) => setCanonicalUrl(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2"
                />
              </div>
              <div className="pt-2 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setAssetModal(false)}
                  className="px-3 py-1.5 rounded-lg border border-slate-300 text-xs text-slate-700 hover:bg-slate-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={addAssetMutation.isPending}
                  className="px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold disabled:opacity-50"
                >
                  {addAssetMutation.isPending ? 'Saving...' : 'Add Verified Asset'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
