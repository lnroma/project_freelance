package com.naumoff.rnc.unit.catalog.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.ResponseEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.order.OrderResponseRepository;
import com.naumoff.rnc.dto.order.OrderResponseDto;
import com.naumoff.rnc.services.order.OrderResponseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderResponseServiceTest {

    @Mock
    private OrderResponseRepository orderResponseRepository;

    @InjectMocks
    private OrderResponseService orderResponseService;

    @Test
    void createOrderResponse_shouldSetAllFieldsAndSave() {
        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);

        OrderEntity currentOrder = new OrderEntity();
        currentOrder.setId(100L);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setDescription("Могу выполнить за 3 дня");
        dto.setPrice(15000.0);
        dto.setTimeline(3);

        when(orderResponseRepository.save(any(ResponseEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity result = orderResponseService.createOrderResponse(currentOrder, currentUser, dto);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Могу выполнить за 3 дня");
        assertThat(result.getPrice()).isEqualTo(15000.0);
        assertThat(result.getTimeline()).isEqualTo(3);
        assertThat(result.getProposalUser()).isEqualTo(currentUser);
        assertThat(result.getProposalUserId()).isEqualTo(1L);
        assertThat(result.getOrderId()).isEqualTo(100L);
        assertThat(result.getOrder()).isEqualTo(currentOrder);

        verify(orderResponseRepository).save(any(ResponseEntity.class));
    }

    @Test
    void createOrderResponse_shouldCaptureAndVerifySavedEntity() {
        UserEntity currentUser = new UserEntity();
        currentUser.setId(5L);

        OrderEntity currentOrder = new OrderEntity();
        currentOrder.setId(200L);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setDescription("Готов взять заказ");
        dto.setPrice(25000.0);
        dto.setTimeline(5);

        ArgumentCaptor<ResponseEntity> captor = ArgumentCaptor.forClass(ResponseEntity.class);
        when(orderResponseRepository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        orderResponseService.createOrderResponse(currentOrder, currentUser, dto);

        ResponseEntity saved = captor.getValue();
        assertThat(saved.getDescription()).isEqualTo("Готов взять заказ");
        assertThat(saved.getPrice()).isEqualTo(25000.0);
        assertThat(saved.getTimeline()).isEqualTo(5);
        assertThat(saved.getProposalUserId()).isEqualTo(5L);
        assertThat(saved.getOrderId()).isEqualTo(200L);
    }

    @Test
    void createOrderResponse_shouldCallSaveExactlyOnce() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        OrderEntity order = new OrderEntity();
        order.setId(10L);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setDescription("Ответ");
        dto.setPrice(1000.0);
        dto.setTimeline(1);

        when(orderResponseRepository.save(any(ResponseEntity.class)))
                .thenReturn(new ResponseEntity());

        orderResponseService.createOrderResponse(order, user, dto);

        verify(orderResponseRepository, times(1)).save(any(ResponseEntity.class));
    }
}
