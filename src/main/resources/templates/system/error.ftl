<#-- templates/error.ftl -->
<!DOCTYPE html>
<html lang="ru">
<#include "../fragments/httpHeader.ftl">
<body class="d-flex flex-column min-vh-100">
    <#include "../fragments/header.ftl">

<main class="flex-grow-1 container text-center py-5">
    <h1 class="display-1 fw-bold text-danger">404</h1>
    <p class="lead">К сожалению, запрошенная страница не найдена.</p>

    <#if authentication?? && authentication.isAuthenticated()>
        <a href="/chat" class="btn btn-primary btn-lg">Вернуться в чат</a>
    <#else>
        <a href="/login" class="btn btn-outline-primary btn-lg">Войти</a>
        <a href="/register" class="btn btn-secondary btn-lg ms-2">Регистрация</a>
    </#if>
</main>

<footer>
    <#include "../fragments/footer.ftl">
</footer>
</body>
</html>
