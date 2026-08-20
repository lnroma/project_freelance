package com.naumoff.rnc.database.entities.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_profiles", schema = "fl")
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // Связь с твоей основной сущностью User

    @Column(columnDefinition = "TEXT", name = "about_me")
    private String aboutMe;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(columnDefinition = "TEXT")
    private String education;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nick_name")
    private String nickName;
}