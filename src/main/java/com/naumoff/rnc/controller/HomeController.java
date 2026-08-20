package com.naumoff.rnc.controller;

import com.naumoff.rnc.services.order.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final CategoryService categoryService;

    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("message", "Добро пожаловать в Spring Boot приложение!");
        model.addAttribute("categories", categoryService.getFirstTenCategories());

        return "index"; // Возвращает index.html из templates/
    }
}