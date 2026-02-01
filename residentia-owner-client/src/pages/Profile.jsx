import React, { useEffect, useState } from 'react';
import api from '../api';
import { User, Mail, MapPin, Phone, Briefcase } from 'lucide-react';

const Profile = () => {
    const [profile, setProfile] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({});

    useEffect(() => {
        fetchProfile();
    }, []);

    const fetchProfile = async () => {
        try {
            const response = await api.get('/owners/profile');
            setProfile(response.data);
            setFormData(response.data);
        } catch (error) {
            console.error(error);
            // Mock
            const mock = {
                name: 'Test Owner',
                email: 'test@example.com',
                mobileNumber: '9988776655',
                address: '123 Main St, Bangalore',
                businessName: 'Sunshine PGs'
            };
            setProfile(mock);
            setFormData(mock);
        }
    };

    const handleSave = async () => {
        try {
            const response = await api.put('/owners/profile', formData);
            setProfile(response.data);
            setIsEditing(false);
        } catch (error) {
            console.error(error);
        }
    };

    if (!profile) return <div>Loading...</div>;

    return (
        <div className="max-w-3xl mx-auto bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div className="bg-blue-600 h-32 relative">
                <div className="absolute -bottom-10 left-8">
                    <div className="w-24 h-24 bg-white rounded-full p-1 shadow-md flex items-center justify-center">
                        <User size={48} className="text-gray-400" />
                    </div>
                </div>
            </div>

            <div className="pt-16 pb-8 px-8">
                <div className="flex justify-between items-start mb-6">
                    <div>
                        <h1 className="text-2xl font-bold text-gray-800">{profile.name}</h1>
                        <p className="text-gray-500">{profile.email}</p>
                    </div>
                    <button
                        onClick={() => isEditing ? handleSave() : setIsEditing(true)}
                        className={`px-4 py-2 rounded-lg font-medium transition-colors ${isEditing ? 'bg-green-600 text-white hover:bg-green-700' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
                    >
                        {isEditing ? 'Save Changes' : 'Edit Profile'}
                    </button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div className="space-y-4">
                        <div className="flex items-center space-x-3 text-gray-600">
                            <Mail size={20} className="text-blue-500" />
                            <span>{profile.email}</span>
                        </div>
                        <div className="flex items-center space-x-3 text-gray-600">
                            <Phone size={20} className="text-blue-500" />
                            {isEditing ? (
                                <input
                                    className="border rounded p-1"
                                    value={formData.mobileNumber}
                                    onChange={e => setFormData({ ...formData, mobileNumber: e.target.value })}
                                />
                            ) : (
                                <span>{profile.mobileNumber}</span>
                            )}
                        </div>
                    </div>
                    <div className="space-y-4">
                        <div className="flex items-center space-x-3 text-gray-600">
                            <Briefcase size={20} className="text-blue-500" />
                            {isEditing ? (
                                <input
                                    className="border rounded p-1"
                                    value={formData.businessName}
                                    onChange={e => setFormData({ ...formData, businessName: e.target.value })}
                                />
                            ) : (
                                <span>{profile.businessName}</span>
                            )}
                        </div>
                        <div className="flex items-center space-x-3 text-gray-600">
                            <MapPin size={20} className="text-blue-500" />
                            {isEditing ? (
                                <input
                                    className="border rounded p-1"
                                    value={formData.address}
                                    onChange={e => setFormData({ ...formData, address: e.target.value })}
                                />
                            ) : (
                                <span>{profile.address || 'No address provided'}</span>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;
