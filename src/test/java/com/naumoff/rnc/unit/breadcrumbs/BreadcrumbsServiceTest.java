package com.naumoff.rnc.unit.breadcrumbs;

import com.naumoff.rnc.services.breadcrumbs.BreadcrumbsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BreadcrumbsServiceTest {

    private final BreadcrumbsService breadcrumbsService = new BreadcrumbsService();

    @Test
    @DisplayName("assignBreadcrumbsToModel — добавляет атрибут breadcrumbs в Model")
    void assignBreadcrumbsToModel_addsAttribute() {
        // Arrange
        Model model = mock(Model.class);
        String uri = breadcrumbsService.BR_DASHBOARD;

        // Act
        breadcrumbsService.assignBreadcrumbsToModel(uri, model);

        // Assert
        verify(model).addAttribute("breadcrumbs", any(List.class));
    }

    @Test
    @DisplayName("getBreadCrumbsForPage — возвращает хлебные крошки для dashboard")
    void getBreadCrumbsForPage_dashboard() {
        // Act
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage(breadcrumbsService.BR_DASHBOARD);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        Map<String, String> first = result.get(0);
        assertThat(first.get("url")).isEqualTo("/");
        assertThat(first.get("label")).isEqualTo("Главная");

        Map<String, String> second = result.get(1);
        assertThat(second.get("url")).isEqualTo("/dashboard");
        assertThat(second.get("label")).isEqualTo("Дашбоард");
    }

    @Test
    @DisplayName("getBreadCrumbsForPage — возвращает хлебные крошки для profile")
    void getBreadCrumbsForPage_profile() {
        // Act
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage(breadcrumbsService.BR_PROFILE);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        Map<String, String> third = result.get(2);
        assertThat(third.get("url")).isEqualTo("/profile");
        assertThat(third.get("label")).isEqualTo("Профиль пользователя");
    }

    @Test
    @DisplayName("getBreadCrumbsForPage — возвращает хлебные крошки для order_create")
    void getBreadCrumbsForPage_orderCreate() {
        // Act
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage(breadcrumbsService.BR_ORDER_CREATE);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        Map<String, String> second = result.get(1);
        assertThat(second.get("url")).isEqualTo("/catalog");
        assertThat(second.get("label")).isEqualTo("Список заказов");

        Map<String, String> third = result.get(2);
        assertThat(third.get("url")).isEqualTo("/catalog/order/create");
        assertThat(third.get("label")).isEqualTo("Создать заказ");
    }

    @Test
    @DisplayName("getBreadCrumbsForPage — возвращает хлебные крошки для catalog")
    void getBreadCrumbsForPage_catalog() {
        // Act
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage(breadcrumbsService.BR_CATALOG);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        Map<String, String> second = result.get(1);
        assertThat(second.get("url")).isEqualTo("/catalog");
        assertThat(second.get("label")).isEqualTo("Каталог заказов");
    }

    @Test
    @DisplayName("getBreadCrumbsForPage — возвращает хлебные крошки для order_preview")
    void getBreadCrumbsForPage_orderPreview() {
        // Act
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage(breadcrumbsService.BR_ORDER_PREVIEW);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        Map<String, String> third = result.get(2);
        assertThat(third.get("url")).isEqualTo("/catalog/order/preview");
        assertThat(third.get("label")).isEqualTo("Подробнее о заказе");
    }

    @Test
    @DisplayName("getBreadCrumbsForPage — возвращает null для неизвестного URI")
    void getBreadCrumbsForPage_unknownUri() {
        // Act
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage("unknown-page");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("getLinkHome — возвращает корректную ссылку на главную")
    void getLinkHome_returnsHomeLink() {
        // Reflection-free: тестируем через вызов одного из методов, который использует getLinkHome
        List<Map<String, String>> result = breadcrumbsService.getBreadCrumbsForPage(breadcrumbsService.BR_DASHBOARD);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        Map<String, String> first = result.get(0);
        assertThat(first.get(breadcrumbsService.URL_KEY)).isEqualTo("/");
        assertThat(first.get(breadcrumbsService.LABEL_KEY)).isEqualTo("Главная");
    }
}
