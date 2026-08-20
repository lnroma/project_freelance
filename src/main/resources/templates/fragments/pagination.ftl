<#if page?? && page.getTotalElements() gt 0>
    <nav aria-label="Page navigation">
        <ul class="custom-pagination mb-2 mt-2">
            <!-- Предыдущая -->
            <#if page.hasPrevious()>
                <li class="page-item">
                    <a class="page-link" href="${paginationUrl!"?"}page=${page.number - 1}&size=${page.size}">←</a>
                </li>
            <#else>
                <li class="page-item disabled">
                    <span class="page-link">←</span>
                </li>
            </#if>

                  <!-- Номера страниц: генерируем диапазон 0..totalPages-1 -->
                  <#list 0 ..< page.getTotalPages() as p>
                      <#if p == page.number>
                          <li class="page-item active">
                              <span class="page-link current-page">${p + 1}</span>
                          </li>
                      <#else>
                          <li class="page-item">
                              <a class="page-link" href="${paginationUrl ! "?"}page=${p}&size=${page.size}">${p + 1}</a>
                          </li>
                      </#if>
                  </#list>

            <!-- Следующая -->
            <#if page.hasNext()>
                <li class="page-item">
                    <a class="page-link" href="${paginationUrl ! "?"}page=${page.number + 1}&size=${page.size}">→</a>
                </li>
            <#else>
                <li class="page-item disabled">
                    <span class="page-link">→</span>
                </li>
            </#if>
        </ul>
    </nav>
</#if>