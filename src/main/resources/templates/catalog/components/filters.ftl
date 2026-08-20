<form method="get" action="/catalog">
    <div class="sidebar" style="padding: 15px; padding-top:0px!important;background: #f8f9fa;">

        <!-- Цена (без изменений) -->
        <div class="filter-section mb-4">
            <h5 class="filter-title fw-bold">
                <svg width="16" height="16" fill="currentColor" viewBox="0 0 24 24" style="margin-right: 8px; vertical-align: middle;">
                    <path d="M11.8 10.9c-.6-.6-1.5-1-2.4-1-1.7 0-3 1.3-3 3s1.3 3 3 3c.9 0 1.8-.4 2.4-1l1.4 1.4c-1 1-2.4 1.7-3.8 1.7C5.6 19 3 16.4 3 13s2.6-6 6-6c1.4 0 2.8.7 3.8 1.7l-1.4 1.4zM12 14c-.6 0-1.2-.2-1.6-.5l-4.7 4.7c1.3.5 2.7.8 4.2.8 3.3 0 6-2.7 6-6s-2.7-6-6-6c-1.5 0-2.9.3-4.2.8l4.7-4.7c.3.4.5 1 .5 1.6 0 3.3 2.7 6 6 6z"/>
                </svg>
                Цена заказа
            </h5>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="0-5000" id="price1" <#if prices?? && prices?seqContains("0-5000")> checked="true" </#if>>
                <label class="form-check-label" for="price1">Меньше 5 000 руб.</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="5000-15000" id="price2" <#if prices?? && prices?seqContains("5000-15000")> checked="true" </#if>>
                <label class="form-check-label" for="price2">5 000–15 000 руб.</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="15000-50000" id="price3" <#if prices?? && prices?seqContains("15000-50000")> checked="true" </#if>>
                <label class="form-check-label" for="price3">15 000–50 000 руб.</label>
            </div>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" value="50000-10000000" id="price4" <#if prices?? && prices?seqContains("50000-10000000")> checked="true" </#if>>
                <label class="form-check-label" for="price4">50 000 руб. и выше</label>
            </div>
        </div>

        <!-- Категории услуг -->
        <div class="filter-section filter-collapsible collapsed mb-4" data-type="category">
            <h5 class="filter-title d-flex justify-content-between align-items-center">
                <span>
                    <svg width="16" height="16" fill="currentColor" viewBox="0 0 24 24" style="margin-right: 8px; vertical-align: middle;">
                        <path d="M22 19c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V5c0-1.1.9-2 2-2h16c1.1 0 2 .9 2 2v14zM2 7v12h18V7H2zm2 0h14v12H4V7z"/>
                    </svg>
                    Категории услуг
                </span>
                <a href="javascript:void(0);" class="toggle-link text-primary d-flex align-items-center gap-1" title="Развернуть/Свернуть">
                    <span class="toggle-text">Развернуть</span>
                    <svg class="toggle-icon" width="14" height="14" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M7 14l5-5 5 5z"/>
                    </svg>
                </a>
            </h5>

            <div class="filter-selected-summary text-muted small px-3 py-2 border-bottom" style="display: block;">
                Ничего не выбрано
            </div>

            <div class="filter-content">
                <#if categories?? >
                    <#list categories as category>
                        <!-- Убрали px-3 у form-check, сделали отступ у label -->
                        <div class="form-check">
                            <input class="form-check-input category-checkbox" type="checkbox"
                                   value="${category.id}" name="category_ids"
                                   id="category_id_${category.id}"
                                    <#if categoryIds?? && categoryIds?seqContains(category.id)> checked="true" </#if>>
                            <label class="form-check-label ps-3" for="category_id_${category.id}">${category.name}</label>
                        </div>
                    </#list>
                <#else>
                    <p class="text-muted small px-3">Категории не загружены</p>
                </#if>
            </div>
        </div>

        <!-- Города -->
        <div class="filter-section filter-collapsible collapsed mb-4" data-type="city">
            <h5 class="filter-title d-flex justify-content-between align-items-center">
                <span>
                    <svg width="16" height="16" fill="currentColor" viewBox="0 0 24 24" style="margin-right: 8px; vertical-align: middle;">
                        <path d="M12 2C8.69 2 6 4.69 6 8c0 3.76 3.43 8.57 5.96 11.04C14.49 16.57 18 11.76 18 8c0-3.31-2.69-6-6-6zm0 10c-2.21 0-4-1.79-4-4s1.79-4 4-4 4 1.79 4 4-1.79 4-4 4z"/>
                    </svg>
                    Города
                </span>
                <a href="javascript:void(0);" class="toggle-link text-primary d-flex align-items-center gap-1" title="Развернуть/Свернуть">
                    <span class="toggle-text">Развернуть</span>
                    <svg class="toggle-icon" width="14" height="14" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M7 14l5-5 5 5z"/>
                    </svg>
                </a>
            </h5>

            <div class="filter-selected-summary text-muted small px-3 py-2 border-bottom" style="display: block;">
                Ничего не выбрано
            </div>

            <div class="filter-content">
                <#if cities?? >
                    <#list cities as city>
                        <!-- Отступ у label вместо form-check -->
                        <div class="form-check">
                            <input class="form-check-input city-checkbox" type="checkbox"
                                   value="${city.id}" name="city_ids"
                                   id="city_id_${city.id}"
                                    <#if cityIds?? && cityIds?seqContains(city.id)> checked="true"</#if>>
                            <label class="form-check-label ps-3" for="city_id_${city.id}">
                                ${city.cityName} (${city.region})
                            </label>
                        </div>
                    </#list>
                <#else>
                    <p class="text-muted small px-3">Города не загружены</p>
                </#if>
            </div>
        </div>

        <button type="submit" class="btn btn-primary w-100">Применить фильтры</button>
    </div>
</form>
