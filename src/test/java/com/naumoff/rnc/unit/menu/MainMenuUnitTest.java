package com.naumoff.rnc.unit.menu;

import com.naumoff.rnc.database.entities.menu.MainMenuApplication;
import com.naumoff.rnc.database.repository.menu.MainMenuApplicationRepository;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.services.menu.MainMenuService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainMenuUnitTest {

    @Mock
    private MainMenuApplicationRepository mainMenuApplicationRepository;

    @InjectMocks
    private MainMenuService mainMenuService;

    @Test
    void shouldReturnMenuItems() {
        List<MainMenuApplication> mainMenuItems = List.of(
                new MainMenuApplication(1L, 1, "/test1", "test", "ALL"),
                new MainMenuApplication(2L, 2, "/test2", "test2", "ALL"),
                new MainMenuApplication(3L, 3, "test3", "test3", "all")
        );

        when(mainMenuApplicationRepository.getAllMenus()).thenReturn(mainMenuItems);

        MenuCollectionDto result = mainMenuService.getMenuCollectionDto();

        assertThat(result.getMenus()).hasSize(3);
    }
}
