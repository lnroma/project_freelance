package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.OrderFaqEntity;
import com.naumoff.rnc.dto.order.OrderFaqDto;
import com.naumoff.rnc.dto.users.history.UserHistoryInterface;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.order.OrderFaqService;
import com.naumoff.rnc.services.order.OrderService;
import com.naumoff.rnc.services.users.UserCountersService;
import com.naumoff.rnc.services.users.UserHistoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderFaqController {

    private final OrderFaqService orderFaqService;
    private final OrderService orderService;
    private final UserCountersService userCountersService;
    private final UserHistoryService userHistoryService;

    public OrderFaqController(
            OrderFaqService orderFaqService,
            OrderService orderService,
            UserCountersService userCountersService,
            UserHistoryService userHistoryService
    ) {
        this.orderFaqService = orderFaqService;
        this.orderService = orderService;
        this.userCountersService = userCountersService;
        this.userHistoryService = userHistoryService;
    }

    @PostMapping(value = "/catalog/order/{id}/ask/question")
    public String createQuestionForOrder(
            @ModelAttribute
            OrderFaqDto orderFaqDto,
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {
        OrderEntity currentOrder = orderService.getOrderById(id);

        orderFaqService.askQuestion(
                orderFaqDto,
                authUser.getEntity(),
                currentOrder
        );

        userCountersService.incrementAskedQuestions(authUser.getEntity());

        userHistoryService.createHistoryRecord(
                "Вы задали вопрос по заказу",
                "Вы задали вопрос по заказу",
                authUser.getEntity(),
                UserHistoryInterface.OBJECT_TYPE_ORDER,
                id
        );

        return "redirect:/catalog/order/" + id + "/preview/faq";
    }

    @PostMapping(value = "/catalog/order/{id}/response/question")
    public String responseQuestion(
            @ModelAttribute
            OrderFaqDto orderFaqDto,
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {

        OrderFaqEntity currentQuestion = orderFaqService.getQuestionEntity(orderFaqDto.getId());
        orderFaqService.responseQuestion(
                authUser.getEntity(),
                currentQuestion,
                orderFaqDto
        );

        userHistoryService.createHistoryRecord(
                "Вы ответили на вопрос",
                "Вы ответили на вопрос по вашему заказу",
                authUser.getEntity(),
                UserHistoryInterface.OBJECT_TYPE_ORDER,
                id
        );

        return "redirect:/catalog/order/" + id + "/preview/faq";
    }

}
