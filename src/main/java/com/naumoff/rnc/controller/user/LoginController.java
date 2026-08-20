package com.naumoff.rnc.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String home(Model model) {
        model.addAttribute("title", "Регистрация");
        model.addAttribute("message", "Добро пожаловать в Spring Boot приложение!");

        return "user/login"; // Возвращает index.html из templates/
    }

    @PostMapping("/login")
    public String auth() {
        return "redirect:/dashboard";
    }
}