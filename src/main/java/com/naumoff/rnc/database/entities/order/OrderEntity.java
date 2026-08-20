package com.naumoff.rnc.database.entities.order;

import com.naumoff.rnc.database.entities.cities.CityEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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