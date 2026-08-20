package com.naumoff.rnc.services.order;

import com.naumoff.rnc.database.entities.order.CategoryEntity;
import com.naumoff.rnc.database.repository.order.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryEntity> getAllCategoryList() {
        return this.categoryRepository.findAll();
    }

    public List<CategoryEntity> getFirstTenCategories() {
        return this.categoryRepository.getListByLimit(12L);
    }
}
