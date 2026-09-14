<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org">
<#include "fragments/httpHeader.ftl">
<div></div>
<body>
<#include "fragments/header.ftl">
<section class="mt-5 mb-5 row" style="background: #080838">
    <img src="/img/1.jpeg" class="col-md-4" />
    <img src="/img/2.jpeg" class="col-md-4" />
    <img src="/img/3.jpeg" class="col-md-4" />
</section>
<section class="mt-5 mb-5 " style="background-image: url('/img/background.jpeg'); color: white">
    <div class="container pt-3">
        <h2 class="text-center mb-5 mt-5" style="font-weight: 1000">Мы инновации</h2>
        <div class="row">
            <div class="col-md-6 mb-4" style="background: #0f0f47; font-weight: 400">
                Мы не просто фриланс площадка, мы целое сообщество профессиональных разработчиков. Мы не занимаемся фрилансом
                мы работаем на вас.
            </div>
            <div class="col-md-6 mb-4" style="background: #0f0f47; font-weight: 400">
                Процес достижения результата очень прост и максимально быстрый, ведь время деньги:
                <ul>
                    <li>Пройдите простую <a href="/registration">регистрацию</a> состоящую из двух шагов</li>
                    <li><a href="/login">Авторизуйтесь</a> </li>
                    <li>Разместите свой заказ с помощью формы создания заказа</li>
                    <li>Внесите взнос, всего 10% стоимости заказа или сколько не жалко</li>
                    <li>Для заказа будет назначен ответственный который возьмет на себя контроль полного цикла выполнения от разработки до тестирования
                    и призентации результата вам
                    </li>
                    <li>Вам останется лишь оплатить свой заказ полностью и получить результат на руки, максимально сокращенно время от создания вашего заказа до его выполнения
                    при этом не требуется ваш постоянный контроль, 3 минуты создать заказ, 5 минут ответить на вопросы менеджера, и 1 минута скачать результат или получить доступы к git репозиторию.
                    </li>
                </ul>
            </div>
        </div>
    </div>
</section>
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


<#include "fragments/footer.ftl">

<!-- Bootstrap JS -->
</body>
</html>