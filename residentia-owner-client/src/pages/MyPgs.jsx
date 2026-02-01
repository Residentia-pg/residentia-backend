import React, { useEffect, useState } from 'react';
import { Plus, Home, MapPin, IndianRupee } from 'lucide-react';
import { Link } from 'react-router-dom';
import api from '../api';

const MyPgs = () => {
    const [pgs, setPgs] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPgs();
    }, []);

    const fetchPgs = async () => {
        try {
            const response = await api.get('/pgs/owner/test@example.com'); // TODO: Use logged in user email
            setPgs(response.data);
        } catch (error) {
            console.error('Failed to fetch PGs', error);
            // Mock data for demo if backend fails
            setPgs([
                { id: 1, name: 'Comfort Stay PG', location: 'Koramangala, Bangalore', price: 8000, type: 'Sharing' },
                { id: 2, name: 'Luxury Living', location: 'Indiranagar, Bangalore', price: 12000, type: 'Single' },
            ]);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-bold text-gray-800">My Properties</h2>
                <Link
                    to="/add-pg"
                    className="bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center space-x-2 hover:bg-blue-700"
                >
                    <Plus size={20} />
                    <span>Add New PG</span>
                </Link>
            </div>

            {pgs.length === 0 ? (
                <div className="text-center py-12 bg-white rounded-xl border border-gray-100">
                    <Home className="mx-auto text-gray-300 mb-4" size={48} />
                    <h3 className="text-lg font-medium text-gray-900">No Properties Found</h3>
                    <p className="text-gray-500 mt-2">Start by adding your first PG accommodation.</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {pgs.map(pg => (
                        <div key={pg.id} className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-md transition-shadow">
                            <div className="h-48 bg-gray-200 w-full relative">
                                <img src={pg.image || "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80"} alt={pg.name} className="w-full h-full object-cover" />
                                <span className="absolute top-4 right-4 bg-white px-3 py-1 rounded-full text-xs font-semibold text-blue-600 shadow-sm">
                                    {pg.type || 'Shared'}
                                </span>
                            </div>
                            <div className="p-5">
                                <h3 className="text-lg font-semibold text-gray-800 mb-2">{pg.name}</h3>
                                <div className="flex items-center text-gray-500 mb-2 text-sm">
                                    <MapPin size={16} className="mr-1" />
                                    {pg.location}
                                </div>
                                <div className="flex items-center text-blue-600 font-bold mb-4">
                                    <IndianRupee size={16} className="mr-1" />
                                    {pg.price}/month
                                </div>
                                <button className="w-full py-2 px-4 border border-blue-600 text-blue-600 rounded-lg hover:bg-blue-50 transition-colors">
                                    View Details
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyPgs;
