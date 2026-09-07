package com.naumoff.rnc.unit.catalog;

import com.naumoff.rnc.database.entities.order.CategoryEntity;
import com.naumoff.rnc.database.repository.order.CategoryRepository;
import com.naumoff.rnc.services.order.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategoryListTest() {
        List<CategoryEntity> categoryEntityList = List.of(
                new CategoryEntity(1L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(2L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(3L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(4L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(5L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(6L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(7L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(8L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(9L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(10L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(11L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(12L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null)
        );

        when(categoryRepository.findAll()).thenReturn(categoryEntityList);

        assertThat(categoryService.getAllCategoryList()).hasSize(12);
    }

    @Test
    void getFirstTenCategoriesTest() {
        List<CategoryEntity> categoryEntityList = List.of(
                new CategoryEntity(1L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(2L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(3L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(4L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(5L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(6L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(7L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(8L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(9L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(10L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(11L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null),
                new CategoryEntity(12L, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null)
        );

        when(categoryRepository.getListByLimit(12L)).thenReturn(categoryEntityList);

        assertThat(categoryService.getFirstTenCategories()).hasSize(12);
    }
}
