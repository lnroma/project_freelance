package com.naumoff.rnc.controller.user;

import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.services.menu.MainMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    final private MainMenuService mainMenuService;
    public LoginController(
            MainMenuService mainMenuService
    ) {
        this.mainMenuService = mainMenuService;
    }

    @GetMapping("/login")
    public String home(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("message", "Добро пожаловать в Spring Boot приложение!");

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "user/login"; // Возвращает index.html из templates/
    }

    @PostMapping("/login")
    public String auth() {
        return "redirect:/dashboard";
    }
}