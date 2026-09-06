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
                            <span class="text-muted">Найдено заказов: <strong>${catalogPage.orderCounts}</strong></span>
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
                    <div class="table-responsive">
                        <table class="table table-hover align-middle order-table">
                            <thead class="table-dark">
                                <tr style="font-size: 0.75rem">
                                    <th scope="col">Название</th>
                                    <th scope="col">Описние</th>
                                    <th scope="col">Дата</th>
                                    <th scope="col">Цена от</th>
                                    <th scope="col">Цена до</th>
                                    <th scope="col">Заказчик</th>
                                    <th scope="col">Действия</th>
                                </tr>
                            </thead>
                            <tbody>
                            <#list catalogPage.orders as order>
                                <tr>
                                    <td>${order.title}</td>
                                    <td>${order.description}</td>
                                    <td>${order.createdAtDate}<br/>${order.createdAtTime}</td>
                                    <td>${order.priceFrom}</td>
                                    <td>${order.priceTo}</td>
                                    <td>${order.author.firstName} ${order.author.lastName}</td>
                                    <td>Подробнее | Ответить</td>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
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
