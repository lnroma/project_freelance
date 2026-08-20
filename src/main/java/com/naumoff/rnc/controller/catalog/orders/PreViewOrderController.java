package com.naumoff.rnc.controller.catalog.orders;

import com.naumoff.rnc.controller.exceptions.NotFoundException;
import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.services.order.OrderService;
import com.naumoff.rnc.services.users.UserProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PreViewOrderController {

    final private OrderService orderService;
    final private UserProfileService userProfileService;

    public PreViewOrderController(
            OrderService orderService,
            UserProfileService userProfileService
    ) {
        this.orderService = orderService;
        this.userProfileService = userProfileService;
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


        return "catalog/orders/preview"; // Возвращает index.html из templates/
    }
}