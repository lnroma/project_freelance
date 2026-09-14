package com.naumoff.rnc.services.elasticsearch;

import com.naumoff.rnc.database.elasticsearch.repository.ElasticOrderRepository;
import com.naumoff.rnc.dto.elasticsearch.ElasticOrderDto;
import com.naumoff.rnc.dto.pageStates.order.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchIndexingService {
    private final ElasticOrderRepository orderRepository;

    public SearchIndexingService(
            ElasticOrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    public ElasticOrderDto combineOrderDto(OrderDto orderDto) {
        return ElasticOrderDto.builder()
                .id(orderDto.getId().toString())
                .title(orderDto.getTitle())
                .description(orderDto.getDescription())
                .priceFrom(orderDto.getPriceFrom())
                .priceTo(orderDto.getPriceTo())
                .cityName(orderDto.getCity())
                .categoryName(orderDto.getCategory())
                .authorName(orderDto.getAuthor().getFirstName() + " " + orderDto.getAuthor().getLastName())
                .executorName(orderDto.getExecutor().getFirstName() + " " + orderDto.getExecutor().getLastName())
                .build();
    }

    public ElasticOrderDto indexOne(OrderDto orderDto) {
        return orderRepository.save(combineOrderDto(orderDto));
    }

    public Iterable<ElasticOrderDto> indexOrderBatch(List<OrderDto> elasticOrders) {
        Iterable<ElasticOrderDto> elasticOrderDtos = elasticOrders.stream().map(this::combineOrderDto).toList();

        return orderRepository.saveAll(elasticOrderDtos);
    }
}
