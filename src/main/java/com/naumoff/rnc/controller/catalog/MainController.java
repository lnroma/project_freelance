package com.naumoff.rnc.controller.catalog;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.dto.pageStates.CatalogPageQueryDto;
import com.naumoff.rnc.dto.pageStates.CatalogPageState;
import com.naumoff.rnc.frontendFactories.catalog.CatalogPageStateFactory;
import com.naumoff.rnc.services.breadcrumbs.BreadcrumbsService;
import com.naumoff.rnc.services.cities.CityService;
import com.naumoff.rnc.services.formaters.LinkHelperService;
import com.naumoff.rnc.services.menu.MainMenuService;
import com.naumoff.rnc.services.order.CategoryService;
import com.naumoff.rnc.services.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class MainController {

    private final BreadcrumbsService breadcrumbsService;
    private final MainMenuService mainMenuService;
    private final CatalogPageStateFactory catalogPageStateFactory;

    public MainController(
            BreadcrumbsService breadcrumbsService,
            MainMenuService mainMenuService,
            CatalogPageStateFactory catalogPageStateFactory
    ) {
        this.breadcrumbsService = breadcrumbsService;
        this.mainMenuService = mainMenuService;
        this.catalogPageStateFactory = catalogPageStateFactory;
    }

    @GetMapping("/catalog")
    public String home(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(value = "category_ids", required = false) List<Long> categoryIds,
            @RequestParam(value = "city_ids", required = false) List<Long> cityIds,
            @RequestParam(value = "prices", required = false) List<String> prices
    ) {
        catalogPageStateFactory.setButtonCatalogListViewIsActive();

        commonModelSetup(
                model,
                page,
                size,
                query,
                categoryIds,
                cityIds,
                prices
        );

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "catalog/main";
    }

    @GetMapping("/catalog/view/card")
    public String viewCard(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(value = "category_ids", required = false) List<Long> categoryIds,
            @RequestParam(value = "city_ids", required = false) List<Long> cityIds,
            @RequestParam(value = "prices", required = false) List<String> prices
    ) {
        catalogPageStateFactory.setButtonCatalogCardsIsActive();
        commonModelSetup(model, page, size, query, categoryIds, cityIds, prices);

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "catalog/cards";
    }

    @GetMapping("/catalog/view/table")
    public String viewTable(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(value = "category_ids", required = false) List<Long> categoryIds,
            @RequestParam(value = "city_ids", required = false) List<Long> cityIds,
            @RequestParam(value = "prices", required = false) List<String> prices
    ) {
        catalogPageStateFactory.setButtonCatalogTableIsActive();
        commonModelSetup(model, page, size, query, categoryIds, cityIds, prices);

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "catalog/table";
    }

    private void commonModelSetup(
            Model model,
            int page,
            int size,
            String query,
            List<Long> categoryIds,
            List<Long> cityIds,
            List<String> prices
    ) {
        Pageable pageable = PageRequest.of(page, size);
//
//        Page<OrderEntity> orderPage = orderService.getOrders(
//                pageable,
//                query,
//                categoryIds,
//                cityIds,
//                prices
//        );
//
//        String paginationUrl = linkHelperService.getUrlForNavigation(
//                "/catalog",
//                query,
//                categoryIds,
//                cityIds,
//                prices
//        );
//
//        model.addAttribute("orders", orderPage.getContent());
//        model.addAttribute("orderCount", orderService.getCountOrders());
//        model.addAttribute("page", orderPage);
//        model.addAttribute("categoryIds", categoryIds);
//        model.addAttribute("cityIds", cityIds);
//        model.addAttribute("prices", prices);
//        model.addAttribute("currentQuery", query);
//
//        model.addAttribute("categories", categoryService.getAllCategoryList());
//        model.addAttribute("cities", cityService.getAllAvailableCity());
//        catalogPageStateFactory.setCurrentQuery();

        CatalogPageQueryDto catalogPageQueryDto = CatalogPageQueryDto.builder()
                .pageable(pageable)
                .query(query)
                .cityIds(cityIds)
                .categoryIds(categoryIds)
                .prices(prices)
                .build();

        catalogPageStateFactory.setCurrentQuery(catalogPageQueryDto);

        CatalogPageState pageState = catalogPageStateFactory.getStateForCatalog();

        model.addAttribute("catalogPage", pageState);
        model.addAttribute("paginationUrl", pageState.getPaginationUrl());
        model.addAttribute("page", pageState.getPage());


        breadcrumbsService.assignBreadcrumbsToModel(breadcrumbsService.BR_CATALOG, model);
    }
}