package com.naumoff.rnc.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private String description;
    private Double price;
    private Integer timeline;
}
