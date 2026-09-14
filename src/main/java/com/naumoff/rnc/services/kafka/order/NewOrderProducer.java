package com.naumoff.rnc.services.kafka.order;

import com.naumoff.rnc.dto.elasticsearch.ElasticOrderDto;
import com.naumoff.rnc.dto.kafka.order.IndexNewOrderDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewOrderProducer {
    private final KafkaTemplate<String, IndexNewOrderDto> kafkaTemplate;
    public static final String KAFKA_ORDER_INDEX_TOPIC = "order.indexing";
    public NewOrderProducer(
            KafkaTemplate<String, IndexNewOrderDto> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCreatedOrder(IndexNewOrderDto indexNewOrderDto) {
        kafkaTemplate.send(KAFKA_ORDER_INDEX_TOPIC, indexNewOrderDto.getId(), indexNewOrderDto);
        System.out.println("order send to kafka");
    }

}
