import React, { useEffect, useState } from 'react';
import { Users, Home, Calendar } from 'lucide-react';
import api from '../api';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalPgs: 0,
        totalBookings: 0,
        recentBookings: []
    });

    useEffect(() => {
        // Mock data for now, or fetch from API if ready
        // api.get('/dashboard').then(...)
        setStats({
            totalPgs: 12,
            totalBookings: 45,
            recentBookings: []
        });
    }, []);

    const StatCard = ({ title, value, icon: Icon, color }) => (
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex items-center space-x-4">
            <div className={`p-3 rounded-lg ${color}`}>
                <Icon className="text-white" size={24} />
            </div>
            <div>
                <p className="text-gray-500 text-sm">{title}</p>
                <h3 className="text-2xl font-bold text-gray-800">{value}</h3>
            </div>
        </div>
    );

    return (
        <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <StatCard
                    title="Total Properties"
                    value={stats.totalPgs}
                    icon={Home}
                    color="bg-blue-500"
                />
                <StatCard
                    title="Total Bookings"
                    value={stats.totalBookings}
                    icon={Calendar}
                    color="bg-green-500"
                />
                <StatCard
                    title="Active Users"
                    value="Waitlist"
                    icon={Users}
                    color="bg-purple-500"
                />
            </div>

            <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                <h3 className="text-lg font-semibold mb-4">Recent Activity</h3>
                <p className="text-gray-500">No recent bookings found.</p>
            </div>
        </div>
    );
};

export default Dashboard;
