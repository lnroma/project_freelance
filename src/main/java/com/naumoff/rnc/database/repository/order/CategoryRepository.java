package com.naumoff.rnc.database.repository.order;


import com.naumoff.rnc.database.entities.order.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
//    List<CategoryEntity> findAll();

    @Query("SELECT oc FROM CategoryEntity oc ORDER BY oc.id LIMIT :limit")
    List<CategoryEntity> getListByLimit(Long limit);
}