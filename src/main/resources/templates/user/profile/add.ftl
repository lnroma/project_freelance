<!DOCTYPE html>
<html lang="ru">
<#include "../../fragments/httpHeader.ftl">
<body class="bg-light">
<#include "../../fragments/header.ftl">

<main class="container mt-5">
    <div class="row justify-content-center">
        <div class="">
            <div class="card dashboard-card border-0 shadow-sm h-100">
                <div class="card-header bg-white py-3" style="border-bottom: 1px solid var(--border); border-radius: 0.75rem 0.75rem 0 0;">
                    <h5 class="mb-0 fw-bold text-dark">
                        <i class="fas fa-user-edit me-2 text-primary"></i>Редактировать профиль
                    </h5>
                </div>
                <div class="card-body p-4">

                    <!-- enctype обязателен для загрузки фото -->
                    <form action="/user/profile/add" method="post" enctype="multipart/form-data">
                        <#-- CSRF: предполагаем, что в модели есть csrfToken (или используй Spring Security CSRF через hiddenInput) -->
                        <input type="hidden" name="${csrf.parameterName!'_csrf'}" value="${csrf.token!''}">

                        <div class="row g-4">
                            <!-- Левая колонка: основные данные -->
                            <div class="col-md-6">
                                <!-- Никнейм -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Никнейм</label>
                                    <input type="text"
                                           class="form-control form-control-sm"
                                           name="nickname"
                                           value="${profileForm.nickname!currentProfile.nickName!""}"
                                           placeholder="Придумайте никнейм"
                                           required>
                                    <#if errors?? && errors.nickname?has_content>
                                        <div class="text-danger small mt-1">${errors.nickname}</div>
                                    </#if>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Имя</label>
                                    <input type="text"
                                           class="form-control form-control-sm"
                                           name="firstName"
                                           value="${profileForm.firstName!currentProfile.firstName!''}"
                                           placeholder="Ваше имя"
                                           required>
                                    <#if errors?? && errors.firstName?has_content>
                                        <div class="text-danger small mt-1">${errors.firstName}</div>
                                    </#if>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Фамилия</label>
                                    <input type="text"
                                           class="form-control form-control-sm"
                                           name="lastName"
                                           value="${profileForm.lastName!currentProfile.lastName!''}"
                                           placeholder="Ваша фамилия"
                                           required>
                                    <#if errors?? && errors.lastName?has_content>
                                        <div class="text-danger small mt-1">${errors.lastName}</div>
                                    </#if>
                                </div>

                                <!-- Пол -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Пол</label>
                                    <select class="form-select form-select-sm" name="gender">
                                        <option value="" selected disabled>Выберите пол</option>
                                        <option value="MALE" <#if profileForm.gender!currentProfile.gender!'MALE' == 'MALE'>selected</#if>>Мужской</option>
                                        <option value="FEMALE" <#if profileForm.gender!currentProfile.gender!'MALE' == 'FEMALE'>selected</#if>>Женский</option>
                                    </select>
                                    <#if errors?? && errors.gender?has_content>
                                        <div class="text-danger small mt-1">${errors.gender}</div>
                                    </#if>
                                </div>

                                <!-- Дата рождения -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Дата рождения</label>
                                    <input type="date"
                                           class="form-control form-control-sm"
                                           name="birthDate"
                                           value="${profileForm.birthDate!currentProfile.birthDate!''}"
                                           max="${todayDate!''}">
                                    <#if errors?? && errors.birthDate?has_content>
                                        <div class="text-danger small mt-1">${errors.birthDate}</div>
                                    </#if>
                                </div>

                                <!-- Тип отношений -->
                            </div>

                            <!-- Правая колонка: город, роль, стоимость, фото, контакты -->
                            <div class="col-md-6">
                                <!-- Город -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Город</label>
                                    <select class="form-select form-select-sm" name="cityId">
                                        <option value="" selected disabled>Выберите город</option>
                                        <#list cities as city>
                                            <option value="${city.id}" <#if (profileForm.cityId?? && profileForm.cityId == city.id) || (currentUser.cityId?? && currentUser.cityId == city.id) >selected</#if>>
                                                ${city.cityName}
                                            </option>
                                        </#list>
                                    </select>
                                    <#if errors?? && errors.cityId?has_content>
                                        <div class="text-danger small mt-1">${errors.cityId}</div>
                                    </#if>
                                </div>

                                <!-- Роль (только если разрешено) -->
                                <#if hasRoleEditor?? && hasRoleEditor == true>
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold text-dark">Роль</label>
                                        <select class="form-select form-select-sm" name="role">
                                            <option value="USER" <#if profileForm.role == 'USER'>selected</#if>>Пользователь</option>
                                            <option value="EXECUTOR" <#if profileForm.role == 'EXECUTOR'>selected</#if>>Исполнитель</option>
                                            <option value="ADMIN" <#if profileForm.role == 'ADMIN'>selected</#if>>Администратор</option>
                                        </select>
                                    </div>

                                    <!-- Стоимость в месяц -->
                                    <div class="mb-3">
                                        <label class="form-label fw-semibold text-dark">Стоимость в месяц, ₽</label>
                                        <input type="number" step="0.01"
                                               class="form-control form-control-sm"
                                               name="costPerMonth"
                                               value="${profileForm.costPerMonth!0}"
                                               placeholder="Например, 15000">
                                        <#if errors?? && errors.costPerMonth?has_content>
                                            <div class="text-danger small mt-1">${errors.costPerMonth}</div>
                                        </#if>
                                    </div>
                                <#else>
                                    <!-- Скрытые поля, чтобы бэкенд не терял значения при POST -->
                                    <input type="hidden" name="role" value="${profileForm.role!'USER'}">
                                    <input type="hidden" name="costPerMonth" value="${profileForm.costPerMonth!0}">
                                </#if>

                                <!-- Фото профиля -->
                                <div class="mb-4">
                                    <label class="form-label fw-semibold text-dark">Фото профиля</label>
                                    <input type="file"
                                           class="form-control form-control-sm"
                                           name="photo"
                                           accept="image/*"
                                           <#if errors?? && errors.photo?has_content>>
                                               <div class="text-danger small mt-1">${errors.photo}</div>
                                           </#if>>
                                    <small class="text-muted small d-block">Допустимы JPG/PNG, до 5 МБ.</small>
                                </div>

                                <!-- Email (только для отображения) -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Email</label>
                                    <input type="email"
                                           class="form-control form-control-sm bg-light border-0 text-muted"
                                           value="${profileForm.email!currentUser.email!''}"
                                           readonly>
                                </div>

                                <!-- Телефон -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold text-dark">Телефон</label>
                                    <input type="tel"
                                           class="form-control form-control-sm disabled"
                                           name="phoneNumber"
                                           value="${profileForm.phoneNumber!currentUser.phoneNumber!''}"
                                           placeholder="+7 (999) 000-00-00">
                                    <#if errors?? && errors.phoneNumber?has_content>
                                        <div class="text-danger small mt-1">${errors.phoneNumber}</div>
                                    </#if>
                                </div>
                            </div>
                        </div>

                        <hr class="my-4 border-0" style="background-color: var(--border); height: 1px;">

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <a href="/dashboard" class="btn btn-outline-secondary btn-lg px-4 me-md-2">Отмена</a>
                            <button type="submit" class="btn btn-primary btn-lg">
                                Сохранить профиль
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<#include "../../fragments/footer.ftl">
</body>
</html>
