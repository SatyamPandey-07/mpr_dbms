import { useState, useEffect } from 'react';
import { Plus, Search, Edit2, Trash2, UserPlus, Users } from 'lucide-react';
import api from '../services/api';
import Toast from '../components/Toast';
import LoadingSpinner from '../components/LoadingSpinner';

const Students = () => {
  const [students, setStudents] = useState([]);
  const [instructors, setInstructors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showAdvisorModal, setShowAdvisorModal] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [toast, setToast] = useState(null);
  const [currentStudent, setCurrentStudent] = useState({
    person: { ssn: '', firstName: '', lastName: '', email: '', address: '', dateOfBirth: '' },
    enrollmentYear: 2024,
    major: ''
  });

  useEffect(() => {
    fetchStudents();
    fetchInstructors();
  }, []);

  const fetchStudents = async () => {
    try {
      const res = await api.get('/students');
      setStudents(res.data.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching students:", error);
      setToast({ message: 'Failed to fetch students', type: 'error' });
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

  const handleSearch = async () => {
    if (searchTerm.trim() === '') {
      fetchStudents();
      return;
    }
    try {
      const res = await api.get(`/students/search?q=${searchTerm}`);
      setStudents(res.data.data);
    } catch (error) {
      console.error("Error searching students:", error);
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (currentStudent.studentId) {
        await api.put(`/students/${currentStudent.studentId}`, currentStudent);
        setToast({ message: 'Student updated successfully', type: 'success' });
      } else {
        await api.post('/students', currentStudent);
        setToast({ message: 'Student added successfully', type: 'success' });
      }
      fetchStudents();
      setShowModal(false);
    } catch (error) {
      console.error("Error saving student:", error);
      setToast({ message: 'Failed to save student', type: 'error' });
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this student?")) {
      try {
        await api.delete(`/students/${id}`);
        setToast({ message: 'Student deleted successfully', type: 'success' });
        fetchStudents();
      } catch (error) {
        console.error("Error deleting student:", error);
        setToast({ message: 'Failed to delete student', type: 'error' });
      }
    }
  };

  const handleAssignAdvisor = async (e) => {
    e.preventDefault();
    try {
      const advisorId = e.target.advisor.value;
      await api.post(`/instructors/${advisorId}/advisees/${selectedStudent.studentId}`);
      setToast({ message: 'Advisor assigned successfully', type: 'success' });
      setShowAdvisorModal(false);
      fetchStudents();
    } catch (error) {
      console.error("Error assigning advisor:", error);
      setToast({ message: 'Failed to assign advisor', type: 'error' });
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-800">Student Management</h1>
        <button 
          onClick={() => { setCurrentStudent({ person: {}, enrollmentYear: 2024, major: '' }); setShowModal(true); }}
          className="bg-primary-600 hover:bg-primary-700 text-white px-4 py-2 rounded-lg flex items-center gap-2 shadow-lg shadow-primary-900/20 transition-all active:scale-95"
        >
          <Plus size={18} /> Add Student
        </button>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
        <div className="p-4 border-b border-slate-100 bg-slate-50/50 flex items-center gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text" 
              placeholder="Search students..." 
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              className="w-full pl-10 pr-4 py-2 bg-white border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500 transition-all"
            />
          </div>
          <button onClick={handleSearch} className="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700">Search</button>
          <button onClick={() => { setSearchTerm(''); fetchStudents(); }} className="px-4 py-2 bg-slate-200 text-slate-700 rounded-lg hover:bg-slate-300">Clear</button>
        </div>

        <div className="overflow-x-auto">
          {loading ? (
            <LoadingSpinner />
          ) : (
            <table className="w-full text-left">
            <thead>
              <tr className="bg-slate-50/50 text-slate-500 text-sm font-semibold uppercase tracking-wider">
                <th className="px-6 py-4">ID</th>
                <th className="px-6 py-4">Name</th>
                <th className="px-6 py-4">Major</th>
                <th className="px-6 py-4">Year</th>
                <th className="px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {students.map((student) => (
                <tr key={student.studentId} className="hover:bg-slate-50/50 transition-colors group">
                  <td className="px-6 py-4 font-mono text-sm text-slate-400">{student.studentId}</td>
                  <td className="px-6 py-4">
                    <div className="font-semibold text-slate-800">{student.person.firstName} {student.person.lastName}</div>
                    <div className="text-xs text-slate-400">{student.person.email}</div>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-3 py-1 bg-primary-50 text-primary-700 rounded-full text-xs font-bold uppercase">{student.major}</span>
                  </td>
                  <td className="px-6 py-4 text-slate-600">{student.enrollmentYear}</td>
                  <td className="px-6 py-4">
                    <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                      <button onClick={() => { setCurrentStudent(student); setShowModal(true); }} className="p-2 text-slate-400 hover:text-primary-600 hover:bg-primary-50 rounded-lg transition-colors">
                        <Edit2 size={18} />
                      </button>
                      <button onClick={() => handleDelete(student.studentId)} className="p-2 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors">
                        <Trash2 size={18} />
                      </button>
                      <button onClick={() => { setSelectedStudent(student); setShowAdvisorModal(true); }} className="p-2 text-slate-400 hover:text-emerald-600 hover:bg-emerald-50 rounded-lg transition-colors">
                        <Users size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          )}
          {students.length === 0 && !loading && (
            <div className="flex flex-col items-center justify-center p-12 text-slate-400">
              <UserPlus size={48} className="mb-4 opacity-20" />
              <p>No students found. Add your first student!</p>
            </div>
          )}
        </div>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden animate-in fade-in zoom-in duration-200">
            <div className="px-8 py-6 border-b border-slate-100 flex items-center justify-between bg-primary-900 text-white">
              <h3 className="text-xl font-bold">{currentStudent.studentId ? 'Edit Student' : 'Add New Student'}</h3>
              <button onClick={() => setShowModal(false)} className="text-primary-200 hover:text-white transition-colors">✕</button>
            </div>
            <form onSubmit={handleSave} className="p-8 space-y-6">
              <div className="grid grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2">First Name</label>
                  <input required type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" value={currentStudent.person.firstName} onChange={(e) => setCurrentStudent({...currentStudent, person: {...currentStudent.person, firstName: e.target.value}})} />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2">Last Name</label>
                  <input required type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" value={currentStudent.person.lastName} onChange={(e) => setCurrentStudent({...currentStudent, person: {...currentStudent.person, lastName: e.target.value}})} />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2">SSN</label>
                  <input required type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" value={currentStudent.person.ssn} onChange={(e) => setCurrentStudent({...currentStudent, person: {...currentStudent.person, ssn: e.target.value}})} />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2">Email</label>
                  <input required type="email" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" value={currentStudent.person.email} onChange={(e) => setCurrentStudent({...currentStudent, person: {...currentStudent.person, email: e.target.value}})} />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2">Major</label>
                  <input required type="text" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" value={currentStudent.major} onChange={(e) => setCurrentStudent({...currentStudent, major: e.target.value})} />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2">Enrollment Year</label>
                  <input required type="number" className="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500" value={currentStudent.enrollmentYear} onChange={(e) => setCurrentStudent({...currentStudent, enrollmentYear: parseInt(e.target.value)})} />
                </div>
              </div>
              <div className="flex justify-end gap-4 mt-8">
                <button type="button" onClick={() => setShowModal(false)} className="px-6 py-2 text-slate-500 font-semibold hover:bg-slate-100 rounded-xl transition-colors">Cancel</button>
                <button type="submit" className="px-8 py-2 bg-primary-600 text-white font-bold rounded-xl shadow-lg shadow-primary-900/20 hover:bg-primary-700 transition-all active:scale-95">Save Student</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showAdvisorModal && selectedStudent && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden">
            <div className="px-8 py-6 border-b border-slate-100 flex items-center justify-between bg-emerald-600 text-white">
              <h3 className="text-xl font-bold">Assign Advisor</h3>
              <button onClick={() => setShowAdvisorModal(false)} className="text-emerald-200 hover:text-white transition-colors">✕</button>
            </div>
            <form onSubmit={handleAssignAdvisor} className="p-8 space-y-6">
              <div>
                <p className="text-sm text-slate-600 mb-4">Select an advisor for <span className="font-bold">{selectedStudent.person.firstName} {selectedStudent.person.lastName}</span></p>
                <label className="block text-sm font-semibold text-slate-700 mb-2">Advisor</label>
                <select name="advisor" required className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500">
                  <option value="">Select Instructor</option>
                  {instructors.map(inst => (
                    <option key={inst.instructorId} value={inst.instructorId}>
                      {inst.person.firstName} {inst.person.lastName} ({inst.rank})
                    </option>
                  ))}
                </select>
              </div>
              <div className="flex justify-end gap-4 mt-8">
                <button type="button" onClick={() => setShowAdvisorModal(false)} className="px-6 py-2 text-slate-500 font-semibold hover:bg-slate-100 rounded-xl transition-colors">Cancel</button>
                <button type="submit" className="px-8 py-2 bg-emerald-600 text-white font-bold rounded-xl shadow-lg hover:bg-emerald-700 transition-all active:scale-95">Assign Advisor</button>
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

export default Students;
