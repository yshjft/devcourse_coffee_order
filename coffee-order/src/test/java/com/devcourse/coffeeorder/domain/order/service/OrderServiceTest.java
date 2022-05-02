package com.devcourse.coffeeorder.domain.order.service;

import static com.devcourse.coffeeorder.TestData.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.devcourse.coffeeorder.domain.order.dao.order.OrderRepository;
import com.devcourse.coffeeorder.domain.order.dao.orderitem.OrderItemRepository;
import com.devcourse.coffeeorder.global.exception.OrderNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("주문 상세 조회 예외 테스트")
    void testGetOrderDetailException() {
        UUID orderId = UUID.randomUUID();

        try {
            when(orderRepository.findById(orderId)).thenThrow(new OrderNotFoundException());

            orderService.getOrderDetail(orderId);
        }catch (OrderNotFoundException e) {
            verify(orderItemRepository, never()).findByIdWithProduct(orderId);
        }
    }

    @Test
    @DisplayName("주문 상세 조회 테스트")
    void testGetOrderDetail() {
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderItemRepository.findByIdWithProduct(order.getOrderId())).thenReturn(Arrays.asList(orderItem3, orderItem4));

        orderService.getOrderDetail(order.getOrderId());

        verify(orderItemRepository).findByIdWithProduct(order.getOrderId());
    }
}