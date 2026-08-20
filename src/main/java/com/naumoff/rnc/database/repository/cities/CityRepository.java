package com.naumoff.rnc.database.repository.cities;

import com.naumoff.rnc.database.entities.cities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    // findAll() уже есть в JpaRepository — можно использовать сразу
    List<CityEntity> findAll();

    // Опционально: поиск по названию города (может пригодиться для автодополнения)
    List<CityEntity> findByCityNameContainingIgnoreCase(String cityName);

    // Опционально: поиск по региону
    List<CityEntity> findByRegion(String region);
}