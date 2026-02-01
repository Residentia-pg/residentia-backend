import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import { Save } from 'lucide-react';

const AddPg = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        location: '',
        price: '',
        type: 'Sharing', // Sharing, Single
        description: '',
        amenities: '',
        ownerEmail: 'test@example.com' // Mock
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/pgs', formData);
            navigate('/my-pgs');
        } catch (error) {
            console.error('Failed to add PG', error);
        }
    };

    return (
        <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-sm border border-gray-100 p-8">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Add New Property</h2>

            <form onSubmit={handleSubmit} className="space-y-6">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">PG Name</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        className="w-full rounded-lg border-gray-300 border p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                        required
                        placeholder="e.g. Sunshine Residency"
                    />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Location</label>
                        <input
                            type="text"
                            name="location"
                            value={formData.location}
                            onChange={handleChange}
                            className="w-full rounded-lg border-gray-300 border p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                            required
                            placeholder="Area, City"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Price (per month)</label>
                        <input
                            type="number"
                            name="price"
                            value={formData.price}
                            onChange={handleChange}
                            className="w-full rounded-lg border-gray-300 border p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                            required
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Room Type</label>
                    <select
                        name="type"
                        value={formData.type}
                        onChange={handleChange}
                        className="w-full rounded-lg border-gray-300 border p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                    >
                        <option value="Sharing">Sharing</option>
                        <option value="Single">Single Room</option>
                        <option value="Double">Double Sharing</option>
                        <option value="Triple">Triple Sharing</option>
                    </select>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Amenities (comma separated)</label>
                    <input
                        type="text"
                        name="amenities"
                        value={formData.amenities}
                        onChange={handleChange}
                        className="w-full rounded-lg border-gray-300 border p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                        placeholder="WiFi, AC, Food, Laundry"
                    />
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        rows="4"
                        className="w-full rounded-lg border-gray-300 border p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
                    ></textarea>
                </div>

                <div className="flex justify-end pt-4">
                    <button
                        type="button"
                        className="mr-4 px-6 py-2 text-gray-600 font-medium hover:bg-gray-100 rounded-lg transition-colors"
                        onClick={() => navigate('/my-pgs')}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className="px-6 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors flex items-center shadow-md hover:shadow-lg"
                    >
                        <Save size={18} className="mr-2" />
                        Save Property
                    </button>
                </div>
            </form>
        </div>
    );
};

export default AddPg;
