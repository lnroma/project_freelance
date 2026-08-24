package com.naumoff.rnc.database.entities.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_responses", schema = "fl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "timeline", nullable = false)
    private Integer timeline; // в днях

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "proposal_user_id", nullable = false)
    private Long proposalUserId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_user_id", insertable = false, updatable = false)
    private UserEntity proposalUser;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    // Обратная связь: один ответ — один заказ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_order_responses_order"))
    private OrderEntity order;


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
