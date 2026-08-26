<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
    <div class="container">
        <!-- Логотип -->
        <a class="navbar-brand fw-bold text-primary" href="/">ServiceHub</a>

        <!-- Кнопка для мобильных -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainMenu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainMenu">
            <!-- Категории (как на Kwork) -->
            <ul class="navbar-nav me-auto align-items-center">
                <li class="nav-item">
                    <a class="nav-link" href="/catalog">Заказы</a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="/blog">Блог</a>
                </li>
                <#if isAuthenticated>
                    <li class="nav-item">
                        <a class="nav-link" href="/dashboard">Дашборд</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/user/chat/">Сообщения</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/settings">Настройки</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/notifications">Уведомления</a>
                    </li>
                </#if>
            </ul>

            <!-- Быстрые действия (CTA) -->
            <div class="d-flex align-items-center gap-2">
                <#if isAuthenticated>
                <a href="/catalog/order/create" class="btn btn-primary btn-sm px-3">Разместить заказ</a>
                <#else>
                <a href="/login" class="btn btn-outline-secondary btn-sm px-3">Вход</a>
                <a href="/registration" class="btn btn-light btn-sm px-3 text-primary">Регистрация</a>
                </#if>
            </div>

            <#if isAuthenticated >
            <!-- Профиль / уведомления (упрощённо) -->
            <div class="ms-2 d-none d-md-block">
                <img src="/assets/img/avatar.jpeg" alt="avatar"
                     class="rounded-circle" width="36" height="36" style="object-fit: cover;">
                <a href="/profile" class="text-decoration-none text-dark">
                    Профиль: ${user.id}
                </a>
            </div>
            </#if>
        </div>
    </div>
</nav>
