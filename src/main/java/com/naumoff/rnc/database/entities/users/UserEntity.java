package com.naumoff.rnc.database.entities.users;

import com.naumoff.rnc.database.entities.post.PostEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users", schema = "fl")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password; // MD5-хеш

    @Column(nullable = false)
    private String role;
    private BigDecimal costPerMonth;

    private Integer cityId;

    @Column(unique = true, nullable = false)
    private String authToken;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProfileEntity> profiles = new ArrayList<>();

    public String toString() {
        return "UserEntity{id=" + id + ",name=" + email + "}";
    }
}

