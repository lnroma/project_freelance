package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.controller.exceptions.NotFoundException;
import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.ResponseEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.services.breadcrumbs.BreadcrumbsService;
import com.naumoff.rnc.services.order.OrderCounterService;
import com.naumoff.rnc.services.order.OrderService;
import com.naumoff.rnc.services.users.UserProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PreViewOrderController {

    final private OrderService orderService;
    final private UserProfileService userProfileService;
    final private BreadcrumbsService breadcrumbsService;
    final private OrderCounterService orderCounterService;

    public PreViewOrderController(
            OrderService orderService,
            UserProfileService userProfileService,
            BreadcrumbsService breadcrumbsService,
            OrderCounterService orderCounterService
    ) {
        this.orderService = orderService;
        this.userProfileService = userProfileService;
        this.breadcrumbsService = breadcrumbsService;
        this.orderCounterService = orderCounterService;
    }

    @GetMapping("/catalog/order/{id}/preview")
    public String preview(
            Model model,
            @PathVariable Long id
    ) {
        OrderEntity orderEntity = orderService.getOrderById(id);

        if (orderEntity == null) {
            throw new NotFoundException("Заказ с id " + id + " не найден");
        }


        UserEntity userEntity = orderEntity.getCreator();
        userProfileService.getCurrentUserProfile(userEntity);

        model.addAttribute("order", orderEntity);
        model.addAttribute("profileService", userProfileService);

        // orders responses
        List<ResponseEntity> responseEntityList = orderEntity.getResponses();
        model.addAttribute("orderResponses", responseEntityList);

        breadcrumbsService.assignBreadcrumbsToModel(breadcrumbsService.BR_ORDER_PREVIEW, model);

        orderCounterService.incrementViewed(orderEntity);

        return "catalog/orders/preview"; // Возвращает index.html из templates/
    }
}