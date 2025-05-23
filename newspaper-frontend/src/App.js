import { Routes, Route, Link } from 'react-router-dom';
import NewspaperList from './components/NewspaperList';
import PrintingHouseList from './components/PrintingHouseList';
import PostOfficeList from './components/PostOfficeList';

export default function App() {
    return (
        <div>
            <nav style={{
                padding: '20px',
                backgroundColor: '#f0f0f0',
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
                gap: '30px'
            }}>
                <Link
                    to="/newspapers"
                    style={{
                        textDecoration: 'none',
                        color: '#1976d2',
                        padding: '10px 15px',
                        whiteSpace: 'nowrap'
                    }}>
                    Управление газетами
                </Link>
                <Link
                    to="/printing-houses"
                    style={{
                        textDecoration: 'none',
                        color: '#1976d2',
                        padding: '10px 15px',
                        whiteSpace: 'nowrap'
                    }}>
                    Управление типографиями
                </Link>
                <Link
                    to="/post-offices"
                    style={{
                        textDecoration: 'none',
                        color: '#1976d2',
                        padding: '10px 15px',
                        whiteSpace: 'nowrap'
                    }}>
                    Почтовые отделения
                </Link>
            </nav>

            <Routes>
                <Route path="/newspapers" element={<NewspaperList/>}/>
                <Route path="/printing-houses" element={<PrintingHouseList/>}/>
                <Route path="/"
                       element={<h1 style={{textAlign: 'center'}}>Добро пожаловать в систему управления газетами</h1>}/>
                <Route path="/post-offices" element={<PostOfficeList/>}/>
            </Routes>
        </div>
    );
}