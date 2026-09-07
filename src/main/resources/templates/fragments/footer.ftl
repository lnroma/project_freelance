<svg style="display: none;">
    <symbol id="icon-plus-circle" viewBox="0 0 24 24"><path d="M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2Zm5 11h-4v4a1 1 0 0 1-2 0v-4H7a1 1 0 0 1 0-2h4V7a1 1 0 0 1 2 0v4h4a1 1 0 0 1 0 2Z"/></symbol>
    <symbol id="icon-list" viewBox="0 0 24 24"><path d="M2 6h20v2H2V6Zm0 5h20v2H2v-2Zm0 5h20v2H2v-2Z"/></symbol>
    <symbol id="icon-chat" viewBox="0 0 24 24"><path d="M14.5 2H6a3 3 0 0 0-3 3v16l6-2 6 2V5a3 3 0 0 0-3-3Zm-2 13.5L8.5 14l-4 2v-3.5l4-2 4 2Zm6-8a3 3 0 0 0-3-3h-1v12l4 2V5a3 3 0 0 0-3-3Z"/></symbol>
    <symbol id="icon-bell" viewBox="0 0 24 24"><path d="M7.5 15a.5.5 0 0 1-.5-.5V14a6.5 6.5 0 0 1 13 0v.5a.5.5 0 0 1-1 0v-.5a4.5 4.5 0 0 0-9 0v.5Zm8.5-9V4a2.5 2.5 0 0 0-5 0v1.5h5Z"/><path d="M13 21a1 1 0 0 1-2 0v-2a1 1 0 0 1 2 0v2Zm-7.5-4h11a.5.5 0 0 0 .5-.5v-.5a2.5 2.5 0 0 0-5-2.5H9a2.5 2.5 0 0 0-2.5 2.5v.5a.5.5 0 0 0 .5.5Z"/></symbol>
    <symbol id="icon-chevron-down" viewBox="0 0 24 24"><path d="M6 9a1 1 0 0 0 0 2h8a1 1 0 0 0 0-2H6Zm0 5a1 1 0 0 0 0 2h8a1 1 0 0 0 0-2H6Z"/></symbol>
    <symbol id="icon-box-arrow-in-right" viewBox="0 0 24 24"><path d="M4 12a1 1 0 0 1 1-1h10.5L12.75 8.25a.5.5 0 0 1 .707.707L17.114 12l-3.657 2.843a.5.5 0 0 1-.707-.707L15.5 12H5a1 1 0 0 1-1-1Zm16 0a1 1 0 0 1 1 1v4a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V8a1 1 0 0 1 1-1h16a1 1 0 0 1 1 1Z"/></symbol>
    <symbol id="icon-person-plus" viewBox="0 0 24 24"><path d="M13 6a3 3 0 1 0-6 0A3 3 0 0 0 13 6Zm-1 4h2a1 1 0 0 1 1 1v6a1 1 0 0 1-1 1H8a1 1 0 0 1-1-1v-6a1 1 0 0 1 1-1h2Zm6 5a1 1 0 0 1 1-1v-2a1 1 0 0 0-1-1H10a1 1 0 0 0-1 1v2a1 1 0 0 1 1 1h8Z"/></symbol>
    <symbol id="icon-box-arrow-right" viewBox="0 0 24 24"><path d="M4 12a1 1 0 0 1 1-1h10.5L12.75 8.25a.5.5 0 0 1 .707.707L17.114 12l-3.657 2.843a.5.5 0 0 1-.707-.707L15.5 12H5a1 1 0 0 1-1-1Zm16 0a1 1 0 0 1 1 1v4a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V8a1 1 0 0 1 1-1h16a1 1 0 0 1 1 1Z"/></symbol>
</svg>

<style>
    .navbar-custom {
        /* Мягкий градиент: почти белый → тёплый оранжевый, не яркий */
        /*background: linear-gradient(90deg, #fffefd 0%, #ffdbb3 40%, #ff8c42 100%);*/
        border-radius: 0 0 12px 12px;
        background-color: #ffdfc4;
        box-shadow: 0 2px 8px rgba(255, 140, 66, 0.18); /* лёгкая тень под цвет темы */
    }

    .navbar-custom .navbar-brand {
        color: #2c3e50 !important; /* тёмно‑серый вместо белого — мягче на светлом фоне */
        font-weight: 700;
        letter-spacing: 0.5px;
    }

    .navbar-custom .nav-link {
        color: #555 !important;
        transition: color 0.2s, background 0.2s;
    }

    .navbar-custom .nav-link:hover {
        color: #d35400 !important; /* при наведении — более тёплый акцент */
        background: rgba(255, 140, 66, 0.08);
        border-radius: 6px;
    }

    /* Выпадающее меню профиля — чистый белый, нейтральный фон */
    .navbar-custom .dropdown-menu {
        background-color: #ffffff;
        border: 1px solid #f0f0f0;
        box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
    }

    .navbar-custom .dropdown-item {
        color: #444;
        padding: 0.6rem 1rem;
    }

    .navbar-custom .dropdown-item:hover {
        background-color: #fff8f0; /* очень мягкий персиковый при наведении */
        color: #d35400;
    }

    .navbar-custom .dropdown-divider {
        border-top: 1px solid #eee;
    }
</style>
<!-- Футер -->
<footer class="footer-custom text-center">
    <div class="container">
        <p class="mb-0">© Все права защищены. 2024</p>
        <p class="mb-0">Телефон: 8 800 200‑0600</p>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<p style='display:none'>test passed</p>