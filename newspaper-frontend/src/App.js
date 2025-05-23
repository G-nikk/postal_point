import { Routes, Route, Link } from 'react-router-dom';
import NewspaperList from './components/NewspaperList';

// Добавьте export default здесь
export default function App() {
    return (
        <div>
            <nav style={{ padding: '20px', backgroundColor: '#f0f0f0' }}>
                <Link to="/newspapers" style={{ textDecoration: 'none', color: '#1976d2' }}>
                    Newspapers Management
                </Link>
            </nav>

            <Routes>
                <Route path="/newspapers" element={<NewspaperList />} />
                <Route path="/" element={<h1 style={{ textAlign: 'center' }}>Welcome to Newspaper System</h1>} />
            </Routes>
        </div>
    );
}