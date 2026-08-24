package com.naumoff.rnc.database.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_counters", schema = "fl")
public class UserCountersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "sended_offers")
    private Long sendedOffers;

    @Column(name = "asked_questions")
    private Long askedQuestions;

    @Column(name = "accepted_orders")
    private Long acceptedOrders;

    @Column(name = "order_in_works")
    private Long orderInWorks;

    @Column(name = "viewed")
    private Long viewed;
}
