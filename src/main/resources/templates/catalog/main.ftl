<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org">
<#include "../fragments/httpHeader.ftl">
<body>

<#include "../fragments/header.ftl">

<!-- Основной контент -->
<div class="container mt-5">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2>Каталог заказов</h2>
                        <div>
                            <span class="text-muted">Найдено заказов: <strong>${orderCount}</strong></span>
                        </div>
                    </div>
    <#include "../fragments/breadcrumbs.ftl">
    <div class="row">
        <!-- Сайдбар с фильтрами -->
        <div class="col-md-3">
            <#include "components/filters.ftl">
        </div>

        <!-- Основная область с заказами -->
        <div class="col-md-9">
            <div class="main-content">

                <!-- toolbar -->
                <#include "components/toolbar.ftl">
                <#include "../fragments/pagination.ftl">
                <!-- Список заказов -->
                <div id="ordersList">
                    <!-- Заказ 1 -->
                    <#list orders as order>
                    <div class="order-card">
                        <div class="d-flex justify-content-between align-items-start row">
                            <div class="col-md-9 right-border">
                                <h5>${order.title}</h5>
                                <p class="text-muted mb-2">${order.description}</p>
                                <div class="d-flex gap-3 mb-2">
                                    <span class="badge bg-primary">${order.category.name}</span>
                                    <span class="badge bg-secondary">${order.city.cityName}</span>
                                </div>
                                <div class="d-flex gap-3 mb-2">
                                    <h6 class="text-primary">от ${order.priceFrom} до ${order.priceTo} руб.</h6>
                                </div>
                            </div>
                            <div class="col-md-3">
<#--                                <h4 class="text-primary mb-1">от ${order.priceFrom} до ${order.priceTo} руб.</h4>-->
                                <img src="/assets/img/avatar.jpeg" alt="avatar"
                                     class="rounded-circle" width="36" height="36" style="object-fit: cover;">
                                Иванов Иван
                                <div class="d-flex gap-3 mt-2">
                                    Рейтинг: 4.8
                                </div>
                                <div class="d-flex gap-3 mt-2">
                                    Отзывы: 4
                                </div>
                                <div class="d-flex gap-3 mt-2">
                                    Заказов создано: 3
                                </div>
                                <div class="d-flex gap-3 mt-2">
                                    Нанято: 4 исполнителя
                                </div>
                            </div>
                        </div>
                        <div class="mt-3">
                            <a href="/catalog/order/${order.id}/preview" class="btn btn-outline-primary btn-sm">Подробнее</a>
                            <#if isAuthenticated>
                            <a href="#" class="btn btn-warning btn-sm">Скыть заказы</a>
                            <a href="#" class="btn btn-primary btn-sm">Следить за заказами</a>
<#--                            <button class="btn btn-sm btn-success">Откликнуться</button>-->
                            </#if>
                        </div>
                    </div>
                    </#list>

                    <!-- Дополнительные заказы будут добавляться динамически -->
                </div>
                <#include "../fragments/pagination.ftl">
            </div>
        </div>
    </div>
</div>

<#include "../fragments/footer.ftl">

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const collapsibles = document.querySelectorAll('.filter-collapsible');

            collapsibles.forEach(section => {
                const toggleLink = section.querySelector('.toggle-link');
                const summaryEl = section.querySelector('.filter-selected-summary');
                // ГЛАВНОЕ: если summaryEl нет — сразу выходим, чтобы не было ошибки
                if (!toggleLink || !summaryEl) return;

                updateSummary(section, summaryEl);

                toggleLink.addEventListener('click', (e) => {
                    e.preventDefault();
                    const isCollapsed = section.classList.contains('collapsed');
                    section.classList.toggle('collapsed', !isCollapsed);

                    const toggleText = section.querySelector('.toggle-text');
                    const toggleIcon = section.querySelector('.toggle-icon');
                    if (!toggleText || !toggleIcon) return;

                    if (isCollapsed) {
                        toggleText.textContent = 'Свернуть';
                        toggleIcon.innerHTML = '<path d="M12 19l-5-5 5-5z"/>';
                    } else {
                        toggleText.textContent = 'Развернуть';
                        toggleIcon.innerHTML = '<path d="M7 14l5-5 5 5z"/>';
                    }
                });

                const checkboxes = section.querySelectorAll('input[type="checkbox"]');
                checkboxes.forEach(cb => {
                    cb.addEventListener('change', () => updateSummary(section, summaryEl));
                });
            });

            function updateSummary(section, summaryEl) {
                const checkboxes = section.querySelectorAll('input[type="checkbox"]:checked');
                if (!summaryEl) return; // защита на всякий случай

                if (checkboxes.length === 0) {
                    const allItems = section.querySelectorAll('.form-check');
                    let text = '';
                    // for (let i = 0; i < Math.min(5, allItems.length); i++) {
                    //     const label = allItems[i].querySelector('label');
                    //     if (label) {
                    //         if (text) text += ', ';
                    //         text += label.textContent.trim();
                    //     }
                    // }
                    summaryEl.textContent = text ? text : 'Ничего не выбрано';
                } else {
                    let names = [];
                    checkboxes.forEach(cb => {
                        const label = cb.closest('.form-check')?.querySelector('label');
                        if (label) names.push(label.textContent.trim());
                    });
                    summaryEl.textContent = names.join(', ');
                }
            }
        });


    </script>
</body>
</html>
