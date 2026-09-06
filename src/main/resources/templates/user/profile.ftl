<!DOCTYPE html>
<html lang="ru">
<#include "../fragments/httpHeader.ftl">
<body>

<#include "../fragments/header.ftl">
<#include "/spring.ftl"> <!-- Обязательно для CSRF -->
<div class="container mt-5">
         <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2>Профиль пользователя </h2>
                                <div>
                                    <span class="text-muted">Активность пользователя: <strong>72%</strong></span>
                                </div>
                            </div>
    <#include "../fragments/breadcrumbs.ftl">
    <div class="row">
        <!-- Левая колонка: Статистика и Инфо -->
        <div class="col-md-3 mb-4">
            <div class="bg-white p-3 rounded shadow-sm">
                <h5>Аватар пользователя</h5>
                <p>
                    <img src="/assets/img/avatar.jpeg" style="height: 16.5rem" class="rounded-circle" />
                    <a href="/profile/avatar/change" class="btn btn-primary mt-3">Сменить аватар</a>
                </p>
                <h5 class="border-bottom pb-2">О себе</h5>
                <p>${currentUser.bio!'Нет информации о себе'}</p>

                <hr>
                <div class="d-flex justify-content-between text-center mt-3">
                    <div class="stats-box">
                        <h4 class="fw-bold">${posts?size!0}</h4>
                        <small>Записей</small>
                    </div>
                    <div class="stats-box">
                        <h4 class="fw-bold">12</h4> <!-- Можно добавить счетчик подписчиков -->
                        <small>Подписчиков</small>
                        <#if currentUser.id != authUser.getEntity().id >
                            <a href="/user/profile/add/favorite" class="btn btn-primary">Подписаться</a>
                        </#if>
                    </div>
                    <div class="stats-box">
                        <h4 class="fw-bold">8</h4>
                        <small>Подписок</small>
                        <#if currentUser.id == authUser.getEntity().id >
                            <a href="/user/profile/my/favorites" class="btn btn-primary">Мои подписчики</a>
                        </#if>
                    </div>
                </div>
                <div class="d-flex mt-3">
                    <a href="/user/profile/add" class="btn btn-primary">Редактировать профиль</a>
                </div>
            </div>
        </div>

        <!-- Правая колонка: Лента и Форма поста -->
        <div class="col-md-9">

            <!-- Форма создания поста (только для своего профиля) -->
            <#if currentUser.id == authUser.getEntity().id>
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <form action="/profile/post" method="post">
                            <#-- CSRF токен подставится автоматически благодаря spring.ftl -->
                            <div class="form-floating mb-3">
                                <textarea class="form-control" id="content" name="content" rows="3" placeholder="Что у вас нового?" required></textarea>
                                <label for="content">Что у вас нового?</label>
                            </div>
                            <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}" />

                            <button type="submit" class="btn btn-primary fw-bold px-4">Опубликовать</button>
                        </form>
                    </div>
                </div>
            </#if>

            <!-- Лента постов -->
            <h4 class="mb-3">Лента</h4>
            <#include "../fragments/pagination.ftl">
            <#list posts as post>
                <div class="post-card">
                    <div>
                        <div class="d-flex align-items-center">
                            <!-- Аватар автора поста (можно подтянуть из БД по ID) -->
<#--                            <img src="https://ui-avatars.com/api/?name=${post.authorUserId}&background=random" class="me-3 rounded-circle" width="40" height="40">-->
                            <div>
                                <h5 class="mb-0 fw-bold">Автор поста #${post.id}</h5>
                                <#-- Тут можно сделать JOIN запрос, чтобы получить имя автора, пока заглушка -->
                                <small class="text-muted">${post.createdAt.toString()}</small>
                            </div>
                        </div>

                        <hr style="border-top: 1px solid #eee;">

                        <p class="lead text-break">${post.content}</p>

                        <div class="post-meta">
                        <span>
                            <button class="btn-icon"><i class="far fa-thumbs-up"></i> Нравится</button>
                            <button class="btn-icon"><i class="far fa-comment"></i> Комментарий</button>
                        </span>
                            <#if currentUser.id == authUser.getEntity().id && post.getAuthor().id == currentUser.id>
                                <span>
                            <button class="btn-icon text-danger"><i class="fas fa-trash"></i> Удалить</button>
                        </span>
                            </#if>
                        </div>
                    </div>
                </div>
            </#list>
            <#include "../fragments/pagination.ftl">
            <#if posts?size == 0>
                <div class="text-center py-5 text-muted">
                    <i class="fas fa-comments fa-3x mb-3"></i>
                    <p>Пока нет записей. Расскажите о себе!</p>
                </div>
            </#if>
        </div>
    </div>
</div>

<#include "../fragments/footer.ftl">
<script>
    // Простая валидация формы перед отправкой (опционально)
    document.querySelector('form[action="/profile/post"]').addEventListener('submit', function(e) {
        const textarea = this.querySelector('[name="content"]');
        if (!textarea.value.trim()) {
            e.preventDefault();
            alert('Запись не может быть пустой');
            return false;
        }
    });
</script>
</body>
</html>