package com.naumoff.rnc.dto.menu;

import com.naumoff.rnc.dto.menu.menuItem.MenuItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCollectionDto {
    Map<String, MenuItemDto> menus = new HashMap<>();
}
