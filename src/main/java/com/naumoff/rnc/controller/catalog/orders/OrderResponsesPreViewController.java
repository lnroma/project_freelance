package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.order.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderResponsesPreViewController {

    private final OrderService orderService;

    private OrderResponsesPreViewController(
            OrderService orderService
    ) {
        this.orderService = orderService;
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
        model.addAttribute("order", order);

        if (authUser != null) {
            UserEntity userEntity = authUser.getEntity();
            model.addAttribute("currentUser", userEntity);
        }

        return "/catalog/orders/preview/responses";
    }
}
