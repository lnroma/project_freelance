package com.naumoff.rnc.database.entities.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_counters", schema = "fl")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCountersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "viewed")
    private Long viewed;

    @Column(name = "offers")
    private Long offers;

    @Column(name = "questions")
    private Long questions;
}
