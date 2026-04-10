import { useState, useEffect } from 'react';
import { Plus, Building2, MapPin, User, Trash2 } from 'lucide-react';
import api from '../services/api';
import Toast from '../components/Toast';
import LoadingSpinner from '../components/LoadingSpinner';

const Departments = () => {
  const [departments, setDepartments] = useState([]);
  const [instructors, setInstructors] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [toast, setToast] = useState(null);
  const [loading, setLoading] = useState(true);
  const [currentDept, setCurrentDept] = useState({ name: '', office: '', chairId: '' });

  useEffect(() => {
    fetchDepartments();
    fetchInstructors();
  }, []);

  const fetchDepartments = async () => {
    try {
      const res = await api.get('/departments');
      setDepartments(res.data.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching departments:", error);
      setToast({ message: 'Failed to fetch departments', type: 'error' });
    }
  };

  const fetchInstructors = async () => {
    try {
      const res = await api.get('/instructors');
      setInstructors(res.data.data);
    } catch (error) {
      console.error("Error fetching instructors:", error);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (currentDept.deptId) {
        await api.put(`/departments/${currentDept.deptId}`, currentDept);
        setToast({ message: 'Department updated successfully', type: 'success' });
      } else {
        await api.post('/departments', currentDept);
        setToast({ message: 'Department created successfully', type: 'success' });
      }
      fetchDepartments();
      setShowModal(false);
    } catch (error) {
      console.error("Error saving department:", error);
      setToast({ message: 'Failed to save department', type: 'error' });
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Department Management</h1>
        <button onClick={() => { setCurrentDept({ name: '', office: '', chairId: '' }); setShowModal(true); }} className="bg-amber-600 hover:bg-amber-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 shadow-lg shadow-amber-900/10 transition-all active:scale-95">
          <Plus size={18} /> New Department
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {loading ? (
          <div className="col-span-full">
            <LoadingSpinner />
          </div>
        ) : (
          departments.map((dept) => (
            <div key={dept.deptId} className="bg-white p-6 rounded-3xl shadow-sm border border-slate-100 group relative">
              <h3 className="text-xl font-bold text-slate-800 flex items-center gap-2">
                  <Building2 className="text-amber-500" size={24} /> {dept.name}
              </h3>
              <div className="mt-4 space-y-3">
                  <div className="flex items-center gap-3 text-slate-500 text-sm">
                      <MapPin size={16} /> <span>Office: {dept.office}</span>
                  </div>
                  <div className="flex items-center gap-3 text-slate-500 text-sm font-semibold p-2 bg-slate-50 rounded-lg">
                      <User size={16} className="text-amber-600" />
                      <span>Chair: {instructors.find(i => i.instructorId === dept.chairId)?.person?.firstName || 'None'} {instructors.find(i => i.instructorId === dept.chairId)?.person?.lastName || ''}</span>
                  </div>
              </div>
              <div className="mt-6 pt-4 border-t border-slate-50 flex gap-2">
                  <button onClick={() => { setCurrentDept(dept); setShowModal(true); }} className="flex-1 py-2 text-sm font-bold text-amber-700 bg-amber-50 rounded-xl hover:bg-amber-100 transition-colors">Edit</button>
                  <button onClick={async () => { if(confirm("Delete?")) { await api.delete(`/departments/${dept.deptId}`); fetchDepartments(); }}} className="px-4 text-red-500 hover:bg-red-50 rounded-xl transition-colors"><Trash2 size={18} /></button>
              </div>
            </div>
          ))
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 flex items-center justify-center p-4 z-50 backdrop-blur-sm overflow-hidden">
            <div className="bg-white rounded-[2rem] shadow-[0_32px_64px_-12px_rgba(0,0,0,0.14)] w-full max-w-lg animate-in zoom-in-95 duration-200">
                <div className="p-10">
                    <div className="flex items-center justify-between mb-8">
                        <div>
                            <h2 className="text-2xl font-bold text-slate-900">Department Setup</h2>
                            <p className="text-slate-500 text-sm mt-1 uppercase tracking-tight font-medium">Registration and Assignments</p>
                        </div>
                        <div className="w-12 h-12 bg-amber-100 rounded-full flex items-center justify-center text-amber-600">
                            <Building2 size={24} />
                        </div>
                    </div>
                    
                    <form onSubmit={handleSave} className="space-y-6">
                        <div className="space-y-2">
                            <label className="text-xs font-bold text-slate-400 uppercase tracking-widest pl-1">Department Name</label>
                            <input required type="text" className="w-full px-5 py-3 text-lg font-medium bg-slate-50 border-2 border-slate-50 rounded-2xl focus:border-amber-500/50 focus:bg-white outline-none transition-all placeholder:text-slate-300" placeholder="e.g. Physics" value={currentDept.name} onChange={(e) => setCurrentDept({...currentDept, name: e.target.value})} />
                        </div>
                        <div className="space-y-2">
                            <label className="text-xs font-bold text-slate-400 uppercase tracking-widest pl-1">Office Location</label>
                            <input required type="text" className="w-full px-5 py-3 bg-slate-50 border-2 border-slate-50 rounded-2xl focus:border-amber-500/50 focus:bg-white outline-none transition-all" placeholder="e.g. Science Hall 401" value={currentDept.office} onChange={(e) => setCurrentDept({...currentDept, office: e.target.value})} />
                        </div>
                        <div className="space-y-2">
                            <label className="text-xs font-bold text-slate-400 uppercase tracking-widest pl-1">Assigning Chair</label>
                            <select className="w-full px-5 py-3 bg-slate-50 border-2 border-slate-50 rounded-2xl outline-none focus:border-amber-500/50 transition-all appearance-none" value={currentDept.chairId} onChange={(e) => setCurrentDept({...currentDept, chairId: parseInt(e.target.value)})}>
                                <option value="">Select Instructor</option>
                                {instructors.map(inst => <option key={inst.instructorId} value={inst.instructorId}>{inst.person.firstName} {inst.person.lastName}</option>)}
                            </select>
                        </div>
                        <div className="flex gap-4 pt-4">
                            <button type="button" onClick={() => setShowModal(false)} className="flex-1 py-4 text-slate-400 font-bold hover:text-slate-600">Cancel</button>
                            <button type="submit" className="flex-[2] py-4 bg-slate-900 text-white font-bold rounded-2xl shadow-xl shadow-slate-900/20 active:scale-95 transition-all">Create Department</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
      )}

      {toast && (
        <Toast 
          message={toast.message} 
          type={toast.type} 
          onClose={() => setToast(null)} 
        />
      )}
    </div>
  );
};

export default Departments;
