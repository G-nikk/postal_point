import { Routes, Route, Link } from 'react-router-dom';
import NewspaperList from './components/NewspaperList';
import PrintingHouseList from './components/PrintingHouseList';

export default function App() {
    return (
        <div>
            <nav style={{ padding: '20px', backgroundColor: '#f0f0f0' }}>
                <Link to="/newspapers" style={{ textDecoration: 'none', color: '#1976d2' }}>
                    Управление газетами
                </Link>
                <Link to="/printing-houses" style={{ textDecoration: 'none', color: '#1976d2' }}>Управление типографиями</Link>
            </nav>

            <Routes>
                <Route path="/newspapers" element={<NewspaperList />} />
                <Route path="/printing-houses" element={<PrintingHouseList />} />
                <Route path="/" element={<h1 style={{ textAlign: 'center' }}>Добро пожаловать в систему управления газетами</h1>} />
            </Routes>
        </div>
    );
}