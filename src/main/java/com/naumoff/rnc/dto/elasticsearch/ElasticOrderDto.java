package com.naumoff.rnc.dto.elasticsearch;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "orders")
public class ElasticOrderDto {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String priceFrom;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String priceTo;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String cityName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String categoryName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String authorName;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String executorName;
}
