package com.naumoff.rnc.database.entities.cities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities", schema = "fl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "city_name", nullable = false, length = 150)
    private String cityName;


}