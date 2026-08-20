package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.dto.order.CreateOrderDto;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.breadcrumbs.BreadcrumbsService;
import com.naumoff.rnc.services.cities.CityService;
import com.naumoff.rnc.services.order.CategoryService;
import com.naumoff.rnc.services.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CreateOrderController {

    private final BreadcrumbsService breadcrumbsService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final CityService cityService;

    public CreateOrderController(
            BreadcrumbsService breadcrumbsService,
            OrderService orderService,
            CategoryService categoryService,
            CityService cityService
    ) {
        this.breadcrumbsService = breadcrumbsService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.cityService = cityService;
    }

    @GetMapping("/catalog/order/create")
    public String create(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("age", 38);
        model.addAttribute("message", "Добро пожаловать в Spring Boot приложение!");
        model.addAttribute("cities", this.cityService.getAllAvailableCity());
        model.addAttribute("orderCategories", this.categoryService.getAllCategoryList());

        System.out.println("category logging");
        this.categoryService.getAllCategoryList().forEach(cat -> {
            System.out.println("category " + cat.getName());
        });

        breadcrumbsService.assignBreadcrumbsToModel(
                breadcrumbsService.BR_ORDER_CREATE,
                model
        );

        return "catalog/orders/create"; // Возвращает index.html из templates/
    }

    @PostMapping(value = "/catalog/order/create")
    public String post(
            @ModelAttribute CreateOrderDto createOrderDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Model model,
            AuthenticatedUser authenticatedUser
    ) {
        this.orderService.createOrder(
                createOrderDto,
                authenticatedUser.getEntity()
        );

        return "catalog/orders/create";
    }
}