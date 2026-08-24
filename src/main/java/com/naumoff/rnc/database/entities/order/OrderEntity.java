package com.naumoff.rnc.database.entities.order;

import com.naumoff.rnc.database.entities.cities.CityEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", schema = "fl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.MERGE)
    @JoinColumn(name = "author_user_id", nullable = false)
    private UserEntity creator;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "executor_user_id", nullable = true)
    private UserEntity executor;

    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<OrderFaqEntity> orderFaqEntities = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private OrderCountersEntity orderCountersEntity;

    @Column(name = "price_from")
    private Double priceFrom;

    @Column(name = "price_to")
    private Double priceTo;

    @Column(name = "deadline_at")
    private LocalDateTime deadlineAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    // --- СВЯЗЬ "ОДИН КО МНОГИМ" ---
    @OneToMany(
            mappedBy = "order",           // Имя поля в классе OrderResponse
            cascade = CascadeType.ALL,     // Осторожно: удалит все ответы, если удалить заказ. Часто используют MERGE или вообще без каскада.
            orphanRemoval = true,         // Удалит ответ из БД, если убрал его из списка и сохранил заказ
            fetch = FetchType.LAZY         // Обязательно LAZY! Иначе при загрузке любого заказа подтянется весь список ответов
    )
    private List<ResponseEntity> responses = new ArrayList<>();

    /**
     * Обработчик перед сохранением — устанавливает createdAt и updatedAt
     */
    @PrePersist
    protected void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Обработчик перед обновлением — обновляет updatedAt
     */
    @PreUpdate
    protected void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "OrderEntity{id=" + id + ",title=" + title + "}";
    }
}