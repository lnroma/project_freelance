<!DOCTYPE html>
<html lang="ru">
<#include "../fragments/httpHeader.ftl">
<body class="d-flex flex-column bg-light">

<!-- Header -->
<#include "../fragments/header.ftl">

<div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h2>Панель управления пользователя</h2>
                                    <div>
                                        <span class="text-muted">Активность пользователя: <strong>72%</strong></span>
                                    </div>
                                </div>
    <#include "../fragments/breadcrumbs.ftl">
    <#if !profileExist>
        <div class="alert alert-warning d-flex align-items-center gap-3 border-0 shadow-sm" role="alert">
            <i class="fas fa-exclamation-triangle text-warning" style="font-size:1.2rem"></i>
            <div>
                <strong>У вас не создан профиль.</strong> Пожалуйста, пройдите по ссылке, чтобы его создать.
                <a href="/user/profile/add" class="ms-2 fw-bold">Создать профиль</a>
            </div>
        </div>
    </#if>

    <div class="row">
        <!-- Сайдбар -->
        <div class="col-md-3 mb-4">
            <div class="card dashboard-card h-100 border-0 shadow-sm">
                <div class="text-center py-4 bg-white rounded-top" style="border-bottom: 1px solid var(--border);">
                    <img src="${user.avatarUrl!'https://ui-avatars.com/api/?name=' + (user.getEmail()!'User') + '&background=667eea&color=fff'}"
                         alt="Аватар"
                         class="user-avatar mb-3"
                         style="width: 80px; height: 80px; object-fit: cover;">
                    <h5 class="mb-1 text-dark">${user.getEmail()!'Пользователь'}</h5>
                    <p class="text-muted small mb-0 fw-normal">${user.getRole()!'user'}</p>
                </div>

                <ul class="nav flex-column sidebar-nav px-2 mt-2">
                    <li class="nav-item">
                        <a class="nav-link active text-dark" href="/dashboard">
                            <i class="fas fa-home me-2 text-primary"></i>Главная
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-muted" href="/profile">
                            <i class="fas fa-user me-2"></i>Профиль
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-muted" href="/user/${user.getId()!0}/orders">
                            <i class="fas fa-box me-2"></i>Мои заказы
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-muted" href="/user/chat">
                            <i class="fas fa-comments me-2"></i>Сообщения
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-muted" href="/settings">
                            <i class="fas fa-cog me-2"></i>Настройки
                        </a>
                    </li>
                </ul>
            </div>
        </div>

        <!-- Основной контент -->
        <div class="col-md-9">
            <!-- Статистические карточки -->
            <div class="row mb-4 g-4">
                <div class="col-sm-6 col-lg-3">
                    <div class="card stat-card dashboard-card border-0 shadow-sm h-100">
                        <div class="card-body p-3">
                            <div class="d-flex align-items-center">
                                <div class="me-3 p-2 bg-light rounded text-primary" style="border: 1px solid var(--border);">
                                    <i class="fas fa-users fa-lg"></i>
                                </div>
                                <div>
                                    <h6 class="text-muted mb-0 small text-uppercase fw-bold lh-1">Всего заказов</h6>
                                    <h3 class="mb-0 fw-bold" style="color: var(--text-main);">${stats.totalOrders!0}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="card stat-card dashboard-card border-0 shadow-sm h-100">
                        <div class="card-body p-3">
                            <div class="d-flex align-items-center">
                                <div class="me-3 p-2 bg-light rounded text-success" style="border: 1px solid var(--border);">
                                    <i class="fas fa-check-circle fa-lg"></i>
                                </div>
                                <div>
                                    <h6 class="text-muted mb-0 small text-uppercase fw-bold lh-1">Выполнено</h6>
                                    <h3 class="mb-0 fw-bold" style="color: var(--text-main);">${stats.completedOrders!0}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="card stat-card dashboard-card border-0 shadow-sm h-100">
                        <div class="card-body p-3">
                            <div class="d-flex align-items-center">
                                <div class="me-3 p-2 bg-light rounded text-warning" style="border: 1px solid var(--border);">
                                    <i class="fas fa-clock fa-lg"></i>
                                </div>
                                <div>
                                    <h6 class="text-muted mb-0 small text-uppercase fw-bold lh-1">В работе</h6>
                                    <h3 class="mb-0 fw-bold" style="color: var(--text-main);">${stats.inProgressOrders!0}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6 col-lg-3">
                    <div class="card stat-card dashboard-card border-0 shadow-sm h-100">
                        <div class="card-body p-3">
                            <div class="d-flex align-items-center">
                                <div class="me-3 p-2 bg-light rounded text-danger" style="border: 1px solid var(--border);">
                                    <i class="fas fa-exclamation-triangle fa-lg"></i>
                                </div>
                                <div>
                                    <h6 class="text-muted mb-0 small text-uppercase fw-bold lh-1">Требуют внимания</h6>
                                    <h3 class="mb-0 fw-bold" style="color: var(--text-main);">${stats.pendingOrders!0}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Секция «Последние действия» -->
            <div class="card dashboard-card border-0 shadow-sm mb-4">
                <div class="card-header bg-white py-3" style="border-bottom: 1px solid var(--border); border-radius: 0.5rem 0.5rem 0 0;">
                    <h5 class="mb-0 fw-bold text-dark">
                        <i class="fas fa-history me-2 text-primary"></i>Последние действия
                    </h5>
                </div>
                <div class="card-body p-0">
                    <#if recentActivities?has_content>
                        <ul class="list-group list-group-flush">
                            <#list recentActivities as activity>
                                <li class="list-group-item d-flex justify-content-between align-items-center border-0 px-3 py-3">
                                    <div>
                                        <h6 class="mb-1 fw-semibold text-dark">${activity.description!'Действие'}</h6>
                                        <small class="text-muted">${activity.timestamp!'Сейчас'}</small>
                                    </div>
                                    <span class="badge bg-light text-dark border rounded-pill px-3 py-1" style="border-color: var(--border); font-size: 0.85rem;">
                                        ${activity.type!'info'}
                                    </span>
                                </li>
                            </#list>
                        </ul>
                    <#else>
                        <div class="p-5 text-center text-muted">
                            Нет недавних действий
                        </div>
                    </#if>
                </div>
            </div>

            <!-- Пример таблицы заказов -->
            <div class="card dashboard-card border-0 shadow-sm">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center" style="border-bottom: 1px solid var(--border); border-radius: 0.5rem 0.5rem 0 0;">
                    <h5 class="mb-0 fw-bold text-dark">
                        <i class="fas fa-list me-2 text-primary"></i>Активные заказы
                    </h5>
                    <a href="/orders" class="btn btn-sm btn-outline-primary rounded-pill px-4" style="border-color: var(--accent); color: var(--accent);">
                        Все заказы
                    </a>
                </div>
                <div class="card-body p-0">
                    <#if activeOrders?has_content>
                        <table class="table table-hover align-middle mb-0 table-striped-custom">
                            <thead class="table-light">
                                <tr>
                                    <th class="fw-semibold text-muted small ps-4">ID</th>
                                    <th class="fw-semibold text-muted small">Услуга / Категория</th>
                                    <th class="fw-semibold text-muted small">Статус</th>
                                    <th class="fw-semibold text-muted small">Дата</th>
                                    <th class="fw-semibold text-muted small pe-4 text-end">Действия</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#list activeOrders as order>
                                    <tr>
                                        <td class="ps-4 fw-medium">${order.getId()!"1"}</td>
                                        <td class="fw-medium">
                                            <#if order.categoryId??>Категория #${order.categoryId}<#else>Не указано</#if>
                                        </td>
                                        <td>
                                            <#assign statusVal = order.status!'NEW'>
                                            <#if statusVal == 'COMPLETED'>
                                                <span class="badge bg-success text-white px-3 py-1 rounded-pill fw-normal" style="font-size: 0.8rem;">Выполнено</span>
                                            <#elseif statusVal == 'IN_PROGRESS'>
                                                <span class="badge bg-warning text-dark px-3 py-1 rounded-pill fw-normal" style="font-size: 0.8rem; border: 1px solid var(--border);">В работе</span>
                                            <#else>
                                                <span class="badge bg-secondary text-white px-3 py-1 rounded-pill fw-normal" style="font-size: 0.8rem;">Новый</span>
                                            </#if>
                                        </td>
                                        <td class="text-muted small">${order.createdAt!'--'}</td>
                                        <td class="pe-4 text-end">
                                            <button class="btn btn-sm btn-light rounded-circle" style="border: 1px solid var(--border); color: var(--text-main);">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    <#else>
                        <div class="p-5 text-center text-muted">
                            У вас нет активных заказов
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<#include "../fragments/footer.ftl">
<#include "../fragments/js.ftl">

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
