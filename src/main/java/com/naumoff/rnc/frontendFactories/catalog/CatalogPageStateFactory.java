package com.naumoff.rnc.frontendFactories.catalog;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.dto.pageStates.CatalogPageQueryDto;
import com.naumoff.rnc.dto.pageStates.CatalogPageState;
import com.naumoff.rnc.dto.pageStates.toolbar.ToolbarDto;
import com.naumoff.rnc.frontendFactories.catalog.catalog.OrderStateFactory;
import com.naumoff.rnc.services.cities.CityService;
import com.naumoff.rnc.services.formaters.LinkHelperService;
import com.naumoff.rnc.services.order.CategoryService;
import com.naumoff.rnc.services.order.OrderService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class CatalogPageStateFactory {

    private final OrderService orderService;
    private final CategoryService categoryService;
    private final CityService cityService;
    private final LinkHelperService linkHelperService;
    private final OrderStateFactory orderStateFactory;

    @Getter
    @Setter
    private CatalogPageQueryDto currentQuery;
    @Getter
    @Setter
    private UserEntity currentUser;
    private ToolbarDto toolbarDto;

    public CatalogPageStateFactory(
            OrderService orderService,
            CategoryService categoryService,
            CityService cityService,
            LinkHelperService linkHelperService,
            OrderStateFactory orderStateFactory
    ) {
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.cityService = cityService;
        this.linkHelperService = linkHelperService;
        this.orderStateFactory = orderStateFactory;
    }

    public void setButtonCatalogListViewIsActive() {
        toolbarDto = ToolbarDto.builder()
                .isCatalogListViewActive(true)
                .isCatalogCardsViewActive(false)
                .isCatalogTableViewActive(false)
                .build();
    }

    public void setButtonCatalogCardsIsActive() {
        toolbarDto = ToolbarDto.builder()
                .isCatalogListViewActive(false)
                .isCatalogCardsViewActive(true)
                .isCatalogTableViewActive(false)
                .build();
    }

    public void setButtonCatalogTableIsActive() {
        toolbarDto = ToolbarDto.builder()
                .isCatalogListViewActive(false)
                .isCatalogCardsViewActive(false)
                .isCatalogTableViewActive(true)
                .build();
    }

    public CatalogPageState getStateForCatalog() {
        CatalogPageState catalogPageState = CatalogPageState.builder().build();

        catalogPageState.setOrderCounts(orderService.getCountOrders());
        catalogPageState.setPage(
                orderService.getOrders(
                        currentQuery.getPageable(),
                        currentQuery.getQuery(),
                        currentQuery.getCategoryIds(),
                        currentQuery.getCityIds(),
                        currentQuery.getPrices()
                )
        );

        catalogPageState.setCategoryIds(currentQuery.getCategoryIds());
        catalogPageState.setCityIds(currentQuery.getCityIds());
        catalogPageState.setCurrentQuery(currentQuery.getQuery());
        catalogPageState.setCategories(categoryService.getAllCategoryList());
        catalogPageState.setCities(cityService.getAllAvailableCity());

        catalogPageState.setPrices(currentQuery.getPrices());

//        catalogPageState.setOrders(catalogPageState.getPage().getContent());
        orderStateFactory.setOrders(catalogPageState.getPage().getContent());
        catalogPageState.setOrders(orderStateFactory.getOrderDtoFromOrders());

        String paginationUrl = linkHelperService.getUrlForNavigation(
                "/catalog",
                currentQuery.getQuery(),
                currentQuery.getCategoryIds(),
                currentQuery.getCityIds(),
                currentQuery.getPrices()
        );

        catalogPageState.setToolBar(toolbarDto);

        catalogPageState.setPaginationUrl(paginationUrl);

        return catalogPageState;
    }
}
