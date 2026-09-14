package com.naumoff.rnc.unit.catalog.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.order.OrderFaqEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.order.OrderFaqRepository;
import com.naumoff.rnc.dto.order.OrderFaqDto;
import com.naumoff.rnc.services.order.OrderFaqService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderFaqServiceTest {

    @Mock
    private OrderFaqRepository orderFaqRepository;

    @InjectMocks
    private OrderFaqService orderFaqService;

    // ── askQuestion ──────────────────────────────────────────────

    @Test
    void askQuestion_shouldCreateAndSaveFaqEntity() {
        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);

        OrderEntity currentOrder = new OrderEntity();
        currentOrder.setId(100L);

        OrderFaqDto dto = new OrderFaqDto();
        dto.setQuestion("Какие сроки выполнения?");

        when(orderFaqRepository.save(any(OrderFaqEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderFaqEntity result = orderFaqService.askQuestion(dto, currentUser, currentOrder);

        assertThat(result).isNotNull();
        assertThat(result.getAuthor()).isEqualTo(currentUser);
        assertThat(result.getOrder()).isEqualTo(currentOrder);
        assertThat(result.getQuestion()).isEqualTo("Какие сроки выполнения?");
        assertThat(result.getResponse()).isNull();

        verify(orderFaqRepository).save(any(OrderFaqEntity.class));
    }

    @Test
    void askQuestion_shouldSetAllFieldsCorrectly() {
        UserEntity author = new UserEntity();
        author.setId(1L);

        OrderEntity order = new OrderEntity();
        order.setId(50L);

        OrderFaqDto dto = new OrderFaqDto();
        dto.setQuestion("Сколько стоит?");

        ArgumentCaptor<OrderFaqEntity> captor = ArgumentCaptor.forClass(OrderFaqEntity.class);
        when(orderFaqRepository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        orderFaqService.askQuestion(dto, author, order);

        OrderFaqEntity saved = captor.getValue();
        assertThat(saved.getAuthor()).isEqualTo(author);
        assertThat(saved.getOrder()).isEqualTo(order);
        assertThat(saved.getQuestion()).isEqualTo("Сколько стоит?");
    }

    @Test
    void askQuestion_shouldSaveExactlyOnce() {
        UserEntity user = new UserEntity();
        OrderEntity order = new OrderEntity();
        OrderFaqDto dto = new OrderFaqDto();
        dto.setQuestion("Вопрос");

        when(orderFaqRepository.save(any(OrderFaqEntity.class)))
                .thenReturn(new OrderFaqEntity());

        orderFaqService.askQuestion(dto, user, order);

        verify(orderFaqRepository, times(1)).save(any(OrderFaqEntity.class));
    }

    // ── responseQuestion ─────────────────────────────────────────

    @Test
    void responseQuestion_shouldUpdateResponse_whenUserIsOrderCreator() {
        UserEntity creator = new UserEntity();
        creator.setId(1L);

        OrderEntity order = new OrderEntity();
        order.setId(100L);
        order.setCreator(creator);

        OrderFaqEntity faqEntity = new OrderFaqEntity();
        faqEntity.setId(10L);
        faqEntity.setOrder(order);
        faqEntity.setQuestion("Вопрос");

        OrderFaqDto dto = new OrderFaqDto();
        dto.setResponse("Ответ от владельца");

        when(orderFaqRepository.save(any(OrderFaqEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderFaqEntity result = orderFaqService.responseQuestion(creator, faqEntity, dto);

        assertThat(result.getResponse()).isEqualTo("Ответ от владельца");
        assertThat(result.getQuestion()).isEqualTo("Вопрос");
        verify(orderFaqRepository).save(faqEntity);
    }

    @Test
    void responseQuestion_shouldNotUpdateResponse_whenUserIsNotOrderCreator() {
        UserEntity creator = new UserEntity();
        creator.setId(1L);

        UserEntity stranger = new UserEntity();
        stranger.setId(2L);

        OrderEntity order = new OrderEntity();
        order.setId(100L);
        order.setCreator(creator);

        OrderFaqEntity faqEntity = new OrderFaqEntity();
        faqEntity.setId(10L);
        faqEntity.setOrder(order);
        faqEntity.setQuestion("Вопрос");

        OrderFaqDto dto = new OrderFaqDto();
        dto.setResponse("Попытка ответа от чужого пользователя");

        OrderFaqEntity result = orderFaqService.responseQuestion(stranger, faqEntity, dto);

        assertThat(result.getResponse()).isNull();
        verify(orderFaqRepository, never()).save(any(OrderFaqEntity.class));
    }

    @Test
    void responseQuestion_shouldNotUpdateResponse_whenUserIdDoesNotMatch() {
        UserEntity creator = new UserEntity();
        creator.setId(1L);

        UserEntity impostor = new UserEntity();
        impostor.setId(999L);

        OrderEntity order = new OrderEntity();
        order.setCreator(creator);

        OrderFaqEntity faqEntity = new OrderFaqEntity();
        faqEntity.setOrder(order);
        faqEntity.setQuestion("Вопрос");

        OrderFaqDto dto = new OrderFaqDto();
        dto.setResponse("Чужой ответ");

        OrderFaqEntity result = orderFaqService.responseQuestion(impostor, faqEntity, dto);

        assertThat(result.getResponse()).isNull();
        verify(orderFaqRepository, never()).save(any());
    }

    @Test
    void responseQuestion_shouldReturnSameEntityInstance_whenNotCreator() {
        UserEntity stranger = new UserEntity();
        stranger.setId(2L);

        UserEntity owner = new UserEntity();
        owner.setId(1L);

        OrderEntity order = new OrderEntity();
        order.setCreator(owner);

        OrderFaqEntity faqEntity = new OrderFaqEntity();
        faqEntity.setId(10L);
        faqEntity.setOrder(order);

        OrderFaqDto dto = new OrderFaqDto();
        dto.setResponse("Не должно сохраниться");

        OrderFaqEntity result = orderFaqService.responseQuestion(stranger, faqEntity, dto);

        assertThat(result).isSameAs(faqEntity);
    }

    // ── getQuestionEntity ───────────────────────────────────────

    @Test
    void getQuestionEntity_shouldReturnEntity_whenFound() {
        Long id = 5L;
        OrderFaqEntity faq = new OrderFaqEntity();
        faq.setId(id);
        faq.setQuestion("Тестовый вопрос");

        when(orderFaqRepository.findById(id)).thenReturn(Optional.of(faq));

        OrderFaqEntity result = orderFaqService.getQuestionEntity(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getQuestion()).isEqualTo("Тестовый вопрос");
        verify(orderFaqRepository).findById(id);
    }

    @Test
    void getQuestionEntity_shouldThrowNoSuchElementException_whenNotFound() {
        Long id = 999L;

        when(orderFaqRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderFaqService.getQuestionEntity(id))
                .isInstanceOf(NoSuchElementException.class);

        verify(orderFaqRepository).findById(id);
    }
}
