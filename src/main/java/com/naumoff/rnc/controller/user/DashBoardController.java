package com.naumoff.rnc.controller.user;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.dto.users.DashboardStats;
import com.naumoff.rnc.model.Activity;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.breadcrumbs.BreadcrumbsService;
import com.naumoff.rnc.services.pages.SeoPagesService;
import com.naumoff.rnc.services.users.UserCheckHasProfile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DashBoardController {

    final private UserCheckHasProfile userCheckHasProfile;
    final private BreadcrumbsService breadcrumbsService;

    public DashBoardController(
            UserCheckHasProfile userCheckHasProfile,
            BreadcrumbsService breadcrumbsService
    ) {
        this.userCheckHasProfile = userCheckHasProfile;
        this.breadcrumbsService = breadcrumbsService;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {
        UserEntity currentUser = authUser.getEntity();

        System.out.println(authUser.getUsername());

        model.addAttribute("user", currentUser);

        DashboardStats stats = new DashboardStats();
        stats.setTotalOrders(12);
        stats.setCompletedOrders(8);
        stats.setInProgressOrders(3);
        stats.setPendingOrders(1);
        model.addAttribute("stats", stats);

        model.addAttribute("profileExist", true);
        if (!userCheckHasProfile.isHasProfile(currentUser)) {
            model.addAttribute("profileExist", false);
        }

        // 3. Последние действия
        List<Activity> recentActivities = List.of(
                new Activity("Создан заказ #101", "ORDER_CREATED", LocalDateTime.now()),
                new Activity("Обновлён профиль", "PROFILE_UPDATE", LocalDateTime.now().minusHours(2))
        );
        model.addAttribute("recentActivities", recentActivities);

        // 4. Активные заказы
        List<OrderEntity> activeOrders = List.of(
//                new OrderEntity("Консультация психолога", "test", 1L, 1L, 1L, 100.00, 200.00, LocalDateTime.now())
        );

        model.addAttribute("activeOrders", activeOrders);

        breadcrumbsService.assignBreadcrumbsToModel(
                breadcrumbsService.BR_DASHBOARD,
                model
        );

        return "user/dashboard";
    }
}
