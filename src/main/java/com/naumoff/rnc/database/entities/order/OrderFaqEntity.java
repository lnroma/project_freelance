package com.naumoff.rnc.database.entities.order;

import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "order_faq", schema = "fl")
public class OrderFaqEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(name = "question")
    private String question;

    @Column(name = "response")
    private String response;

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
        return "OrderFaqEntity{id=" + id + ",id=" + id + "}";
    }
}
