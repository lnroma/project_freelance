package com.naumoff.rnc.database.entities.users;

import com.naumoff.rnc.database.entities.order.OrderFaqEntity;
import com.naumoff.rnc.database.entities.post.PostEntity;
import com.naumoff.rnc.database.entities.users.role.UserRoleEntity;
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

    private Long cityId;

    @Column(unique = true, nullable = false)
    private String authToken;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHistory> historyList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProfileEntity> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderFaqEntity> orderFaqEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "users_to_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<UserRoleEntity> roles = new ArrayList<>();
//
    public UserCountersEntity getUserCountersEntity() { return null; }
    public void setUserCountersEntity(UserCountersEntity userCountersEntity) {}
//    @OneToOne(mappedBy = "user")
//    private UserCountersEntity userCountersEntity;

    public String toString() {
        return "UserEntity{id=" + id + ",name=" + email + "}";
    }
}

