import { useState, useEffect } from 'react';
import { Plus, Search, Book, GraduationCap, Building, Trash2 } from 'lucide-react';
import api from '../services/api';
import Toast from '../components/Toast';
import LoadingSpinner from '../components/LoadingSpinner';

const Courses = () => {
  const [courses, setCourses] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [toast, setToast] = useState(null);
  const [loading, setLoading] = useState(true);
  const [currentCourse, setCurrentCourse] = useState({ title: '', credits: 3, deptId: '' });

  useEffect(() => {
    fetchCourses();
    fetchDepartments();
  }, []);

  const fetchCourses = async () => {
    try {
      const res = await api.get('/courses');
      setCourses(res.data.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching courses:", error);
      setToast({ message: 'Failed to fetch courses', type: 'error' });
    }
  };

  const fetchDepartments = async () => {
    try {
      const res = await api.get('/departments');
      setDepartments(res.data.data);
    } catch (error) {
      console.error("Error fetching departments:", error);
    }
  };

  const handleSearch = async () => {
    if (searchTerm.trim() === '') {
      fetchCourses();
      return;
    }
    try {
      const res = await api.get(`/courses/search?q=${searchTerm}`);
      setCourses(res.data.data);
    } catch (error) {
      console.error("Error searching courses:", error);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (currentCourse.courseId) {
        await api.put(`/courses/${currentCourse.courseId}`, currentCourse);
        setToast({ message: 'Course updated successfully', type: 'success' });
      } else {
        await api.post('/courses', currentCourse);
        setToast({ message: 'Course created successfully', type: 'success' });
      }
      fetchCourses();
      setShowModal(false);
    } catch (error) {
      console.error("Error saving course:", error);
      setToast({ message: 'Failed to save course', type: 'error' });
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Course Catalog</h1>
        <button onClick={() => { setCurrentCourse({ title: '', credits: 3, deptId: '' }); setShowModal(true); }} className="bg-emerald-600 hover:bg-emerald-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 shadow-lg shadow-emerald-900/10">
          <Plus size={18} /> Create Course
        </button>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-slate-100 p-4 flex items-center gap-4">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
          <input 
            type="text" 
            placeholder="Search courses..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            className="w-full pl-10 pr-4 py-2 bg-white border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 transition-all"
          />
        </div>
        <button onClick={handleSearch} className="px-4 py-2 bg-emerald-600 text-white rounded-lg hover:bg-emerald-700">Search</button>
        <button onClick={() => { setSearchTerm(''); fetchCourses(); }} className="px-4 py-2 bg-slate-200 text-slate-700 rounded-lg hover:bg-slate-300">Clear</button>
      </div>

      {loading ? (
        <LoadingSpinner />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {courses.map((course) => (
          <div key={course.courseId} className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 hover:shadow-md hover:border-emerald-200 transition-all group overflow-hidden relative">
            <div className="absolute top-0 right-0 p-4 opacity-10 group-hover:opacity-20 transition-opacity">
              <Book size={64} className="text-emerald-900" />
            </div>
            <div className="flex items-start justify-between relative z-10">
              <div className="w-12 h-12 bg-emerald-50 rounded-xl flex items-center justify-center text-emerald-600">
                <GraduationCap size={24} />
              </div>
              <span className="text-xs font-bold bg-slate-100 text-slate-500 px-3 py-1 rounded-full uppercase">{course.credits} Credits</span>
            </div>
            <h3 className="mt-4 text-xl font-bold text-slate-800">{course.title}</h3>
            <div className="mt-2 flex items-center gap-2 text-slate-400 text-sm">
                <Building size={14} />
                <span>Dept ID: {course.deptId}</span>
            </div>
            <div className="mt-6 flex gap-3">
              <button onClick={() => { setCurrentCourse(course); setShowModal(true); }} className="flex-1 py-2 bg-slate-50 hover:bg-slate-100 text-slate-600 rounded-lg text-sm font-semibold transition-colors">Edit Details</button>
              <button onClick={async () => { if(confirm("Delete?")) { await api.delete(`/courses/${course.courseId}`); fetchCourses(); }}} className="px-4 py-2 text-red-500 hover:bg-red-50 rounded-lg transition-colors"><Trash2 size={18} /></button>
            </div>
          </div>
        ))}
        </div>
      )}

      {courses.length === 0 && !loading && (
        <div className="flex flex-col items-center justify-center p-12 text-slate-400 text-center">
          <Book size={48} className="mb-4 opacity-20" />
          <p>No courses found. Create your first course!</p>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/60 flex items-center justify-center p-4 z-50 overflow-hidden backdrop-blur-sm">
            <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden animate-in fade-in zoom-in duration-200">
                <div className="px-8 py-6 bg-slate-50 border-b border-slate-100 flex items-center justify-between">
                    <h3 className="text-xl font-bold text-slate-800">Course Information</h3>
                    <button onClick={() => setShowModal(false)} className="text-slate-400 hover:text-slate-800">✕</button>
                </div>
                <form onSubmit={handleSave} className="p-8 space-y-6">
                    <div>
                        <label className="block text-sm font-semibold text-slate-700 mb-2">Course Title</label>
                        <input required type="text" className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500/20" value={currentCourse.title} onChange={(e) => setCurrentCourse({...currentCourse, title: e.target.value})} />
                    </div>
                    <div className="grid grid-cols-2 gap-6">
                        <div>
                            <label className="block text-sm font-semibold text-slate-700 mb-2">Credits</label>
                            <input required type="number" className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl" value={currentCourse.credits} onChange={(e) => setCurrentCourse({...currentCourse, credits: parseInt(e.target.value)})} />
                        </div>
                        <div>
                            <label className="block text-sm font-semibold text-slate-700 mb-2">Department</label>
                            <select required className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl" value={currentCourse.deptId} onChange={(e) => setCurrentCourse({...currentCourse, deptId: parseInt(e.target.value)})}>
                                <option value="">Select Dept</option>
                                {departments.map(d => <option key={d.deptId} value={d.deptId}>{d.name}</option>)}
                            </select>
                        </div>
                    </div>
                    <button type="submit" className="w-full py-4 bg-emerald-600 text-white font-bold rounded-2xl shadow-lg shadow-emerald-900/20 hover:bg-emerald-700 transition-all active:scale-[0.98]">Confirm and Save</button>
                </form>
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

export default Courses;
