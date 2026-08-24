<#if orderResponses?has_content && orderResponses?size gt 0 >
    <div class="app-card mt-5 primary-border">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="fw-bold text-accent-orange">
                <i class="fas fa-comments"></i> Предложения исполнителей (${orderResponses?size})
            </h4>
            <!-- Кнопка массового действия -->
            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="handleMassAction()">
                <i class="fas fa-check-square me-1"></i> Массовое действие
            </button>
        </div>

        <div class="table-responsive">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                <tr style="font-size: 0.75rem">
                    <th scope="col" style="width: 50px;">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="select-all" onchange="toggleAllCheckboxes(this)">
                            <label class="form-check-label visually-hidden" for="select-all">Выбрать все</label>
                        </div>
                    </th>
                    <th scope="col">Исполнитель</th>
                    <th scope="col" style="width: 500px;">Описание предложения</th>

                    <!-- Сортировка по цене -->
                    <th class="w-15 text-end">
                        <a href="?sort=price&order=asc" class="text-decoration-none text-body fw-bold">
                            Бюджет<i class="fas fa-arrow-up ms-1 text-success"></i>
                        </a>
                    </th>

                    <!-- Сортировка по сроку -->
                    <th class="w-10">
                        <a href="?sort=timeline&order=asc" class="text-decoration-none text-body fw-bold">
                            Срок<i class="fas fa-arrow-down ms-1 text-danger"></i>
                        </a>
                    </th>

                    <th class="w-15">Дата</th>

                    <!-- Сортировка по рейтингу -->
                    <th width="w-10">
                        <a href="?sort=rating&order=asc" class="text-decoration-none text-body fw-bold">
                            Рейтинг <i class="fas fa-arrow-up ms-1 text-success"></i>
                        </a>
                    </th>

                    <th class="text-end w-20">Действия</th>
                </tr>
                </thead>
                <tbody>
                <#list orderResponses as response>
                    <tr id="row-${response.id}">
                        <td>
                            <div class="form-check">
                                <input class="form-check-input row-checkbox" type="checkbox"
                                       value="${response.id}" onchange="updateMassSelection()">
                                <label class="form-check-label visually-hidden" for="cb-${response.id}">Выбрать ${response.id}</label>
                            </div>
                        </td>
                        <td>
                            <span class="">#${response.proposalUserId}
                                &nbsp; ${profileService.getCurrentUserProfile(response.proposalUser).firstName}
                                &nbsp; ${profileService.getCurrentUserProfile(response.proposalUser).lastName}
                            </span>
                        </td>
                        <td class="fw-normal">
                            ${response.description?replace("\n", "<br>")}
                        </td>
                        <td class="text-end  text-accent-orange">
                            ${response.price} ₽
                        </td>
                        <td>
                            <span class="badge bg-light text-dark border">${response.timeline} дн.</span>
                        </td>
                        <td class="small text-muted">
                            ${response.createdAt?datetime("yyyy-MM-dd'T'HH:mm:ss.SSS")?string("dd-MM-yy HH:mm")}
                        </td>
                        <td>
                            <span class="badge bg-warning text-dark">4.8</span>
                        </td>
                        <td class="text-end">
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-sm btn-secondary" title="Скрыть"
                                        onclick="hideResponse(${response.id})">
                                    <i class="fas fa-eye-slash"></i>
                                </button>
                                <button type="button" class="btn btn-sm btn-success" title="Принять"
                                        onclick="acceptResponse(${response.id})">
                                    <i class="fas fa-check"></i>
                                </button>
                                <button type="button" class="btn btn-sm btn-danger" title="Отклонить"
                                        onclick="rejectResponse(${response.id})">
                                    <i class="fas fa-times"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
<#else>
    <div class="app-card primary-border mt-4">
        <p>Пока ответов на ваш заказ нет, но они очень скоро появятся. Мы пришлём уведомление в центр уведомлений в шапке сайта, а также на вашу электронную почту.</p>
    </div>
</#if>
