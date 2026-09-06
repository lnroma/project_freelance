package com.naumoff.rnc.dto.pageStates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatalogPageQueryDto {
    private Pageable pageable;
    private String query;
    private List<Long> cityIds;
    private List<Long> categoryIds;
    private List<String> prices;
}
