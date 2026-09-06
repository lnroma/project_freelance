package com.naumoff.rnc.dto.pageStates;

import com.naumoff.rnc.database.entities.cities.CityEntity;
import com.naumoff.rnc.database.entities.order.CategoryEntity;
import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.dto.pageStates.order.OrderDto;
import com.naumoff.rnc.dto.pageStates.toolbar.ToolbarDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CatalogPageState {
    private Long orderCounts;
    private Page<OrderEntity> page;
    private String paginationUrl;
    private List<Long> categoryIds;
    private List<Long> cityIds;
    private List<String> prices;
    private String currentQuery;
    private List<CategoryEntity> categories;
    private List<CityEntity> cities;
//    private List<OrderEntity> orders;
    private ToolbarDto toolBar;
    private List<OrderDto> orders;
}
