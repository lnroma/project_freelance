package com.naumoff.rnc.database.entities.users.role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_role_access_list")
public class AccessListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private UserRoleEntity role;

    @Column(name = "object_id")
    private BigInteger objectId;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "object_key")
    private String objectKey;

    @Column(name = "access_level")
    private String accessLevel;
}
