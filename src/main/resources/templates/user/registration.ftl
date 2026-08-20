<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org">
<#include "../fragments/httpHeader.ftl">
<body>

<#include "../fragments/header.ftl">

<!-- Форма регистрации -->
<div class="container mt-5">
     <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2>Регистрация</h2>
                            <div>
                                <span class="text-muted">Уже зарегистрированно: <strong>14700</strong> пользователей</span>
                            </div>
                        </div>
    <div class="registration-form bg-white shadow-sm" style="padding: 40px; border-radius: 8px;">
        <form th:action="@{/register}" method="post">
            <!-- CSRF Token (обязательно для Spring Security) -->
            <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}" />

            <div class="row g-4">
                <!-- ЛЕВАЯ КОЛОНКА -->
                <div class="col-md-6">
                    <!-- Email -->
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email</label>
                        <input type="email" class="form-control" id="email" name="email" required placeholder="example@mail.ru">
                    </div>

                    <!-- Телефон и верификация -->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Номер телефона</label>
                        <div class="input-group">
                            <input type="tel" class="form-control" id="phone" name="phoneNumber" required placeholder="+7 (999) 000-00-00"/>
                            <button type="button" class="btn btn-outline-primary" id="verifyPhone">Подтвердить</button>
                        </div>

                        <!-- Блок верификации (скрыт по умолчанию) -->
                        <div id="phoneVerification" class="phone-verification mt-2" style="display: none;">
                            <label for="code" class="form-label small text-muted">Введите 4‑значный код</label>
                            <div class="d-flex gap-2">
                                <input type="text" class="form-control" id="code" maxlength="4" placeholder="1234" style="width: 100px;">
                                <button type="button" class="btn btn-sm btn-primary align-self-end" id="submitCode">OK</button>
                            </div>

                            <div id="verificationSuccess" class="verification-success d-flex align-items-center mt-2 text-success" style="display: none;">
                                <i class="bi bi-check-circle-fill me-2"></i>
                                <span>Номер подтверждён</span>
                            </div>
                        </div>
                    </div>

                    <!-- Пароль -->
                    <div class="mb-3">
                        <label for="password" class="form-label fw-semibold">Пароль</label>
                        <input type="password" class="form-control" id="password" name="password" required minlength="6" placeholder="••••••••">
                    </div>

                    <!-- Подтверждение пароля -->
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label fw-semibold">Подтверждение пароля</label>
                        <input type="password" class="form-control" id="confirmPassword" required minlength="6" placeholder="••••••••">
                    </div>
                </div>

                <!-- ПРАВАЯ КОЛОНКА -->
                <div class="col-md-6">
                    <!-- Выбор роли -->
                    <div class="mb-4">
                        <label class="form-label fw-semibold">Роль пользователя</label>
                        <div class="d-flex gap-4 border p-3 rounded bg-light">
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="role" id="customer" value="customer" checked onchange="toggleRoleFields()">
                                <label class="form-check-label" for="customer">Заказчик</label>
                            </div>
                            <div class="form-check ms-auto">
                                <input class="form-check-input" type="radio" name="role" id="performer" value="performer" onchange="toggleRoleFields()">
                                <label class="form-check-label" for="performer">Исполнитель</label>
                            </div>
                        </div>
                    </div>

                    <!-- Город (автозаполнение) -->
                    <div class="mb-4">
                        <label class="form-label fw-semibold">Ближайший город</label>
                        <div class="position-relative">
                            <input type="text" class="form-control" id="citySearch" placeholder="Начните вводить название...">
                            <div id="citySuggestions" class="position-absolute w-100 bg-white border shadow-sm rounded" style="top: 100%; z-index: 1050; display: none; max-height: 200px; overflow-y: auto;"></div>
                        </div>
                        <input type="hidden" id="selectedCity" name="city">
                    </div>

                    <!-- Промо описание (только для исполнителя) -->
                    <div class="mb-3" id="promoDescription" style="display: none;">
                        <label for="promo" class="form-label fw-semibold">Промо описание</label>
                        <textarea class="form-control" id="promo" name="promo" rows="3" placeholder="Расскажите о себе, преимуществах и опыте..."></textarea>
                    </div>

                    <!-- Навыки (только для исполнителя) -->
                    <div class="mb-3" id="skillsDescription" style="display: none;">
                        <label for="skills" class="form-label fw-semibold">Основные навыки</label>
                        <textarea class="form-control" id="skills" name="skills" rows="3" placeholder="Перечислите ключевые навыки..."></textarea>
                    </div>

                    <!-- Ставка в час -->
                    <div class="mb-0">
                        <label for="rate" class="form-label fw-semibold">Ставка в час (руб.)</label>
                        <input type="number" class="form-control" id="rate" name="costPerMonth" min="0" placeholder="500">
                        <small class="text-muted">Указывается ориентировочно</small>
                    </div>
                </div>
            </div>

            <!-- Кнопка регистрации (на всю ширину) -->
            <div class="mt-4 pt-3 border-top">
                <button type="submit" class="btn btn-primary btn-lg">Зарегистрироваться</button>
            </div>
        </form>
    </div>
