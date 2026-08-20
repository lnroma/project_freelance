<!DOCTYPE html>
<html lang="ru">
<#include "../fragments/httpHeader.ftl">
<body>
<!-- Header -->
<#include "../fragments/header.ftl">

<!-- Основной контент -->
<section class="bg-gradient py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <div class="card login-card p-4">
                    <!-- Логотип и заголовок -->
                    <div class="text-center mb-4">
                        <i class="fas fa-user-lock logo-heart"></i>
                        <h2 class="mt-2">Вход в систему</h2>
                        <p class="text-muted">Введите свои учётные данные</p>
                    </div>

                    <!-- Сообщение об ошибке -->
                    <#if RequestParameters?? >
                        <#if RequestParameters.error??>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            Неверное имя пользователя или пароль
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                            </#if>
                    </#if>

                    <!-- Форма входа -->
                    <form action="/login" method="post">
                        <!-- Поле email -->
                        <div class="mb-3">
                            <label for="username" class="form-label">Email</label>
                            <input type="email"
                                   class="form-control form-control-lg"
                                   id="username"
                                   name="username"
                                   placeholder="Введите ваш email"
                                   required>
                        </div>

                        <!-- Поле пароля -->
                        <div class="mb-4">
                            <label for="password" class="form-label">Пароль</label>
                            <input type="password"
                                   class="form-control form-control-lg"
                                   id="password"
                                   name="password"
                                   placeholder="Введите пароль"
                                   required>
                        </div>

                        <!-- Чекбокс «Запомнить меня» -->
                        <div class="mb-4 form-check">
                            <input type="checkbox"
                                   class="form-check-input"
                                   id="remember-me"
                                   name="remember-me">
                            <label class="form-check-label" for="remember-me">Запомнить меня</label>
                        </div>
                        <!-- Кнопка входа -->
                        <button type="submit" class="btn btn-primary btn-lg w-100">
                            Войти
                            <i class="fas fa-arrow-right ms-2"></i>
                        </button>

<#--                        <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}" >-->
                        <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}" />
                    </form>
                    <!-- Ссылки -->
                    <div class="text-center mt-4">
                        <a href="/registration" class="text-decoration-none">Нет аккаунта? Зарегистрироваться</a>
                        <br>
                        <a href="/forgot-password" class="text-decoration-none text-muted small">Забыли пароль?</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<#include "../fragments/footer.ftl">

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>