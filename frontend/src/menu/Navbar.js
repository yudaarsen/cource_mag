export default function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <a className="navbar-brand" href="/">Главная</a>
            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item active">
                        <a className="nav-link" href="/documents">Бухгалтерия</a>
                    </li>
                    <li className="nav-item active">
                        <a className="nav-link" href="/employees">Кадры</a>
                    </li>
                    <li className="nav-item active">
                        <a className="nav-link" href="/report/osv">ОСВ</a>
                    </li>
                </ul>
            </div>
        </nav>
    );
}