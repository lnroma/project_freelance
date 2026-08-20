package com.naumoff.rnc.dto.states;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpParamsDto {
    private String query;
    private Integer number;
    private Integer size;
    private BigInteger priceFrom;
    private BigInteger priceTo;
//
//    public ArrayList<Map<String, String>> getAllFields() {
//        ArrayList<Map<String, String>> result = new ArrayList<>();
//
//        Map<String, String> query = new HashMap<>();
//        query.put("name", "query");
//        query.put("type", STRING_TYPE);
//        result.add(query);
//
//        Map<String, String> number = new HashMap<>();
//        query.put("name", "number");
//        query.put("type", INTEGER_TYPE);
//        result.add(number);
//
//        Map<String, String> size = new HashMap<>();
//        query.put("name", "size");
//        query.put("type", INTEGER_TYPE);
//        result.add(size);
//
//        Map<String, String> priceFrom = new HashMap<>();
//        priceFrom.put("name", "priceFrom");
//        priceFrom.put("type", BIG_INTEGER_TYPE);
//        result.add(priceFrom);
//
//        Map<String, String> priceTo = new HashMap<>();
//        priceTo.put("name", "priceTo");
//        priceTo.put("type", BIG_INTEGER_TYPE);
//        result.add(priceTo);
//
//        return result;
//    }
}
