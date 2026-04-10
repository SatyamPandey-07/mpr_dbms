import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import Students from './pages/Students';
import Instructors from './pages/Instructors';
import Courses from './pages/Courses';
import Departments from './pages/Departments';

function App() {
  return (
    <Router>
      <div className="flex min-h-screen bg-slate-50">
        <Sidebar />
        <div className="flex-1 flex flex-col">
          <Navbar />
          <main className="p-6 overflow-y-auto">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/students" element={<Students />} />
              <Route path="/instructors" element={<Instructors />} />
              <Route path="/courses" element={<Courses />} />
              <Route path="/departments" element={<Departments />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
}

export default App;
