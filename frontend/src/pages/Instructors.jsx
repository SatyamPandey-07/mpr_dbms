import { useState, useEffect } from 'react';
import { Plus, Search, Edit2, Trash2, ShieldCheck, GraduationCap, BookOpen } from 'lucide-react';
import api from '../services/api';
import Toast from '../components/Toast';
import LoadingSpinner from '../components/LoadingSpinner';

const Instructors = () => {
  const [instructors, setInstructors] = useState([]);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showCourseModal, setShowCourseModal] = useState(false);
  const [selectedInstructor, setSelectedInstructor] = useState(null);
  const [toast, setToast] = useState(null);
  const [currentInst, setCurrentInst] = useState({
    person: { ssn: '', firstName: '', lastName: '', email: '', address: '', dateOfBirth: null },
    rank: '',
    salary: 0
  });
  const [courseAssignment, setCourseAssignment] = useState({
    courseId: '',
    semester: '',
    year: new Date().getFullYear()
  });

  useEffect(() => {
    fetchInstructors();
    fetchCourses();
  }, []);

  const fetchInstructors = async () => {
    try {
      const res = await api.get('/instructors');
      setInstructors(res.data.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching instructors:", error);
      setToast({ message: 'Failed to fetch instructors', type: 'error' });
    }
  };

  const fetchCourses = async () => {
    try {
      const res = await api.get('/courses');
      setCourses(res.data.data);
    } catch (error) {
      console.error("Error fetching courses:", error);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (currentInst.instructorId) {
        await api.put(`/instructors/${currentInst.instructorId}`, currentInst);
        setToast({ message: 'Instructor updated successfully', type: 'success' });
      } else {
        await api.post('/instructors', currentInst);
        setToast({ message: 'Instructor added successfully', type: 'success' });
      }
      fetchInstructors();
      setShowModal(false);
    } catch (error) {
      console.error("Error saving instructor:", error);
      setToast({ message: 'Failed to save instructor', type: 'error' });
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure? This will delete the instructor and their assignments.")) {
      try {
        await api.delete(`/instructors/${id}`);
        setToast({ message: 'Instructor deleted successfully', type: 'success' });
        fetchInstructors();
      } catch (error) {
        console.error("Error deleting instructor:", error);
        setToast({ message: 'Failed to delete instructor', type: 'error' });
      }
    }
  };

  const handleAssignCourse = async (e) => {
    e.preventDefault();
    try {
      await api.post(
        `/instructors/${selectedInstructor.instructorId}/courses/${courseAssignment.courseId}`,
        null,
        {
          params: {
            semester: courseAssignment.semester,
            year: courseAssignment.year
          }
        }
      );
      setToast({ message: 'Course assigned successfully', type: 'success' });
      setShowCourseModal(false);
      setCourseAssignment({ courseId: '', semester: '', year: new Date().getFullYear() });
    } catch (error) {
      console.error("Error assigning course:", error);
      setToast({ message: 'Failed to assign course', type: 'error' });
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Instructor Management</h1>
        <button 
          onClick={() => { setCurrentInst({ person: { ssn: '', firstName: '', lastName: '', email: '', address: '', dateOfBirth: null }, rank: '', salary: 0 }); setShowModal(true); }}
          className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 shadow-lg shadow-primary-900/20 transition-all active:scale-95"
        >
          <Plus size={18} /> Add Instructor
        </button>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
        <div className="overflow-x-auto">
          {loading ? (
            <LoadingSpinner />
          ) : (
            <table className="w-full text-left">
            <thead>
              <tr className="bg-slate-50/50 text-slate-500 text-sm font-semibold uppercase tracking-wider">
                <th className="px-6 py-4">Name</th>
                <th className="px-6 py-4">Rank</th>
                <th className="px-6 py-4">Salary</th>
                <th className="px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {instructors.map((inst) => (
                <tr key={inst.instructorId} className="hover:bg-slate-50/50 transition-colors group">
                  <td className="px-6 py-4">
                    <div className="font-semibold text-slate-800">{inst.person.firstName} {inst.person.lastName}</div>
                    <div className="text-xs text-slate-400">{inst.person.email}</div>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-3 py-1 bg-amber-50 text-amber-700 rounded-full text-xs font-bold uppercase border border-amber-100">{inst.rank || 'N/A'}</span>
                  </td>
                  <td className="px-6 py-4 text-slate-600 font-mono">${inst.salary?.toLocaleString()}</td>
                  <td className="px-6 py-4">
                    <div className="flex gap-2">
                      <button onClick={() => { setCurrentInst(inst); setShowModal(true); }} className="p-2 text-slate-400 hover:text-primary-600 hover:bg-primary-50 rounded-lg transition-colors">
                        <Edit2 size={18} />
                      </button>
                      <button onClick={() => handleDelete(inst.instructorId)} className="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors">
                        <Trash2 size={18} />
                      </button>
                      <button onClick={() => { setSelectedInstructor(inst); setShowCourseModal(true); }} className="p-2 text-slate-400 hover:text-emerald-600 hover:bg-emerald-50 rounded-lg transition-colors">
                        <BookOpen size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          )}
          {instructors.length === 0 && !loading && (
            <div className="flex flex-col items-center justify-center p-12 text-slate-400 text-center">
              <ShieldCheck size={48} className="mb-4 opacity-20" />
              <p>No instructors found. Start by hiring experts!</p>
            </div>
          )}
        </div>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden animate-in fade-in zoom-in duration-200">
            <div className="px-8 py-6 border-b border-slate-100 flex items-center justify-between bg-primary-900 text-white">
              <h3 className="text-xl font-bold flex items-center gap-2"><GraduationCap /> {currentInst.instructorId ? 'Update Profile' : 'New Assignment'}</h3>
              <button onClick={() => setShowModal(false)} className="text-primary-200 hover:text-white transition-colors">✕</button>
            </div>
            <form onSubmit={handleSave} className="p-8 space-y-6">
              <div className="grid grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">First Name</label>
                    <input required type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.person.firstName || ''} onChange={(e) => setCurrentInst({...currentInst, person: {...currentInst.person, firstName: e.target.value}})} />
                </div>
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Last Name</label>
                    <input required type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.person.lastName || ''} onChange={(e) => setCurrentInst({...currentInst, person: {...currentInst.person, lastName: e.target.value}})} />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">SSN</label>
                    <input required type="text" placeholder="XXX-XX-XXXX" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.person.ssn || ''} onChange={(e) => setCurrentInst({...currentInst, person: {...currentInst.person, ssn: e.target.value}})} />
                </div>
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Email</label>
                    <input required type="email" placeholder="email@university.edu" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.person.email || ''} onChange={(e) => setCurrentInst({...currentInst, person: {...currentInst.person, email: e.target.value}})} />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Address</label>
                    <input type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.person.address || ''} onChange={(e) => setCurrentInst({...currentInst, person: {...currentInst.person, address: e.target.value}})} />
                </div>
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Date of Birth</label>
                    <input type="date" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.person.dateOfBirth || ''} onChange={(e) => setCurrentInst({...currentInst, person: {...currentInst.person, dateOfBirth: e.target.value || null}})} />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-6">
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Rank</label>
                    <select required className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.rank} onChange={(e) => setCurrentInst({...currentInst, rank: e.target.value})}>
                        <option value="">Select Rank</option>
                        <option value="Professor">Professor</option>
                        <option value="Associate Professor">Associate Professor</option>
                        <option value="Assistant Professor">Assistant Professor</option>
                        <option value="Lecturer">Lecturer</option>
                    </select>
                </div>
                <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Salary</label>
                    <input required type="number" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl" value={currentInst.salary} onChange={(e) => setCurrentInst({...currentInst, salary: parseFloat(e.target.value)})} />
                </div>
              </div>
              <div className="flex justify-end gap-4 mt-8">
                <button type="button" onClick={() => setShowModal(false)} className="px-6 py-2 text-slate-500 font-semibold hover:bg-slate-100 rounded-xl">Cancel</button>
                <button type="submit" className="px-8 py-2 bg-primary-600 text-white font-bold rounded-xl shadow-lg shadow-primary-900/20 hover:bg-primary-700 transition-all active:scale-95">Save Profile</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showCourseModal && selectedInstructor && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden">
            <div className="px-8 py-6 border-b border-slate-100 flex items-center justify-between bg-emerald-600 text-white">
              <h3 className="text-xl font-bold">Assign Course</h3>
              <button onClick={() => setShowCourseModal(false)} className="text-emerald-200 hover:text-white transition-colors">✕</button>
            </div>
            <form onSubmit={handleAssignCourse} className="p-8 space-y-6">
              <div>
                <p className="text-sm text-slate-600 mb-4">Assign course to <span className="font-bold">{selectedInstructor.person.firstName} {selectedInstructor.person.lastName}</span></p>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Course</label>
                    <select required className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl" 
                      value={courseAssignment.courseId} 
                      onChange={(e) => setCourseAssignment({...courseAssignment, courseId: e.target.value})}>
                      <option value="">Select Course</option>
                      {courses.map(course => (
                        <option key={course.courseId} value={course.courseId}>
                          {course.title} ({course.credits} credits)
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Semester</label>
                      <select required className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl"
                        value={courseAssignment.semester}
                        onChange={(e) => setCourseAssignment({...courseAssignment, semester: e.target.value})}>
                        <option value="">Select</option>
                        <option value="Fall">Fall</option>
                        <option value="Spring">Spring</option>
                        <option value="Summer">Summer</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Year</label>
                      <input required type="number" className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl"
                        value={courseAssignment.year}
                        onChange={(e) => setCourseAssignment({...courseAssignment, year: parseInt(e.target.value)})} />
                    </div>
                  </div>
                </div>
              </div>
              <div className="flex justify-end gap-4 mt-8">
                <button type="button" onClick={() => setShowCourseModal(false)} className="px-6 py-2 text-slate-500 font-semibold hover:bg-slate-100 rounded-xl transition-colors">Cancel</button>
                <button type="submit" className="px-8 py-2 bg-emerald-600 text-white font-bold rounded-xl shadow-lg hover:bg-emerald-700 transition-all active:scale-95">Assign Course</button>
              </div>
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

export default Instructors;
