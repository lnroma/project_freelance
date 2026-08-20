package com.naumoff.rnc.services.order;

import com.naumoff.rnc.database.entities.order.OrderEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.cities.CityRepository;
import com.naumoff.rnc.database.repository.order.CategoryRepository;
import com.naumoff.rnc.database.repository.order.OrderRepository;
import com.naumoff.rnc.dto.order.CreateOrderDto;
import com.naumoff.rnc.specification.OrderSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    final private OrderRepository orderRepository;
    final private CityRepository cityRepository;
    final private CategoryRepository categoryRepository;

    /**
     * construct
     *
     * @param orderRepository order repository
     */
    public OrderService(
            OrderRepository orderRepository,
            CityRepository cityRepository,
            CategoryRepository categoryRepository
    ) {
        this.orderRepository = orderRepository;
        this.cityRepository = cityRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get order by id
     *
     * @param id Order id for load
     * @return OrderEntity
     */
    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * get available orders for user
     *
     * @return list orders entity
     */
    public List<OrderEntity> getAvailableOrders() {
        return orderRepository.findAll();
    }

    /**
     * get all orders for pagination in catalog
     *
     * @param pageable pagination
     * @return list or orders
     */
    public Page<OrderEntity> getOrders(Pageable pageable) {
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * Get orders by filters
     *
     * @param pageable Pageable object for pagination
     * @param query Search in title orders
     * @param categoryIds Filter by category ids
     * @param cityIds Filter by city ids
     * @param prices filter by prices
     *
     * @return page order entity
     */
    public Page<OrderEntity> getOrders(
            Pageable pageable,
            String query,
            List<Long> categoryIds,
            List<Long> cityIds,
            List<String> prices
    ) {
        if (query.isEmpty() &&
                categoryIds == null &&
                cityIds == null &&
                prices == null
        ) {
            return getOrders(pageable);
        }

        Long priceFrom = 0L;
        Long priceTo = 0L;
        if (prices != null && !prices.isEmpty()) {
            for (String price : prices) {
                String[] parts = price.split("-");
                if (priceFrom > Long.valueOf(parts[0])) {
                    priceFrom = Long.valueOf(parts[0]);
                }

                if (priceTo < Long.valueOf(parts[0])) {
                    priceTo = Long.valueOf(parts[1]);
                }
            }
        }

        if (priceFrom == 0L && priceTo == 0L) {
            priceFrom = null;
            priceTo = null;
        }

        Specification<OrderEntity> specification = OrderSpecifications.buildSpecifications(
                query,
                categoryIds,
                cityIds,
                priceFrom,
                priceTo
        );

        return orderRepository.findAll(specification, pageable);
    }

    /**
     * Filter orders by predicate
     *
     * @param title title for search
     * @param page current page
     * @param size size entity of page
     * @return list orderEntity
     */
    public Page<OrderEntity> filterByTitle(String title, int page, int size) {
        Specification<OrderEntity> specification = Specification.where(OrderSpecifications.hasTitleContaining(title));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return orderRepository.findAll(specification, pageable);
    }

    /**
     * get common count for orders
     *
     * @return count orders
     */
    public Long getCountOrders() {
        return orderRepository.count();
    }

    /**
     * Create new order
     *
     * @param createOrderDto order dto for create new order
     *
     * @return new order entity
     */
    public OrderEntity createOrder(
            CreateOrderDto createOrderDto,
            UserEntity currentUser
    ) {
        OrderEntity newOrder = new OrderEntity();

//        newOrder.setAuthorUserId(createOrderDto.getAuthorUserId());
        newOrder.setCreator(currentUser);

        newOrder.setCategory(categoryRepository.findById(createOrderDto.getCategoryId()).get());
        newOrder.setCity(cityRepository.findById(createOrderDto.getCityId()).get());
//        newOrder.setDeadlineAt(LocalDateTime.parse(createOrderDto.getDeadlineAt()));
        newOrder.setTitle(createOrderDto.getTitle());
        newOrder.setDescription(createOrderDto.getDescription());
        newOrder.setPriceFrom(createOrderDto.getPriceFrom().doubleValue());
        newOrder.setPriceTo(createOrderDto.getPriceTo().doubleValue());
        newOrder.setExecutor(currentUser);
//        newOrder.setAuthorUserId(-1L);
        orderRepository.save(newOrder);

        return newOrder;
    }
}
