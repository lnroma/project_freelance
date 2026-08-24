package com.naumoff.rnc.services.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.OrderFaqEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.order.OrderFaqRepository;
import com.naumoff.rnc.dto.order.OrderFaqDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderFaqService {

    private final OrderFaqRepository orderFaqRepository;

    public OrderFaqService(
            OrderFaqRepository orderFaqRepository
    ) {
        this.orderFaqRepository = orderFaqRepository;
    }

    /**
     * Ask question for order owner
     *
     * @param orderFaqDto Dto for request
     * @param currentUser Current auth user is author of question
     * @param currentOrder Current order for link to order question
     * @return Order faq entity for render
     */
    public OrderFaqEntity askQuestion(
            OrderFaqDto orderFaqDto,
            UserEntity currentUser,
            OrderEntity currentOrder
    ) {
        OrderFaqEntity orderFaqEntity = new OrderFaqEntity();

        orderFaqEntity.setAuthor(currentUser);
        orderFaqEntity.setOrder(currentOrder);
        orderFaqEntity.setQuestion(orderFaqDto.getQuestion());

        this.orderFaqRepository.save(orderFaqEntity);

        return orderFaqEntity;
    }

    /**
     * Response to question from order owner
     *
     * @param currentUser current auth user
     * @param currentOrderFaqEntity current order faq entity
     * @param orderFaqDto order faq dto
     * @return Return updated entity for response to question
     */
    public OrderFaqEntity responseQuestion(
            UserEntity currentUser,
            OrderFaqEntity currentOrderFaqEntity,
            OrderFaqDto orderFaqDto
    ) {
        // @todo response to order question only can order owner
        if (!Objects.equals(currentOrderFaqEntity.getOrder().getCreator().getId(), currentUser.getId())) {
            return currentOrderFaqEntity;
        }

        currentOrderFaqEntity.setResponse(orderFaqDto.getResponse());

        this.orderFaqRepository.save(currentOrderFaqEntity);

        return currentOrderFaqEntity;
    }

    /**
     * Get current question by id
     *
     * @param id Long id for question
     * @return null or question entity
     */
    public OrderFaqEntity getQuestionEntity(Long id) {
        return orderFaqRepository.findById(id).get();
    }
}
