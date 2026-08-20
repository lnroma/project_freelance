<nav class="navbar navbar-expand-lg navbar-custom shadow-sm" style="border-radius: 0px!important">
    <div class="container-fluid">
        <!-- Логотип слева -->
        <a class="navbar-brand text-decoration-none" href="/dashboard">
            ServiceHub
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarMain">
            <!-- Основные пункты меню -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <#if authentication?? && authentication.isAuthenticated()>
                <li class="nav-item">
                    <a class="nav-link" href="/catalog/order/create">Создать заказ</a>
                </li>
                </#if>
                <li class="nav-item">
                    <a class="nav-link" href="/catalog">Список заказов</a>
                </li>
            </ul>

            <!-- Правая часть: сообщения, уведомления, профиль -->
            <div class="d-flex align-items-center gap-3">
            <#if authentication?? && authentication.isAuthenticated()>
                <!-- Сообщения (без иконки, только текст со счётчиком) -->
                <a href="/user/chat" class="text-decoration-none text-dark fw-bold" title="Сообщения">
                    Сообщения <span class="ms-1 text-danger fw-normal">(3)</span>
                </a>

                <!-- Уведомления (без иконки) -->
                <a href="/notifications" class="text-decoration-none text-dark fw-bold" title="Уведомления">
                    Уведомления <span class="ms-1 text-warning fw-normal">(1)</span>
                </a>
            </#if>

                <!-- Профиль (выпадающее меню) -->
                <div class="dropdown">
                    <a class="dropdown-toggle d-flex align-items-center gap-2 text-decoration-none text-dark"
                       href="#" role="button" data-bs-toggle="dropdown">
                        <img src="/assets/img/avatar.jpeg" alt="avatar"
                             class="rounded-circle" width="36" height="36" style="object-fit: cover;">
                        <span style="color: #333; font-size: 0.95rem;">Пользователь</span>
                        <svg class="bi ms-1" width="12" height="12"><use href="#icon-chevron-down"></use></svg>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end border-0 shadow">
                        <#if authentication?? && authentication.isAuthenticated()>
                            <li><a class="dropdown-item" href="/logout">Выход</a></li>
                            <li><a class="dropdown-item" href="/dashboard">Панель управления</a></li>
                            <li><a class="dropdown-item" href="/profile">Профиль</a></li>
                            <li><a class="dropdown-item" href="/settings">Настройки</a></li>
                        <#else>
                            <li><a class="dropdown-item" href="/login">Вход</a></li>
                            <li><a class="dropdown-item" href="/registration">Регистрация</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>