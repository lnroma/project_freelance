package com.naumoff.rnc.services.menu;

import com.naumoff.rnc.database.entities.menu.MainMenuApplication;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.menu.MainMenuApplicationRepository;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.dto.menu.menuItem.MenuItemDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainMenuService {

    final private MainMenuApplicationRepository mainMenuApplicationRepository;
    private MenuCollectionDto mainMenu;

    public MainMenuService(
            MainMenuApplicationRepository mainMenuApplicationRepository
    ) {
        this.mainMenuApplicationRepository = mainMenuApplicationRepository;
    }

    public void buildMenu() {
        List<MainMenuApplication> mainMenuApplicationList = mainMenuApplicationRepository.getAllMenus();

        MenuCollectionDto menuCollectionDto = MenuCollectionDto.builder().build();
        Map<String, MenuItemDto> menuItemDtoMap = new HashMap<>();


        mainMenuApplicationList.forEach((ma) -> {
            MenuItemDto mid = MenuItemDto.builder()
                    .uri(ma.getMenuUri())
                    .label(ma.getMenuTitle())
                    .isActive(false)
                    .orderMenu(ma.getOrderWidth())
                    .build();

            menuItemDtoMap.put(ma.getMenuUri(), mid);
        });

        menuCollectionDto.setMenus(menuItemDtoMap);
    }

    public MenuCollectionDto getMenuCollectionDto() {
        if (mainMenu == null) {
            buildMenu();
        }

        return mainMenu;
    }

    public void assignMenuToTemplate(Model model, MenuCollectionDto mainMenu) {
        model.addAttribute("mainMenu", mainMenu);
    }
}