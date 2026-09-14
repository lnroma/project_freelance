package com.naumoff.rnc.unit.cities;

import com.naumoff.rnc.database.entities.cities.CityEntity;
import com.naumoff.rnc.database.repository.cities.CityRepository;
import com.naumoff.rnc.services.cities.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    private CityEntity city1;
    private CityEntity city2;

    @BeforeEach
    void setUp() {
        city1 = new CityEntity();
        city1.setId(1L);
        city1.setCityName("Москва");
        city1.setRegion("Центральный");

        city2 = new CityEntity();
        city2.setId(2L);
        city2.setCityName("Санкт-Петербург");
        city2.setRegion("Северо-Западный");
    }

    @Test
    @DisplayName("getAllAvailableCity — возвращает список всех городов из репозитория")
    void getAllAvailableCity_returnsAllCities() {
        // Arrange
        List<CityEntity> expectedCities = List.of(city1, city2);
        when(cityRepository.findAll()).thenReturn(expectedCities);

        // Act
        List<CityEntity> result = cityService.getAllAvailableCity();

        // Assert
        assertThat(result).isEqualTo(expectedCities);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getCityName()).isEqualTo("Москва");
        assertThat(result.get(1).getCityName()).isEqualTo("Санкт-Петербург");
    }

    @Test
    @DisplayName("getAllAvailableCity — возвращает пустой список, если в БД нет городов")
    void getAllAvailableCity_returnsEmptyListWhenNoCities() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(List.of());

        // Act
        List<CityEntity> result = cityService.getAllAvailableCity();

        // Assert
        assertThat(result).isEmpty();
    }
}
