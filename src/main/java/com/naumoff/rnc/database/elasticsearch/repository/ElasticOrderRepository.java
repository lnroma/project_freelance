package com.naumoff.rnc.database.elasticsearch.repository;

import com.naumoff.rnc.dto.elasticsearch.ElasticOrderDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticOrderRepository extends ElasticsearchRepository<ElasticOrderDto, String> {
}
