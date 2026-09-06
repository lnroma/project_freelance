package com.naumoff.rnc.dto.pageStates.toolbar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToolbarDto {
    private Boolean isCatalogListViewActive;
    private Boolean isCatalogCardsViewActive;
    private Boolean isCatalogTableViewActive;
}
