package com.naumoff.rnc.controller.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naumoff.rnc.dto.debug.NotificationPayload;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.services.menu.MainMenuService;
import org.springframework.lang.Nullable;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NotificationController {

    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate messagingTemplate;
    private final MainMenuService mainMenuService;
    private String destinationPrefix = "/user/";

    public NotificationController(
            SimpMessagingTemplate messagingTemplate,
            SimpUserRegistry userRegistry,
            MainMenuService mainMenuService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.simpUserRegistry = userRegistry;
        this.mainMenuService = mainMenuService;
    }

    @GetMapping("/test/message")
    // Отправка уведомления конкретному пользователю
    public String sendNotificationToUser(Principal principal) {

        // Spring сам подставит префикс "/user/{userId}"
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John");
        data.put("age", 30);
        ObjectMapper mapper = new ObjectMapper();
        // Convert to JSON string
        try {
            String jsonString = mapper.writeValueAsString(data);

            messagingTemplate.convertAndSendToUser("6", "/queue/n", jsonString);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
//        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "user/login";
    }

    @GetMapping("/t")
    @SendToUser("/queue/n")
    public NotificationPayload not() {
        NotificationPayload notificationPayload = new NotificationPayload("test");

        return notificationPayload;
    }


    @GetMapping("/current-principal")
    public String getCurrentPrincipal(Principal principal) {
        Map<String, Object> result = new HashMap<>();
        result.put("principal_object", principal);
        result.put("principal_name", principal != null ? principal.getName() : null);
        result.put("principal_class", principal != null ? principal.getClass().getName() : "null");

        // Покажем, как Spring видит этого пользователя в реестре STOMP
        String username = principal != null ? principal.getName() : "";
        if (simpUserRegistry.getUser(username) != null) {
            result.put("ws_session_found", true);
//            result.put("ws_sessions_count", simpUserRegistry.(username).size());
        } else {
            result.put("ws_session_found", false);
            // Покажем всех пользователей в реестре для сравнения
            result.put("all_active_users", simpUserRegistry.getUsers().stream()
//                    .map(SimpUserRegistry::getName)
                    .toList());
        }

        System.out.println(result);
        return "user/login";
    }

    @GetMapping("/send-test")
    public String sendTest() {
        String email = "test10@test.ru";
        System.out.println("=== ПОПЫТКА ОТПРАВКИ ===");
        System.out.println("Ищем пользователя: " + email);

        SimpUser user = simpUserRegistry.getUser(email);
        if (user == null) {
            System.out.println("[ОШИБКА] Пользователь '" + email + "' НЕ найден в реестре STOMP!");
            System.out.println("Активные пользователи: " + simpUserRegistry.getUsers().stream().map(SimpUser::getName).toList());
            return "FAIL: User not found in STOMP registry. Check if user is connected and authenticated.";
        } else {
            System.out.println("[OK] Пользователь найден. Сессий: " + user.getSessions().size());
        }

        String payload = "{\"type\":\"debug\", \"text\":\"Test notification for " + email + "\"}";

        // ЕДИНСТВЕННЫЙ ПРАВИЛЬНЫЙ ВАРИАНТ
        messagingTemplate.convertAndSendToUser(email, "/queue/notification", payload);
        System.out.println("Отправлено через convertAndSendToUser('" + email + "', '/queue/notification')");

        return "user/login";
    }

    @GetMapping("/debug/ws/session-details")
    public String getSessionDetails(Principal principal) {
        String name = principal != null ? principal.getName() : null;
        Map<String, Object> result = new HashMap<>();

        if (name == null) {
            result.put("error", "No principal");
            System.out.println("no prinicpals");
            return "user/login";
        }

//        SimpUser user = simpUserRegistry.findUser(name);
        SimpUser user = simpUserRegistry.getUsers().stream()
                .filter(u -> u.getName().equals("test10@test.ru"))
                .findFirst()
                .orElse(null);

        result.put("user_exists", user != null);

        if (user != null) {
            List<String> sessionsInfo = new ArrayList<>();
            for (SimpSession session : user.getSessions()) {
                sessionsInfo.add(String.format("ID=%s, Destinations=%s",
                        session.getId(),
                        session.getSubscriptions().stream().map(d -> d.getDestination()).toList()));
            }
            result.put("sessions", sessionsInfo);
            result.put("total_sessions", user.getSessions().size());

            // Ключевой момент: есть ли у сессий подписка на нужный путь?
            boolean hasSubscription = user.getSessions().stream()
                    .flatMap(s -> s.getSubscriptions().stream())
                    .anyMatch(d -> d.getDestination().contains("/queue/notification"));
            result.put("has_subscription_to_queue_notification", hasSubscription);
        } else {
            result.put("user_not_found", true);
        }

        System.out.println(result);
//        return result;

        return "user/login";
    }

    public void convertAndSendToUserCustom(String user, String destination, Object payload,
                                     @Nullable Map<String, Object> headers, @Nullable MessagePostProcessor postProcessor)
            throws MessagingException {

        Assert.notNull(user, "User must not be null");
        String username = user;
        Assert.isTrue(!user.contains("%2F"), () -> "Invalid sequence \"%2F\" in user name: " + username);
        user = StringUtils.replace(user, "/", "%2F");
        destination = destination.startsWith("/") ? destination : "/" + destination;
        System.out.println("dist: " + this.destinationPrefix + user + destination);
        messagingTemplate.convertAndSend(this.destinationPrefix + user + destination, payload, headers, postProcessor);
    }
}