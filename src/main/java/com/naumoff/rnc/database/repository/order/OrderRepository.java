package com.naumoff.rnc.database.repository.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    // Заказы в диапазоне цен — без удалённых
    @Query("SELECT o FROM OrderEntity o WHERE o.deletedAt IS NULL " +
           "AND ((o.priceFrom IS NULL OR o.priceFrom <= :maxPrice) " +
           "AND (o.priceTo IS NULL OR o.priceTo >= :minPrice))")
    List<OrderEntity> findByPriceRange(Double minPrice, Double maxPrice);

    // Пример: заказы с дедлайном до указанной даты — без удалённых
    @Query("SELECT o FROM OrderEntity o WHERE o.deletedAt IS NULL AND o.deadlineAt <= :deadline")
    List<OrderEntity> findByDeadlineAtBefore(LocalDateTime deadline);

    Page<OrderEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}