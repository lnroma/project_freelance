package com.naumoff.rnc.database.entities.post;

import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts", schema = "fl")
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    private UserEntity author;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}