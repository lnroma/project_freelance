package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.order.OrderFaqService;
import com.naumoff.rnc.services.order.OrderService;
import com.naumoff.rnc.services.users.UserProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class OrderFaqPreviewController {

    private final OrderFaqService orderFaqService;
    private final OrderService orderService;
    private final UserProfileService userProfileService;

    public OrderFaqPreviewController(
            OrderFaqService orderFaqService,
            OrderService orderService,
            UserProfileService userProfileService
    ) {
        this.orderFaqService = orderFaqService;
        this.orderService = orderService;
        this.userProfileService = userProfileService;
    }

    @GetMapping(value = "/catalog/order/{id}/preview/faq")
    public String previewFaq(
            @PathVariable
            Long id,
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authenticatedUser
    ) {
        OrderEntity currentOrder = orderService.getOrderById(id);

        model.addAttribute("order", currentOrder);
        model.addAttribute("profileService", userProfileService);

        return "/catalog/orders/preview/faq";
    }
}
