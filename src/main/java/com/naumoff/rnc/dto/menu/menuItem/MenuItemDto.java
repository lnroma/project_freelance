package com.naumoff.rnc.dto.menu.menuItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemDto {
    private String uri;
    private String label;
    private Boolean isActive;
    private Integer orderMenu;
}
