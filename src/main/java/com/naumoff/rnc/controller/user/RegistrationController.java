package com.naumoff.rnc.controller.user;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.dto.users.UserDto;
import com.naumoff.rnc.services.users.UserAuthenticationService;
import com.naumoff.rnc.services.users.UserRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;

    public RegistrationController(
            UserRegistrationService userRegistrationService,
            UserAuthenticationService userAuthenticationService
    ) {
        this.userRegistrationService = userRegistrationService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("/registration")
    public String home(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("message", "Добро пожаловать в Spring Boot приложение!");
        return "user/registration"; // Возвращает index.html из templates/
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String post(
            @ModelAttribute
            @Valid
            UserDto request,
            BindingResult result,
            Model model,
            HttpServletRequest requestHttp
    ) {
        if (result.hasErrors()) {
            return "user/registration";
        }

        try {
            UserEntity user = userRegistrationService.registerUser(request);

            this.userAuthenticationService.authenticate(user, requestHttp);

            return "redirect:/user/profile/add";
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return "user/registration";
        }
    }
}