import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { brandsApi } from '../api/brands';
import PageHeader from '../components/layout/PageHeader';
import LoadingSpinner from '../components/shared/LoadingSpinner';
import ErrorState from '../components/shared/ErrorState';
import { Plus, Building2, Globe, ShieldCheck, ChevronRight, Trash2 } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function BrandsPage() {
  const queryClient = useQueryClient();
  const [modalOpen, setModalOpen] = useState(false);
  const [name, setName] = useState('');
  const [website, setWebsite] = useState('');
  const [description, setDescription] = useState('');
  const [aliases, setAliases] = useState('');

  const { data: brands, isLoading, error, refetch } = useQuery({
    queryKey: ['brands'],
    queryFn: brandsApi.list,
  });

  const createMutation = useMutation({
    mutationFn: brandsApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['brands'] });
      setModalOpen(false);
      setName('');
      setWebsite('');
      setDescription('');
      setAliases('');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: brandsApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['brands'] });
    },
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    createMutation.mutate({
      name: name.trim(),
      website: website.trim() || undefined,
      description: description.trim() || undefined,
      aliases: aliases ? aliases.split(',').map((s) => s.trim()).filter(Boolean) : [],
    });
  };

  if (isLoading) return <LoadingSpinner message="Loading brand profiles..." />;
  if (error) return <ErrorState message={(error as Error).message} onRetry={() => refetch()} />;

  return (
    <div className="space-y-6">
      <PageHeader
        title="Brand Profiles"
        description="Register and manage monitored corporate brands, protected trademarks, and official verified assets."
        actions={
          <button
            onClick={() => setModalOpen(true)}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold shadow-xs"
          >
            <Plus className="w-3.5 h-3.5" />
            Add Brand Profile
          </button>
        }
      />

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {brands && brands.length > 0 ? (
          brands.map((brand) => (
            <div
              key={brand.id}
              className="bg-white rounded-xl border border-slate-200/90 p-5 shadow-xs hover:shadow-md transition-shadow flex flex-col justify-between"
            >
              <div>
                <div className="flex items-start justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-xl bg-blue-50 border border-blue-100 flex items-center justify-center text-blue-600 font-bold text-sm">
                      {brand.name.substring(0, 2).toUpperCase()}
                    </div>
                    <div>
                      <h3 className="text-sm font-bold text-slate-900">{brand.name}</h3>
                      {brand.website && (
                        <a
                          href={brand.website}
                          target="_blank"
                          rel="noreferrer"
                          className="text-xs text-blue-600 hover:underline flex items-center gap-1"
                        >
                          <Globe className="w-3 h-3" />
                          {brand.website.replace(/^https?:\/\//, '')}
                        </a>
                      )}
                    </div>
                  </div>
                  <button
                    onClick={() => {
                      if (confirm(`Delete brand profile for ${brand.name}?`)) {
                        deleteMutation.mutate(brand.id);
                      }
                    }}
                    className="p-1.5 text-slate-400 hover:text-rose-600 rounded-lg hover:bg-rose-50 transition-colors"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>

                {brand.description && (
                  <p className="mt-3 text-xs text-slate-600 line-clamp-2">{brand.description}</p>
                )}

                <div className="mt-4 flex flex-wrap gap-1.5">
                  {brand.aliases?.map((alias, idx) => (
                    <span
                      key={idx}
                      className="text-[10px] font-medium bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full"
                    >
                      {alias}
                    </span>
                  ))}
                </div>
              </div>

              <div className="mt-5 pt-3 border-t border-slate-100 flex items-center justify-between text-xs">
                <span className="text-slate-500 flex items-center gap-1.5">
                  <ShieldCheck className="w-3.5 h-3.5 text-emerald-600" />
                  {brand.assetCount || 0} Official Assets
                </span>
                <Link
                  to={`/brands/${brand.id}`}
                  className="font-semibold text-blue-600 hover:text-blue-700 flex items-center gap-1"
                >
                  Manage Assets <ChevronRight className="w-3.5 h-3.5" />
                </Link>
              </div>
            </div>
          ))
        ) : (
          <div className="col-span-full bg-white rounded-xl border border-slate-200/80 p-8 text-center">
            <Building2 className="w-8 h-8 text-slate-400 mx-auto mb-2" />
            <h3 className="text-sm font-bold text-slate-900">No brand profiles registered</h3>
            <p className="text-xs text-slate-500 mt-1 mb-4">
              Add your corporate brand name and official digital assets to start monitoring.
            </p>
            <button
              onClick={() => setModalOpen(true)}
              className="inline-flex items-center gap-1 px-3 py-1.5 rounded-lg bg-blue-600 text-white text-xs font-semibold"
            >
              <Plus className="w-3.5 h-3.5" /> Register Brand
            </button>
          </div>
        )}
      </div>

      {/* Create Modal */}
      {modalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/50 backdrop-blur-xs">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 shadow-xl border border-slate-100">
            <h2 className="text-base font-bold text-slate-900 mb-1">Create Brand Profile</h2>
            <p className="text-xs text-slate-500 mb-4">
              Define the primary name and aliases used to identify look-alike impersonations.
            </p>
            <form onSubmit={handleSubmit} className="space-y-3.5">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Brand Name *</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. TechVault"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Official Website</label>
                <input
                  type="url"
                  placeholder="https://techvault.com"
                  value={website}
                  onChange={(e) => setWebsite(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">
                  Brand Aliases (Comma separated)
                </label>
                <input
                  type="text"
                  placeholder="Tech Vault, TV Cloud, TechVault Secure"
                  value={aliases}
                  onChange={(e) => setAliases(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Description</label>
                <textarea
                  rows={2}
                  placeholder="Enterprise secure cloud storage platform..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  className="w-full text-xs rounded-lg border border-slate-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="pt-2 flex justify-end gap-2">
                <button
                  type="button"
                  onClick={() => setModalOpen(false)}
                  className="px-3 py-1.5 rounded-lg border border-slate-300 text-xs text-slate-700 hover:bg-slate-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={createMutation.isPending}
                  className="px-3 py-1.5 rounded-lg bg-blue-600 hover:bg-blue-700 text-white text-xs font-semibold disabled:opacity-50"
                >
                  {createMutation.isPending ? 'Saving...' : 'Save Brand Profile'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
