package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.ResponseEntity;
import com.naumoff.rnc.dto.order.OrderResponseDto;
import com.naumoff.rnc.dto.users.history.UserHistoryInterface;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.menu.MainMenuService;
import com.naumoff.rnc.services.order.OrderResponseService;
import com.naumoff.rnc.services.order.OrderService;
import com.naumoff.rnc.services.users.UserHistoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreateOrderResponseController {

    private final OrderResponseService orderResponseService;
    private final OrderService orderService;
    private final UserHistoryService userHistoryService;
    private final MainMenuService mainMenuService;

    public CreateOrderResponseController(
            OrderResponseService orderResponseService,
            OrderService orderService,
            UserHistoryService userHistoryService,
            MainMenuService mainMenuService
    ) {
        this.orderResponseService = orderResponseService;
        this.orderService = orderService;
        this.userHistoryService = userHistoryService;
        this.mainMenuService = mainMenuService;
    }

    @PostMapping(value = "/catalog/order/{id}/response")
    public String createOrderResponse(
            @ModelAttribute
            OrderResponseDto orderResponseDto,
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {

        OrderEntity orderEntity = orderService.getOrderById(id);

        ResponseEntity responseEntity = orderResponseService.createOrderResponse(
                orderEntity,
                authUser.getEntity(),
                orderResponseDto
        );

        userHistoryService.createHistoryRecord(
                "Вы ответили на заказ",
                "Вы успешно ответили на заказ",
                authUser.getEntity(),
                UserHistoryInterface.OBJECT_TYPE_ORDER, id
        );

        return "redirect:/catalog/order/" + String.valueOf(id) + "/preview";
    }
}
