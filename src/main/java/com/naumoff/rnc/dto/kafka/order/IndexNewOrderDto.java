package com.naumoff.rnc.dto.kafka.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexNewOrderDto {
    private String id;
    private String title;
    private String description;
    private BigInteger priceFrom;
    private BigInteger priceTo;
}
