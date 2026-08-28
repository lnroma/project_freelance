package com.naumoff.rnc.database.entities.pages;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name = "seo_pages", schema = "fl")
@AllArgsConstructor
@NoArgsConstructor
public class SeoPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "slug", columnDefinition = "TEXT")
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords;

    @Column(name = "additional_information")
    @JdbcTypeCode(SqlTypes.JSON)
    private String additionalInformation; // храним как JSON-строку; можно использовать Jackson для маппинга
}