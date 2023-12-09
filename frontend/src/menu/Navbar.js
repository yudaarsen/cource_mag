export default function Navbar() {
    return (
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="navbar-brand" href="/">Главная</a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="/documents">Бухгалтерия</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="/employees">Кадры</a>
                    </li>
                </ul>
            </div>
        </nav>
    );
}