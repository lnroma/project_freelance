<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org">
<#include "../../fragments/httpHeader.ftl">
<body>

<#include "../../fragments/header.ftl">

<!-- Форма создания заказа -->
<div class="container">
<div class="d-flex justify-content-between mt-5 align-items-center mb-4">
                            <h2>Создать новый заказ</h2>
                            <div>
                                <span class="text-muted">Уже создано: <strong>12929</strong> заказов</span>
                            </div>
                        </div>
    <div class="create-order-form bg-white  shadow-sm" style="padding: 40px; border-radius: 8px;">

        <form th:action="@{/create-order}" method="post" enctype="multipart/form-data">
            <!-- CSRF Token (обязательно для Spring Security) -->
            <input type="hidden" name="${csrf.parameterName}" value="${csrf.token}" />

            <div class="row g-4">
                <!-- ЛЕВАЯ КОЛОНКА -->
                <div class="col-md-6">
                    <!-- Название заказа/проекта -->
                    <div class="mb-3">
                        <label for="orderTitle" class="form-label fw-semibold">Название заказа/проекта</label>
                        <input type="text" class="form-control" id="orderTitle" name="title" placeholder="Например: Разработка веб‑сайта для интернет‑магазина" required>
                    </div>

                    <!-- Описание что сделать -->
                    <div class="mb-3">
                        <label for="orderDescription" class="form-label fw-semibold">Описание задачи</label>
                        <textarea class="form-control" id="orderDescription" name="description" rows="5" placeholder="Подробно опишите требования к заказу, задачи и ожидаемый результат..." required></textarea>
                    </div>

                    <!-- Прикрепление файла с ТЗ -->
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Техническое задание (при наличии)</label>
                        <div class="file-upload" id="fileUploadArea" style="border: 2px dashed #ced4da; padding: 20px; text-align: center; border-radius: 6px; cursor: pointer;">
                            <i class="bi bi-file-earmark-arrow-up me-2"></i>
                            <p class="mb-0 fw-medium">Перетащите файл сюда или нажмите для выбора</p>
                            <small class="text-muted">Поддерживаемые форматы: PDF, DOC, DOCX, TXT. Макс. размер: 10 МБ</small>
                            <input type="file" id="technicalTask" name="technicalTask" class="d-none" accept=".pdf,.doc,.docx,.txt">
                        </div>
                        <div id="fileName" class="mt-2 text-muted small"></div>
                    </div>
                </div>

                <!-- ПРАВАЯ КОЛОНКА -->
                <div class="col-md-6">
                    <!-- Желаемый бюджет -->
                    <div class="mb-4">
                        <label class="form-label fw-semibold">Желаемый бюджет (руб.)</label>
                        <div class="budget-range d-flex gap-3">
                            <div class="flex-grow-1">
                                <label for="budgetFrom" class="form-label small text-muted">От</label>
                                <input type="number" class="form-control" id="budgetFrom" name="priceFrom" placeholder="5000" min="0" required>
                            </div>
                            <div class="flex-grow-1">
                                <label for="budgetTo" class="form-label small text-muted">До</label>
                                <input type="number" class="form-control" id="budgetTo" name="priceTo" placeholder="150000" min="0" required>
                            </div>
                        </div>
                    </div>

                    <!-- Желаемый срок выполнения -->
                    <div class="mb-4">
                        <label for="deadline" class="form-label fw-semibold">Срок выполнения</label>
                        <input type="date" class="form-control" id="deadline" name="deadlineAt" required>
                        <small class="text-muted">Минимальная дата — сегодня</small>
                    </div>

                    <!-- Город -->
                    <div class="mb-4">
                        <label for="city" class="form-label fw-semibold">Город</label>
                        <select class="form-select" id="city" name="cityId" required>
                            <option value="" disabled selected>Выберите город</option>
                            <#list cities as city>
                                <option value="${city.id}">${city.cityName}</option>
                            </#list>
                        </select>
                    </div>

                    <!-- Город -->
                    <div class="mb-0">
                        <label for="category" class="form-label fw-semibold">Категория заказа</label>
                        <select class="form-select" id="category" name="categoryId" required>
                            <option value="" disabled selected>Выберите категорию</option>
                            <#list orderCategories as category>
                                <option value="${category.id}">${category.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Кнопка сохранения (на всю ширину) -->
            <div class="mt-4 pt-3 border-top">
                <button type="submit" class="btn btn-primary btn-lg shadow-sm">Сохранить заказ</button>
            </div>
        </form>
    </div>
</div>

<#include "../../fragments/footer.ftl">

<!-- Bootstrap Icons (обязательно для иконки файла) -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const fileUploadArea = document.getElementById('fileUploadArea');
        const fileInput = document.getElementById('technicalTask');
        const fileNameDisplay = document.getElementById('fileName');

        // Обработчик клика по области загрузки файла
        fileUploadArea.addEventListener('click', function() {
            fileInput.click();
        });

        // Обработчик перетаскивания файла (dragover)
        fileUploadArea.addEventListener('dragover', function(e) {
            e.preventDefault();
            fileUploadArea.style.borderColor = '#667eea';
            fileUploadArea.style.backgroundColor = '#f0f4ff';
        });

        // Обработчик ухода курсора с области (dragleave)
        fileUploadArea.addEventListener('dragleave', function() {
            fileUploadArea.style.borderColor = '#ced4da';
            fileUploadArea.style.backgroundColor = 'white';
        });

        // Обработчик падения файла (drop)
        fileUploadArea.addEventListener('drop', function(e) {
            e.preventDefault();
            fileUploadArea.style.borderColor = '#ced4da';
            fileUploadArea.style.backgroundColor = 'white';

            if (e.dataTransfer.files.length) {
                fileInput.files = e.dataTransfer.files;
                updateFileName(e.dataTransfer.files[0].name);
            }
        });

        // Обработчик выбора файла через диалог
        fileInput.addEventListener('change', function() {
            if (fileInput.files.length > 0) {
                updateFileName(fileInput.files[0].name);
            }
        });

        // Функция обновления отображения имени файла
        function updateFileName(fileName) {
            fileNameDisplay.innerHTML = `
                <i class="bi bi-file-alt me-2"></i>
                Выбран файл: <strong>fileName</strong>
            `;
        }

        // Валидация формы перед отправкоgnsй
        document.querySelector('form').addEventListener('submit', function(e) {
            const budgetFrom = document.getElementById('budgetFrom').value;
            const budgetTo = document.getElementById('budgetTo').value;

            // Проверка, что поля заполнены
            if (!budgetFrom || !budgetTo) return;

            if (parseInt(budgetFrom) > parseInt(budgetTo)) {
                e.preventDefault();
                alert('Ошибка: бюджет "От" не может быть больше бюджета "До".');
                return false;
            }

            // Дополнительная валидация может быть добавлена здесь
        });

        // Установка минимальной даты (сегодня) для поля срока выполнения
        const today = new Date().toISOString().split('T')[0];
        const deadlineInput = document.getElementById('deadline');
        if (deadlineInput) {
            deadlineInput.setAttribute('min', today);
        }
    });
</script>
</body>
</html>
