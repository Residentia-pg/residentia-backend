import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';

// Placeholder components for now
const MyPgs = () => <div>My PGs Page</div>;
const AddPg = () => <div>Add PG Page</div>;
const Profile = () => <div>Profile Page</div>;
const Login = () => <div>Login Page</div>;

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<Layout />}>
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="my-pgs" element={<MyPgs />} />
        <Route path="add-pg" element={<AddPg />} />
        <Route path="profile" element={<Profile />} />
      </Route>
    </Routes>
  );
}

export default App;
