package com.naumoff.rnc.dto.order;

import lombok.Data;
import lombok.Builder;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateOrderDto {

    public CreateOrderDto() {}
    private Long id;

    @NotBlank(message = "Заголовок обязателен")
    private String title;

    private String description;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    private BigDecimal priceFrom;

    @Min(value = 0, message = "Цена не может быть отрицательной")
    private BigDecimal priceTo;


    private String deadlineAt;

    private Long cityId;
    private Long categoryId;
    private Long authorUserId;
    private Long executorUserId;
}