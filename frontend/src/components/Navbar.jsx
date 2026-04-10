import { Bell, User } from 'lucide-react';

const Navbar = () => {
  return (
    <nav className="bg-white border-b border-slate-200 px-6 py-4 flex items-center justify-between shadow-sm">
      <div className="flex items-center gap-4">
        <h2 className="text-xl font-bold text-slate-800">University Admin</h2>
      </div>
      <div className="flex items-center gap-6">
        <button className="text-slate-500 hover:text-primary-600 transition-colors">
          <Bell size={20} />
        </button>
        <div className="flex items-center gap-3 border-l pl-6 border-slate-200">
          <div className="text-right">
            <p className="text-sm font-semibold text-slate-700 leading-none">Admin User</p>
            <p className="text-xs text-slate-400 mt-1">Super Admin</p>
          </div>
          <div className="w-10 h-10 bg-primary-100 rounded-full flex items-center justify-center text-primary-700 font-bold">
            <User size={20} />
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
