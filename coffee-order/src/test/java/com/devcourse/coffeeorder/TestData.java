package com.devcourse.coffeeorder;

import com.devcourse.coffeeorder.domain.order.dto.order.OrderUpdateReqDto;
import com.devcourse.coffeeorder.domain.order.entity.order.Order;
import com.devcourse.coffeeorder.domain.order.entity.orderitem.OrderItem;
import com.devcourse.coffeeorder.domain.order.entity.order.OrderStatus;
import com.devcourse.coffeeorder.domain.product.entity.Category;
import com.devcourse.coffeeorder.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestData {
    public static Product product = Product.builder()
            .productId(UUID.randomUUID())
            .productName("test coffee")
            .category(Category.COFFEE_BEAN_PACKAGE)
            .price(2500)
            .description("best coffee!")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static Product product2 = Product.builder()
            .productId(UUID.randomUUID())
            .productName("test coffee2")
            .category(Category.COFFEE_BEAN_PACKAGE)
            .price(1000)
            .description("cheap coffee!")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static Product coffee = Product.builder()
            .productId(UUID.randomUUID())
            .productName("test coffee")
            .category(Category.COFFEE_BEAN_PACKAGE)
            .price(2500)
            .description("best coffee!")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static Product cookie = Product.builder()
            .productId(UUID.randomUUID())
            .productName("test cookie")
            .category(Category.COOKIE)
            .price(1000)
            .description("best cookie!")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static Order order = Order.builder()
            .orderId(UUID.randomUUID())
            .email("tester@test.com")
            .address("경기도 용인시 처이군 ~~")
            .postcode("12345")
            .orderStatus(OrderStatus.ORDER_ACCEPTED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static Order order2 = Order.builder()
            .orderId(UUID.randomUUID())
            .email("tester@test.com")
            .address("경기도 용인시 ㅁㅁ ~~")
            .postcode("45678")
            .orderStatus(OrderStatus.ORDER_ACCEPTED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static Order order3 = Order.builder()
            .orderId(UUID.randomUUID())
            .email("tester@test.com")
            .address("경기도 용인시 처이군 ~~")
            .postcode("12345")
            .orderStatus(OrderStatus.ORDER_CANCELLED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static OrderItem orderItem1 = OrderItem.builder()
            .orderId(order.getOrderId())
            .productId(coffee.getProductId())
            .quantity(3)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static OrderItem orderItem2 = OrderItem.builder()
            .orderId(order.getOrderId())
            .productId(cookie.getProductId())
            .quantity(10)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static OrderItem orderItem3 = OrderItem.builder()
            .orderItemId(1L)
            .orderId(order.getOrderId())
            .productId(coffee.getProductId())
            .quantity(3)
            .product(coffee)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static OrderItem orderItem4 = OrderItem.builder()
            .orderItemId(2L)
            .productId(cookie.getProductId())
            .quantity(10)
            .product(cookie)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    public static OrderUpdateReqDto orderUpdateReqDto = new OrderUpdateReqDto("경기도 수원시 ", "00000");
}
