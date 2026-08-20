<!DOCTYPE html>
<html lang="ru" itemscope itemtype="https://schema.org/Service">
<#include "../../fragments/httpHeader.ftl">
<body class="bg-light">
<#include "../../fragments/header.ftl">

<style>
    .app-card {
        background: #ffffff;
        border-radius: 12px;
        box-shadow: 0 1px 3px rgba(0,0,0,.05), 0 2px 6px rgba(0,0,0,.08);
        padding: 24px;
    }
    .order-header-row {
        border-bottom: 1px solid #e9ecef;
        padding-bottom: 16px;
        margin-bottom: 20px;
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
    .text-accent-orange { color: #d35400; }
    .star-fill { color: #ffc107; }
</style>

<div class="container py-4">
    <div class="order-header-row">
        <div class="d-flex justify-content-between align-items-start flex-column flex-md-row mb-3">
            <div>
                <h2 class="fw-bold mb-1">${order.title}</h2>
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

    <!-- Контент страницы заказа -->
    <div class="order-details">
        <!-- Микроразметка для Яндекс/Google -->
        <div itemscope itemtype="https://schema.org/Offer">
            <meta itemprop="name" content="${order.title}">
            <meta itemprop="priceCurrency" content="RUB">
            <meta itemprop="lowPrice" content="${order.priceFrom}">
            <meta itemprop="highPrice" content="${order.priceTo}">

            <div class="app-card mb-4">
                <p itemprop="description" class="lead">${order.description}</p>
            </div>

            <!-- Метаинформация -->
            <div class="app-card mb-4">
                <div class="row g-4">
                    <div class="col-md-6">
                        <div class="fw-semibold text-muted small mb-1">Бюджет</div>
                        <div class="fw-bold fs-5 text-accent-orange">
                            ${order.priceFrom} – ${order.priceTo} ₽
                        </div>
                        <div class="mt-3">
                            <div class="fw-semibold text-muted small">Предложений</div>
                            <div class="fw-bold">12</div>
                        </div>
                    </div>
                    <div class="col-md-6 d-flex flex-column justify-content-end">
                        <div class="fw-semibold text-muted small">Дата создания</div>
                        <div class="fw-bold">${order.createdAt}</div>
                        <div class="mt-2">
                            <div class="fw-semibold text-muted small">Заказчик</div>
                            <a href="#" itemprop="url" class="fw-bold text-decoration-none">
                                ${profileService.getCurrentUserProfile(order.creator).firstName} ${profileService.getCurrentUserProfile(order.creator).lastName}
                            </a>
                            <span class="ms-2">
                                <i class="fas fa-star star-fill"></i> 4.8
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Форма подачи предложения -->
            <div class="app-card">
                <h4 class="fw-bold mb-3 border-bottom pb-2">Отправить предложение</h4>
                <form itemprop="potentialAction" itemscope itemtype="https://schema.org/ApplyAction">
                    <input type="hidden" itemprop="target" value="/offer/submit">
                    <div class="mb-3">
                        <label for="offerText" class="form-label fw-semibold">Текст предложения</label>
                        <textarea class="form-control" id="offerText" rows="4" placeholder="Опишите ваш подход к выполнению заказа, опыт работы и преимущества сотрудничества..." required></textarea>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="offerBudget" class="form-label fw-semibold">Предварительный бюджет (руб.)</label>
                            <input type="number" class="form-control" id="offerBudget" placeholder="100000" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="offerDeadline" class="form-label fw-semibold">Срок выполнения</label>
                            <input type="text" class="form-control" id="offerDeadline" placeholder="30 рабочих дней" required>
                        </div>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-primary-app btn-lg w-100 rounded-pill">
                            Ответить на заказ
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<#include "../../fragments/footer.ftl">

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
