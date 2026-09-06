package com.naumoff.rnc.frontendFactories.catalog.catalog;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.dto.pageStates.order.AuthorDto;
import com.naumoff.rnc.dto.pageStates.order.ExecutorDto;
import com.naumoff.rnc.dto.pageStates.order.OrderDto;
import com.naumoff.rnc.services.users.UserProfileService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderStateFactory {

    private UserProfileService userProfileService;

    public OrderStateFactory(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Setter
    @Getter
    private List<OrderEntity> orders;

    public List<OrderDto> getOrderDtoFromOrders() {
        List<OrderDto> result = new ArrayList<>();
        getOrders().forEach((order) -> {
            AuthorDto authorDto = AuthorDto.builder()
                    .id(order.getCreator().getId())
                    .firstName(userProfileService.getCurrentUserProfile(order.getCreator()).getFirstName())
                    .lastName(userProfileService.getCurrentUserProfile(order.getCreator()).getLastName())
                    .build();

            ExecutorDto executorDto = ExecutorDto.builder()
                    .id(order.getId())
                    .firstName(userProfileService.getCurrentUserProfile(order.getExecutor()).getFirstName())
                    .lastName(userProfileService.getCurrentUserProfile(order.getExecutor()).getLastName())
                    .build();

            OrderDto currentOrder = OrderDto.builder()
                    .id(order.getId())
                    .title(order.getTitle())
                    .description(order.getDescription())
                    .createdAtDate(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yy")))
                    .createdAtTime(order.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .priceFrom(String.valueOf(order.getPriceFrom()))
                    .priceTo(String.valueOf(order.getPriceTo()))
                    .city(order.getCity().getCityName())
                    .category(order.getCategory().getName())
                    .author(authorDto)
                    .executor(executorDto)
                    .build();

            result.add(currentOrder);
        });

        return result;
    }
}
