package com.naumoff.rnc.services.order;

import com.naumoff.rnc.database.entities.order.OrderCountersEntity;
import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.repository.order.OrderCountersRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCounterService {

    private final OrderCountersRepository orderCountersRepository;

    public OrderCounterService(
            OrderCountersRepository orderCountersRepository
    ) {
        this.orderCountersRepository = orderCountersRepository;
    }

    public void incrementViewed(OrderEntity currentOrder) {
        OrderCountersEntity counters = currentOrder.getOrderCountersEntity();
        if (counters == null) {
            counters = new OrderCountersEntity();
            counters.setViewed(0L);
            counters.setOrder(currentOrder);
        }

        Long current = counters.getViewed();
        current++;
        counters.setViewed(current);

        this.orderCountersRepository.save(counters);
    }

    public void decrementViewed(OrderEntity currentOrder) {
        OrderCountersEntity counters = currentOrder.getOrderCountersEntity();
        if (counters == null) {
            return;
        }

        Long current = counters.getViewed();

        if (current == 0L) {
            return;
        }

        current--;
        counters.setViewed(current);

        this.orderCountersRepository.save(counters);
    }

    public void incrementOffers(OrderEntity currentOrder) {
        OrderCountersEntity counters = currentOrder.getOrderCountersEntity();
        if (counters == null) {
            counters = new OrderCountersEntity();
            counters.setOffers(0L);
            counters.setOrder(currentOrder);
        }

        Long current = counters.getViewed();
        current++;
        counters.setOffers(current);

        this.orderCountersRepository.save(counters);
    }

    public void decrementOffers(OrderEntity currentOrder) {
        OrderCountersEntity counters = currentOrder.getOrderCountersEntity();
        if (counters == null) {
            return;
        }

        Long current = counters.getOffers();

        if (current == 0L) {
            return;
        }

        current--;
        counters.setOffers(current);

        this.orderCountersRepository.save(counters);
    }

    public void incrementQuestions(OrderEntity currentOrder) {
        OrderCountersEntity counters = currentOrder.getOrderCountersEntity();
        if (counters == null) {
            counters = new OrderCountersEntity();
            counters.setQuestions(0L);
            counters.setOrder(currentOrder);
        }

        Long current = counters.getViewed();
        current++;
        counters.setQuestions(current);

        this.orderCountersRepository.save(counters);
    }

    public void decrementQuestions(OrderEntity currentOrder) {
        OrderCountersEntity counters = currentOrder.getOrderCountersEntity();
        if (counters == null) {
            return;
        }

        Long current = counters.getQuestions();

        if (current == 0L) {
            return;
        }

        current--;
        counters.setQuestions(current);

        this.orderCountersRepository.save(counters);
    }
}
