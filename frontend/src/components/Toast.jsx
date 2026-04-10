import { CheckCircle, XCircle, X } from 'lucide-react';

const Toast = ({ message, type = 'success', onClose }) => {
  setTimeout(() => {
    onClose();
  }, 3000);

  return (
    <div className={`fixed bottom-4 right-4 z-50 flex items-center gap-3 px-6 py-4 rounded-xl shadow-2xl animate-in slide-in-from-right duration-300 ${
      type === 'success' ? 'bg-emerald-600 text-white' : 'bg-red-600 text-white'
    }`}>
      {type === 'success' ? <CheckCircle size={20} /> : <XCircle size={20} />}
      <span className="font-semibold">{message}</span>
      <button onClick={onClose} className="ml-2 hover:opacity-70">
        <X size={16} />
      </button>
    </div>
  );
};

export default Toast;
