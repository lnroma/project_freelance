package com.naumoff.rnc.database.repository.menu;

import com.naumoff.rnc.database.entities.menu.MainMenuApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MainMenuApplicationRepository extends JpaRepository<MainMenuApplication, Long> {
    @Query("SELECT mm FROM MainMenuApplication mm ORDER BY mm.orderWidth ASC ")
    List<MainMenuApplication> getAllMenus();

    @Query("select mm from MainMenuApplication mm where mm.role = 'USER' order by mm.orderWidth asc")
    List<MainMenuApplication> getAllMenuForRoleUser();

    @Query("select mm from MainMenuApplication mm where mm.role = 'ALL' order by mm.orderWidth asc")
    List<MainMenuApplication> getAllMenuForRoleAll();
}
