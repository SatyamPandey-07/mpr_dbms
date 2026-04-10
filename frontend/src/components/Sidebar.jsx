import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Users, UserCog, BookOpen, Building2 } from 'lucide-react';

const Sidebar = () => {
  const navItems = [
    { name: 'Dashboard', icon: <LayoutDashboard size={20} />, path: '/' },
    { name: 'Students', icon: <Users size={20} />, path: '/students' },
    { name: 'Instructors', icon: <UserCog size={20} />, path: '/instructors' },
    { name: 'Courses', icon: <BookOpen size={20} />, path: '/courses' },
    { name: 'Departments', icon: <Building2 size={20} />, path: '/departments' },
  ];

  return (
    <div className="w-64 bg-slate-900 text-white flex flex-col shadow-xl">
      <div className="p-8">
        <h1 className="text-2xl font-bold tracking-tight text-primary-400">G-Univ</h1>
        <p className="text-slate-400 text-xs mt-1 uppercase tracking-widest font-semibold font-mono">Management</p>
      </div>
      
      <nav className="flex-1 px-4 py-6 space-y-2">
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-3 rounded-lg transition-all duration-200 ${
                isActive 
                ? 'bg-primary-600 text-white shadow-lg shadow-primary-900/20' 
                : 'text-slate-400 hover:bg-slate-800 hover:text-white'
              }`
            }
          >
            {item.icon}
            <span className="font-medium">{item.name}</span>
          </NavLink>
        ))}
      </nav>

      <div className="p-6 border-t border-slate-800">
        <div className="bg-slate-800 rounded-xl p-4">
          <p className="text-xs text-slate-400 font-medium">SYSTEM STATUS</p>
          <div className="flex items-center gap-2 mt-2">
            <div className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></div>
            <p className="text-sm text-slate-200">Online</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
