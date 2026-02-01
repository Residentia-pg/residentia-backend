import React from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, Home, PlusCircle, User, LogOut } from 'lucide-react';

const Layout = () => {
    const location = useLocation();

    const navItems = [
        { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
        { path: '/my-pgs', label: 'My PGs', icon: Home },
        { path: '/add-pg', label: 'Add PG', icon: PlusCircle },
        { path: '/profile', label: 'Profile', icon: User },
    ];

    return (
        <div className="flex h-screen bg-gray-100">
            {/* Sidebar */}
            <div className="w-64 bg-white shadow-md flex flex-col">
                <div className="p-6">
                    <h1 className="text-2xl font-bold text-blue-600">Residentia<span className="text-xs text-gray-400 block font-normal">Owner Portal</span></h1>
                </div>
                <nav className="flex-1 px-4 space-y-2">
                    {navItems.map((item) => {
                        const Icon = item.icon;
                        const isActive = location.pathname === item.path;
                        return (
                            <Link
                                key={item.path}
                                to={item.path}
                                className={`flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${isActive
                                        ? 'bg-blue-50 text-blue-600'
                                        : 'text-gray-600 hover:bg-gray-50'
                                    }`}
                            >
                                <Icon size={20} />
                                <span className="font-medium">{item.label}</span>
                            </Link>
                        );
                    })}
                </nav>
                <div className="p-4 border-t">
                    <button className="flex items-center space-x-3 text-red-500 w-full px-4 py-2 hover:bg-red-50 rounded-lg">
                        <LogOut size={20} />
                        <span>Logout</span>
                    </button>
                </div>
            </div>

            {/* Main Content */}
            <div className="flex-1 overflow-auto">
                <header className="bg-white shadow-sm p-4 flex justify-between items-center px-8">
                    <h2 className="text-xl font-semibold text-gray-800">
                        {navItems.find(i => i.path === location.pathname)?.label || 'Overview'}
                    </h2>
                    <div className="flex items-center gap-4">
                        <span className="text-sm text-gray-500">Welcome, Owner</span>
                        <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 font-bold">
                            O
                        </div>
                    </div>
                </header>
                <main className="p-8">
                    <Outlet />
                </main>
            </div>
        </div>
    );
};

export default Layout;
