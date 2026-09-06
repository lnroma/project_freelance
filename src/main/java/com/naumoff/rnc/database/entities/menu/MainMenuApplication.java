package com.naumoff.rnc.database.entities.menu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "main_menu_application", schema = "fl")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MainMenuApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_width", length = 255, nullable = false)
    private Integer orderWidth;

    @Column(name = "menu_uri", length = 255, nullable = false)
    private String menuUri;

    @Column(name = "menu_title", length = 255, nullable = false)
    private String menuTitle;

    @Column(name = "role", length = 255, nullable = false)
    private String role;
}
