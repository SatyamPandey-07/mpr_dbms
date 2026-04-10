import { useState, useEffect } from 'react';
import { Users, UserCog, BookOpen, Building2, TrendingUp, Clock } from 'lucide-react';
import api from '../services/api';

const Dashboard = () => {
  const [stats, setStats] = useState({
    students: 0,
    instructors: 0,
    courses: 0,
    departments: 0,
  });

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const studentRes = await api.get('/students');
        const instructorRes = await api.get('/instructors');
        const courseRes = await api.get('/courses');
        const deptRes = await api.get('/departments');
        
        setStats({
          students: studentRes.data.data.length,
          instructors: instructorRes.data.data.length,
          courses: courseRes.data.data.length,
          departments: deptRes.data.data.length,
        });
      } catch (error) {
        console.error("Error fetching stats:", error);
      }
    };
    fetchStats();
  }, []);

  const statCards = [
    { label: 'Total Students', value: stats.students, icon: <Users size={24} />, color: 'bg-blue-500' },
    { label: 'Total Instructors', value: stats.instructors, icon: <UserCog size={24} />, color: 'bg-purple-500' },
    { label: 'Courses Offered', value: stats.courses, icon: <BookOpen size={24} />, color: 'bg-emerald-500' },
    { label: 'Departments', value: stats.departments, icon: <Building2 size={24} />, color: 'bg-amber-500' },
  ];

  return (
    <div className="space-y-8">
      <header>
        <h1 className="text-3xl font-bold text-slate-800 tracking-tight">University Overview</h1>
        <p className="text-slate-500 mt-2">Welcome back! Here's what's happening today.</p>
      </header>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {statCards.map((card, i) => (
          <div key={i} className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 flex items-center gap-5 hover:shadow-md transition-shadow">
            <div className={`${card.color} text-white p-4 rounded-xl shadow-lg shadow-${card.color.split('-')[1]}-200`}>
              {card.icon}
            </div>
            <div>
              <p className="text-sm font-medium text-slate-400 uppercase tracking-wider">{card.label}</p>
              <h3 className="text-2xl font-bold text-slate-800 mt-1">{card.value}</h3>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 bg-white rounded-3xl p-8 shadow-sm border border-slate-100">
          <div className="flex items-center justify-between mb-8">
            <h3 className="text-xl font-bold text-slate-800 flex items-center gap-2">
              <TrendingUp className="text-primary-500" /> Recent Activity
            </h3>
            <button className="text-primary-600 text-sm font-semibold hover:underline">View All</button>
          </div>
          <div className="space-y-6">
            {[1, 2, 3].map((_, i) => (
              <div key={i} className="flex gap-4 group">
                <div className="w-12 h-12 bg-slate-50 rounded-full flex items-center justify-center shrink-0 group-hover:bg-primary-50 transition-colors">
                  <Clock size={20} className="text-slate-400 group-hover:text-primary-500" />
                </div>
                <div className="flex-1 pb-6 border-b border-slate-50 last:border-0 last:pb-0">
                  <p className="font-semibold text-slate-800">New student enrollment</p>
                  <p className="text-sm text-slate-500">Alice Smith was added to Computer Science department.</p>
                  <p className="text-xs text-slate-400 mt-2">2 hours ago</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-primary-900 rounded-3xl p-8 text-white relative overflow-hidden shadow-xl shadow-primary-900/40">
          <div className="relative z-10">
            <h3 className="text-xl font-bold mb-4">Quick Links</h3>
            <div className="space-y-4">
              <button className="w-full py-3 px-6 bg-primary-800 hover:bg-primary-700 rounded-xl transition-all font-medium text-left">Assign Advisor</button>
              <button className="w-full py-3 px-6 bg-primary-800 hover:bg-primary-700 rounded-xl transition-all font-medium text-left">Create New Course</button>
              <button className="w-full py-3 px-6 bg-primary-800 hover:bg-primary-700 rounded-xl transition-all font-medium text-left">Generate Reports</button>
            </div>
          </div>
          <div className="absolute -bottom-12 -right-12 w-48 h-48 bg-primary-800/30 rounded-full blur-3xl"></div>
          <div className="absolute top-0 right-0 w-32 h-32 bg-primary-400/10 rounded-full blur-2xl"></div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
