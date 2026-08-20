package com.naumoff.rnc.database.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gallery", schema = "fl")
@NoArgsConstructor
@AllArgsConstructor
public class UserGalleryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private UserProfileEntity profile;

    private String imageUrl;
    private Integer position = 0;
    private boolean isAvatar = false;
    private boolean isHead = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    // Геттеры, сеттеры...
}