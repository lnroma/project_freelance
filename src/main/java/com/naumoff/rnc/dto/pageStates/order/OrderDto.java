package com.naumoff.rnc.dto.pageStates.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String title;
    private String description;
    private String createdAtDate;
    private String createdAtTime;
    private String priceFrom;
    private String priceTo;
    private String city;
    private String category;
    private AuthorDto author;
    private ExecutorDto executor;
}
