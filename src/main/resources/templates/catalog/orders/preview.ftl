<!DOCTYPE html>
<html lang="ru" itemscope itemtype="https://schema.org/Service">
<#include "../../fragments/httpHeader.ftl">
<body class="bg-light">
<#include "../../fragments/header.ftl">

<style>
    .app-card {
        background: #ffffff;
        border-radius: 12px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, .05), 0 2px 6px rgba(0, 0, 0, .08);
        padding: 24px;
    }

    .stat-badge {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 99px;
        font-size: 0.85rem;
        color: #495057;
        background: #f8f9fa;
    }

    /* Оранжево‑красный акцент (как в твоём стиле) */
    .btn-primary-app {
        background-color: #d35400;
        border-color: #d35400;
    }

    .btn-primary-app:hover {
        background-color: #b04100;
        border-color: #b04100;
    }

    .text-accent-orange {
        color: #d35400;
    }

    .star-fill {
        color: #ffc107;
    }
</style>

<div class="container mt-5">
    <div class="order-header-row">
        <div class="d-flex justify-content-between align-items-start flex-column flex-md-row mb-3">
            <div>
                <h2 class=" mb-1">${order.title}</h2>
                <span class="text-muted small">
                    Бюджет: ${order.priceFrom} – ${order.priceTo} ₽
                </span>
            </div>
            <div class="text-end">
                <span class="stat-badge">
                    <strong>9</strong> ответов
                </span>
                <span class="stat-badge ms-2">
                    <strong>47</strong> просмотров
                </span>
            </div>
        </div>
    </div>

    <#include "../../fragments/breadcrumbs.ftl">

    <!-- Контент страницы заказа -->
    <div class="order-details">
        <!-- Микроразметка для Яндекс/Google -->
        <div itemscope itemtype="https://schema.org/Offer">
            <meta itemprop="name" content="${order.title}">
            <meta itemprop="priceCurrency" content="RUB">
            <meta itemprop="lowPrice" content="${order.priceFrom}">
            <meta itemprop="highPrice" content="${order.priceTo}">

            <div class="app-card mb-4 primary-border">
                <a href="/catalog/order/${order.id}/preview" class="btn btn-primary">Основное</a>
                <#if isAuthenticated >
                    <a href="/catalog/order/${order.id}/preview/offers"
                       class="btn btn-primary">Предложения(${order.responses?size})</a>
                </#if>
                <a href="/catalog/order/${order.id}/preview/faq" class="btn btn-primary">Вопросы и
                    ответы(${order.orderFaqEntities?size})</a>
            </div>
            <div class="app-card mb-4 primary-border">
                <p itemprop="description" class="lead">${order.description}</p>
            </div>

            <!-- Метаинформация -->
            <div class="app-card mb-4 primary-border">
                <div class="row g-4">
                    <div class="col-md-6" style="border-right: 1px solid green">
                        <div class="fw-semibold text-muted small mb-1">Бюджет</div>
                        <div class="fw-bold fs-5 text-accent-orange">
                            ${order.priceFrom} – ${order.priceTo} ₽
                        </div>
                        <div class="mt-3">
                            <div class="fw-semibold text-muted small">Предложений</div>
                            <div class="fw-bold">12</div>
                        </div>
                    </div>
                    <div class="col-md-6 d-flex flex-column">
                        <div class="fw-semibold text-muted small">Дата создания</div>
                        <div class="fw-bold">${order.createdAt}</div>
                        <div class="mt-2">
                            <div class="fw-semibold text-muted small">Заказчик</div>
                            <a href="/profile/${profileService.getCurrentUserProfile(order.creator).id}" itemprop="url" class="fw-bold text-decoration-none">
                                ${profileService.getCurrentUserProfile(order.creator).firstName} ${profileService.getCurrentUserProfile(order.creator).lastName}
                            </a>
                            <#if isAuthenticated >
                            <a href="/send/message/to/${order.creator.id}">Написать сообщение</a>
                            </#if>
                            <span class="ms-2">
                                <i class="fas fa-star star-fill"></i> 4.8
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Форма подачи предложения -->
            <div class="app-card primary-border">
                <h4 class="fw-bold mb-3 border-bottom pb-2">Отправить предложение</h4>
                <#if isAuthenticated >
                    <form itemprop="potentialAction" itemscope itemtype="https://schema.org/ApplyAction"
                          action="/catalog/order/${order.id}/response" method="post">
                        <input type="hidden" itemprop="target" value="/offer/submit">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="offerText" class="form-label fw-semibold">Текст предложения</label>
                                <textarea class="form-control" id="offerText" rows="4"
                                          name="description"
                                          placeholder="Опишите ваш подход к выполнению заказа, опыт работы и преимущества сотрудничества..."
                                          required></textarea>
                            </div>
                            <div class="col-md-6">
                                <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}"/>
                                <div>
                                    <label for="offerBudget" class="form-label fw-semibold">Предварительный бюджет
                                        (руб.)</label>
                                    <input type="number" name="price" class="form-control" id="offerBudget"
                                           placeholder="100000"
                                           required>
                                </div>
                                <div>
                                    <label for="offerDeadline" class="form-label fw-semibold">Срок выполнения</label>
                                    <input type="number" name="timeline" class="form-control" id="offerDeadline"
                                           placeholder="30 рабочих дней"
                                           required>
                                </div>
                            </div>
                        </div>
                        <div class="mt-4 pt-3 border-top">
                            <button type="submit" class="btn btn-primary btn-lg">
                                Ответить на заказ
                            </button>
                        </div>
                    </form>
                <#else>
                    <div class="lead">Что бы отправить предложение вы должны
                        либо <a href="/login">вторизоваться</a> либо <a href="/registration">Зарегистрироваться</a> в
                        сервисе.
                    </div>
                </#if>
            </div>
            <#if isAuthenticated >
                <#if user.id == order.creator.id >
                    <#include "./components/order_responses.ftl">
                </#if>
            </#if>
        </div>
    </div>
</div>

<#include "../../fragments/footer.ftl">

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
