<div class="card mb-3 shadow-sm toolbar-main">
                    <div class="toolbar-header bg-white py-2 px-3 d-flex align-items-center justify-content-between flex-wrap gap-2">

                        <!-- ЛЕВАЯ ЧАСТЬ: Переключение вида (Список / Карточки / Таблица) -->
                        <div class="btn-group" role="group" aria-label="Переключение вида">
                            <a type="button" class="btn btn-outline-primary btn-sm <#if catalogPage.toolBar.isCatalogListViewActive>active</#if>"
                                    href="/catalog">
                                <i class="bi bi-list-ul me-1"></i> Список
                            </a>
                            <a type="button" class="btn btn-outline-primary btn-sm <#if catalogPage.toolBar.isCatalogCardsViewActive>active</#if>"
                                    href="/catalog/view/cards">
                                <i class="bi bi-grid-fill me-1"></i> Карточки
                            </a>
                            <a type="button" class="btn btn-outline-primary btn-sm <#if catalogPage.toolBar.isCatalogTableViewActive>active</#if>"
                                    href="/catalog/view/table">
                                <i class="bi bi-table me-1"></i> Таблица
                            </a>
                        </div>

                        <!-- ЦЕНТР: Форма поиска -->
                        <form class="d-flex flex-grow-1 justify-content-center align-items-center gap-2" id="searchForm" onsubmit="performSearch(event)">
                            <div class="input-group input-group-sm w-100" style="max-width: 400px;">
                                <input type="text" name="query" value="${catalogPage.currentQuery ! ""}" class="form-control" id="searchQuery" placeholder="Поиск по названию, описанию..." aria-label="Поиск">
                                <input type="hidden" name="page" value="0" />
                                <input type="hidden" name="size" value="${catalogPage.page.size}" />
                                <button type="submit" class="btn btn-primary" id="searchBtn">
                                    <i class="bi bi-search me-1"></i> Найти
                                </button>
                            </div>
                        </form>

                        <!-- ПРАВАЯ ЧАСТЬ: Сортировка -->
                        <div class="dropdown">
                            <button class="btn btn-sm btn-outline-secondary dropdown-toggle"
                                    type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="bi bi-funnel me-1"></i> Сортировка: <span id="sort-label">По дате</span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li>
                                    <a class="dropdown-item d-flex justify-content-between align-items-center"
                                       href="#" onclick="setSort('price', event)">
                                        Цена <span class="badge bg-light text-dark small">↑</span>
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item d-flex justify-content-between align-items-center"
                                       href="#" onclick="setSort('created_at', event)">
                                        Дата создания <span class="badge bg-light text-dark small">↓</span>
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item d-flex justify-content-between align-items-center"
                                       href="#" onclick="setSort('rating', event)">
                                        Рейтинг заказчика <span class="badge bg-light text-dark small">↑</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>