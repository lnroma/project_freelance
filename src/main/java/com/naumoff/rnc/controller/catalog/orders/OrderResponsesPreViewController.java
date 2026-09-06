package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.menu.MainMenuService;
import com.naumoff.rnc.services.order.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderResponsesPreViewController {

    private final OrderService orderService;
    private final MainMenuService mainMenuService;

    private OrderResponsesPreViewController(
            OrderService orderService,
            MainMenuService mainMenuService
    ) {
        this.orderService = orderService;
        this.mainMenuService = mainMenuService;
    }

    @GetMapping(value = "/catalog/order/{id}/preview/offers")
    public String previewOrderResponses(
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser,
            @PathVariable
            Long id
    ) {
        OrderEntity order = orderService.getOrderById(id);

        if (!order.getCreator().getId().equals(authUser.getEntity().getId())) {
            return "redirect:/catalog/order/" + order.getId() + "/preview/";
        }

        model.addAttribute("order", order);

        UserEntity userEntity = authUser.getEntity();
        model.addAttribute("currentUser", userEntity);

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "/catalog/orders/preview/responses";
    }
}
