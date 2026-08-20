<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org">
<#include "fragments/httpHeader.ftl">
<div></div>
<body>
<#include "fragments/header.ftl">

<!-- Блок категорий услуг -->
<section class="categories mt-5 mb-5">
    <div class="container">
        <h2 class="text-center mb-5">Популярные категории услуг</h2>
        <div class="row">
            <#list categories as category>
            <!-- Категория 1 -->
            <div class="col-md-4 col-lg-2 mb-4">
                <div class="card category-card p-3" style="min-height: 300px">
                    <b class="card-title">${category.name}</b>
                    <p>${category.description}</p>
                    <small class="text-muted">1 245 заказов</small>
                </div>
            </div>
            </#list>
        </div>
    </div>
</section>

<!-- Блок топовых исполнителей -->
<section class="top-performers">
    <div class="container">
        <h2 class="text-center mb-5">Топовые исполнители</h2>
        <div class="row">
            <!-- Исполнитель 1 -->
            <div class="col-md-4 col-lg-2 mb-4">
                <div class="card performer-card text-center">
                    <img src="https://via.placeholder.com/100" class="rounded-circle mx-auto mt-3" alt="Исполнитель 1" width="100" height="100">
                    <div class="card-body">
                        <h6 class="card-title">Иван Петров</h6>
                        <div class="d-flex justify-content-center align-items-center mb-2">
                            <i class="fas fa-star text-warning"></i>
                            <span class="ms-1">4.9</span>
                        </div>
                        <small class="text-muted">124 отзыва</small>
                    </div>
                </div>
            </div>
            <!-- Остальные 9 исполнителей (аналогично) -->
            <div class="col-md-4 col-lg-2 mb-4">
                <div class="card performer-card text-center">
                    <img src="https://via.placeholder.com/100" class="rounded-circle mx-auto mt-3" alt="Исполнитель 2" width="100" height="100">
                    <div class="card-body">
                        <h6 class="card-title">Мария Сидорова</h6>
                        <div class="d-flex justify-content-center align-items-center mb-2">
                            <i class="fas fa-star text-warning"></i>
                            <span class="ms-1">4.8</span>
                        </div>
                        <small class="text-muted">98 отзывов</small>
                    </div>
                </div>
            </div>
            <!-- ... ещё 8 карточек исполнителей ... -->
        </div>
    </div>
</section>

<!-- Блок последних заказов на бирже -->
<section class="recent-orders">
    <div class="container">
        <h2 class="text-center mb-4">Последние заказы на бирже</h2>
        <div class="row">
            <div class="col-12">
                <!-- Заказ 1 -->
                <div class="order-panel">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h5>Ремонт квартиры под ключ</h5>
                            <p class="text-muted mb-1">Требуется выполнить ремонт трёхкомнатной квартиры в новостройке</p>
                            <small>Бюджет: 500 000 руб.</small>
                        </div>
                        <div class="text-end">
                            <span class="badge bg-primary mb-2">Новый</span>
                            <p><small>Опубликовано: сегодня</small></p>
                        </div>
                    </div>
                </div>
                <!-- Заказ 2 -->
                <div class="order-panel">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h5>Разработка веб‑сайта</h5>
                            <p class="text-muted mb-1">Нужен современный сайт для интернет‑магазина</p>
                            <small>Бюджет: 150 000 руб.</small>
                        </div>
                        <div class="text-end">
                            <span class="badge bg-success mb-2">В работе</span>
                            <p><small>Опубликовано: вчера</small></p>
                        </div>
                    </div>
                </div>
                <!-- Ещё 8 заказов (аналогично) -->
            </div>
        </div>
    </div>
</section>


<#include "fragments/footer.ftl">

<!-- Bootstrap JS -->
</body>
</html>