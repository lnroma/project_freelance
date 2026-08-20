<#-- fragments/breadcrumbs.ftl -->
<#if breadcrumbs?has_content && breadcrumbs?size gt 0>
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb custom-breadcrumb">
            <#list breadcrumbs as item>
                <#if item_has_next>
                    <#-- Ссылка (не последняя) -->
                    <li class="breadcrumb-item">
                        <a href="${item.url!''}" class="text-decoration-none text-dark fw-semibold hover-accent">
                            ${item.label!'Без названия'}
                        </a>
                    </li>
                <#else>
                    <#-- Текущая страница (без ссылки) -->
                    <li class="breadcrumb-item active" aria-current="page" style="color: var(--text-main); font-weight: 700;">
                        ${item.label!'Без названия'}
                    </li>
                </#if>
            </#list>
        </ol>
    </nav>
<#else>
    <#-- Опционально: ничего не рендерим, если списка нет -->
</#if>
