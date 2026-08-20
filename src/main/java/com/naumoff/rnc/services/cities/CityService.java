package com.naumoff.rnc.services.cities;

import com.naumoff.rnc.database.entities.cities.CityEntity;
import com.naumoff.rnc.database.repository.cities.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(
            CityRepository cityRepository
    ) {
        this.cityRepository = cityRepository;
    }

    /**
     * get all available cities on this projects
     *
     * @return list of cities
     */
    public List<CityEntity> getAllAvailableCity() {
        return cityRepository.findAll();
    }
}
