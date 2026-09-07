package com.naumoff.rnc.unit.catalog.order;

import com.naumoff.rnc.database.entities.order.OrderCountersEntity;
import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.repository.order.OrderCountersRepository;
import com.naumoff.rnc.services.order.OrderCounterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderCounterServiceUnitTest {
    @Mock
    private OrderCountersRepository orderCountersRepository;

    @Mock
    private OrderEntity currentOrder;

//    @Mock
//    private OrderCountersEntity orderCountersEntity;

    @InjectMocks
    private OrderCounterService orderCounterService;

    private void instantOrderMock() {
        OrderCountersEntity orderCountersEntity = new OrderCountersEntity(
                1L, currentOrder, 1L, 1L, 1L
        );

        when(currentOrder.getOrderCountersEntity()).thenReturn(orderCountersEntity);
        when(orderCountersRepository.save(orderCountersEntity)).thenReturn(orderCountersEntity);
    }

    @Test
    void incrementViewedTest() {
        instantOrderMock();

        orderCounterService.incrementViewed(currentOrder);
        assertThat(currentOrder.getOrderCountersEntity().getViewed()).isEqualTo(2L);
    }

    @Test
    void decrementViewedTest() {
        instantOrderMock();

        orderCounterService.decrementViewed(currentOrder);
        assertThat(currentOrder.getOrderCountersEntity().getViewed()).isEqualTo(0L);
    }

    @Test
    void incrementOffers() {
        instantOrderMock();

        orderCounterService.incrementOffers(currentOrder);
        assertThat(currentOrder.getOrderCountersEntity().getOffers()).isEqualTo(2L);
    }

    @Test
    void decrementOffers() {
        instantOrderMock();

        orderCounterService.decrementOffers(currentOrder);
        assertThat(currentOrder.getOrderCountersEntity().getOffers()).isEqualTo(0L);
    }

    @Test
    void incrementQuestions() {
        instantOrderMock();

        orderCounterService.incrementQuestions(currentOrder);
        assertThat(currentOrder.getOrderCountersEntity().getQuestions()).isEqualTo(2L);
    }

    @Test
    void decrementQuestions() {
        instantOrderMock();

        orderCounterService.decrementQuestions(currentOrder);
        assertThat(currentOrder.getOrderCountersEntity().getQuestions()).isEqualTo(0L);
    }
}
