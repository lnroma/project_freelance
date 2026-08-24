package com.naumoff.rnc.services.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.ResponseEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.order.OrderResponseRepository;
import com.naumoff.rnc.dto.order.OrderResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrderResponseService {

    private final OrderResponseRepository orderResponseRepository;

    public OrderResponseService(
            OrderResponseRepository orderResponseRepository
    ) {
        this.orderResponseRepository = orderResponseRepository;
    }

    /**
     * Create response to order
     *
     * @param currentOrder Current order entity
     * @param currentUser current user entity
     * @param orderResponseDto order response dto
     *
     * @return current response entity
     */
    public ResponseEntity createOrderResponse(
            OrderEntity currentOrder,
            UserEntity currentUser,
            OrderResponseDto orderResponseDto
    ) {
        ResponseEntity responseEntity = new ResponseEntity();

        responseEntity.setDescription(orderResponseDto.getDescription());
        responseEntity.setPrice(orderResponseDto.getPrice());
        responseEntity.setProposalUser(currentUser);
        responseEntity.setProposalUserId(currentUser.getId());
        responseEntity.setOrderId(currentOrder.getId());
        responseEntity.setOrder(currentOrder);
        responseEntity.setTimeline(orderResponseDto.getTimeline());

        orderResponseRepository.save(responseEntity);

        return responseEntity;
    }
}