</div>

<#include "../fragments/footer.ftl">

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const promoDescription = document.getElementById('promoDescription');
        const skillsDescription = document.getElementById('skillsDescription');
        const verifyPhoneBtn = document.getElementById('verifyPhone');
        const phoneVerification = document.getElementById('phoneVerification');
        const verificationSuccess = document.getElementById('verificationSuccess');
        const submitCodeBtn = document.getElementById('submitCode');
        const citySearch = document.getElementById('citySearch');
        const citySuggestions = document.getElementById('citySuggestions');
        const selectedCity = document.getElementById('selectedCity');

        // Массив крупных городов России
        const cities = [
            'Москва', 'Санкт‑Петербург', 'Новосибирск', 'Екатеринбург',
            'Казань', 'Нижний Новгород', 'Челябинск', 'Самара',
            'Омск', 'Ростов‑на‑Дону', 'Уфа', 'Красноярск',
            'Воронеж', 'Пермь', 'Волгоград'
        ];

        // Переключение полей для исполнителя
        window.toggleRoleFields = function() {
            const performerRadio = document.getElementById('performer');
            if (performerRadio.checked) {
                promoDescription.style.display = 'block';
                skillsDescription.style.display = 'block';
            } else {
                promoDescription.style.display = 'none';
                skillsDescription.style.display = 'none';
            }
        };

        // Подтверждение номера телефона
        verifyPhoneBtn.addEventListener('click', function() {
            phoneVerification.style.display = 'block';
            // В реальном проекте здесь будет запрос к API
            alert('Код подтверждения (1234) отправлен на указанный номер телефона!');
        });

        // Проверка кода подтверждения
        submitCodeBtn.addEventListener('click', function() {
            const code = document.getElementById('code').value;
            if (code === '1234') {
                verificationSuccess.style.display = 'flex';
                // Скрываем поле ввода кода после успеха
                document.querySelector('#phoneVerification .d-flex').style.display = 'none';
            } else {
                alert('Неверный код подтверждения. Попробуйте ещё раз.');
            }
        });

        // Автозаполнение городов
        citySearch.addEventListener('input', function() {
            const searchTerm = citySearch.value.toLowerCase();
            citySuggestions.innerHTML = '';
            citySuggestions.style.display = searchTerm.length > 0 ? 'block' : 'none';

            if (searchTerm.length > 0) {
                const filteredCities = cities.filter(city =>
                    city.toLowerCase().includes(searchTerm)
                );

                filteredCities.forEach(city => {
                    const div = document.createElement('div');
                    div.className = 'p-2 border-bottom cursor-pointer';
                    div.textContent = city;
                    div.style.cursor = 'pointer';
                    div.addEventListener('click', function() {
                        citySearch.value = city;
                        selectedCity.value = city;
                        citySuggestions.style.display = 'none';
                    });
                    citySuggestions.appendChild(div);
                });
            }
        });

        // Закрытие списка городов при клике вне поля
        document.addEventListener('click', function(e) {
            if (!citySearch.contains(e.target) && !citySuggestions.contains(e.target)) {
                citySuggestions.style.display = 'none';
            }
        });
    });
</script>
</body>
</html>
