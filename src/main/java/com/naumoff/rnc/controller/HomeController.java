package com.naumoff.rnc.controller;

import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.services.menu.MainMenuService;
import com.naumoff.rnc.services.order.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final CategoryService categoryService;
    private final MainMenuService mainMenuService;

    public HomeController(
            CategoryService categoryService,
            MainMenuService mainMenuService
    ) {
        this.categoryService = categoryService;
        this.mainMenuService = mainMenuService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        model.addAttribute("message", "Добро пожаловать в Spring Boot приложение!");
        model.addAttribute("categories", categoryService.getFirstTenCategories());

        MenuCollectionDto mainMenu = mainMenuService.getMenuCollectionDto();

        mainMenuService.assignMenuToTemplate(model, mainMenu);

        return "index"; // Возвращает index.html из templates/
    }
}