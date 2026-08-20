package com.naumoff.rnc.specification;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {
    private static final String TITLE_LABEL = "title";
    private static final String DESCRIPTION_LABEL = "description";
    private static final String PRICE_FROM_LABEL = "priceFrom";
    private static final String PRICE_TO_LABEL = "priceTo";
    private static final String CATEGORY_LABEL = "category";
    private static final String CATEGORY_ID_LABEL = "id";
    private static final String CITY_LABEL = "city";
    private static final String CITY_ID_LABEL = "id";

    public static Specification<OrderEntity> hasTitleContaining(String querySearch) {
        return ((root, query, criteriaBuilder) -> {
            if (querySearch.isEmpty()) {
                return null;
            }

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(TITLE_LABEL)), "%" + querySearch.toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(DESCRIPTION_LABEL)), "%" + querySearch.toLowerCase() + "%"));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<OrderEntity> betweenPriceFrom(Long priceFrom, Long priceTo) {
        return ((root, query, criteriaBuilder) -> {
            if (priceFrom == null || priceTo == null) {
                return null;
            }

            List<Predicate> predicateList = new ArrayList<>();

            predicateList.add(criteriaBuilder.between(root.get(PRICE_FROM_LABEL), priceFrom, priceTo));
            predicateList.add(criteriaBuilder.between(root.get(PRICE_TO_LABEL), priceFrom, priceTo));

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        });
    }

    public static Specification<OrderEntity> hasCity(ArrayList<Long> cityIds) {
        return (root, query, criteriaBuilder) -> {
            if (cityIds == null) {
                return null;
            }

            return root.get(CITY_LABEL).get(CITY_ID_LABEL).in(cityIds.stream().toList());
        };
    }

    public static Specification<OrderEntity> hasCategory(ArrayList<Long> categoryIds) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryIds == null) {
                return null;
            }

            return root.get(CATEGORY_LABEL).get(CATEGORY_ID_LABEL).in(categoryIds.stream().toList());
        });
    }

    public static Specification<OrderEntity> buildSpecifications(
            String querySearch,
            List<Long> categoryIds,
            List<Long> cityIds,
            Long priceFrom,
            Long priceTo
    ) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!querySearch.equals("")) {
                predicates.add(hasTitleContaining(querySearch).toPredicate(root, query, criteriaBuilder));
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(hasCategory(new ArrayList<>(categoryIds)).toPredicate(root, query, criteriaBuilder));
            }

            if (cityIds != null && !cityIds.isEmpty()) {
                predicates.add(hasCity(new ArrayList<>(cityIds)).toPredicate(root, query, criteriaBuilder));
            }

            if (priceFrom != null) {
                predicates.add(betweenPriceFrom(priceFrom, 10000000000L).toPredicate(root, query, criteriaBuilder));
            }

            if (priceFrom == null && priceTo != null) {
                predicates.add(betweenPriceFrom(0L, priceTo).toPredicate(root, query, criteriaBuilder));
            }

            if (priceFrom != null && priceTo != null) {
                predicates.add(betweenPriceFrom(priceFrom, priceTo).toPredicate(root, query, criteriaBuilder));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
