package com.devcourse.coffeeorder.domain.order.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.devcourse.coffeeorder.domain.order.dto.orderitem.OrderItemCreateReqDto;
import com.devcourse.coffeeorder.domain.order.entity.order.Order;
import com.devcourse.coffeeorder.domain.order.entity.order.OrderStatus;
import com.devcourse.coffeeorder.global.exception.customexception.badrequest.WrongInputException;
import org.springframework.util.StringUtils;

import static com.devcourse.coffeeorder.global.util.Util.*;

public class OrderCreateReqDto {
    private String email;
    private String address;
    private String postcode;
    private List<OrderItemCreateReqDto> orderItems;

    public OrderCreateReqDto(String email, String address, String postcode, List<OrderItemCreateReqDto> orderItems) {
        validateParams(email, address, postcode, orderItems);
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
    }

    public static OrderCreateReqDtoBuilder builder() {
        return new OrderCreateReqDtoBuilder();
    }

    public static class OrderCreateReqDtoBuilder {
        private String email;
        private String address;
        private String postcode;
        private List<OrderItemCreateReqDto> orderItems;

        public OrderCreateReqDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public OrderCreateReqDtoBuilder address(String address) {
            this.address = address;
            return this;
        }

        public OrderCreateReqDtoBuilder postcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public OrderCreateReqDtoBuilder orderItems(List<OrderItemCreateReqDto> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public OrderCreateReqDto build() {
            return new OrderCreateReqDto(email, address, postcode, orderItems);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public List<OrderItemCreateReqDto> getOrderItems() {
        return orderItems;
    }

    public Order toEntity() {
        return Order.builder()
                .orderId(UUID.randomUUID())
                .email(email)
                .address(address)
                .postcode(postcode)
                .orderStatus(OrderStatus.ORDER_ACCEPTED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void validateParams(String email, String address, String postcode, List<OrderItemCreateReqDto> orderItems) {
        if(!StringUtils.hasText(email) || email.length() > 50) {
            throw new WrongInputException("email must not be blank! (1 <= email <= 50)");
        }
        if(!isValidEmail(email)) {
            throw new WrongInputException("wrong email format!");
        }
        if(!StringUtils.hasText(address) || address.length() > 200) {
            throw new WrongInputException("address must not be blank! (1 <= address <= 200)");
        }
        if(!StringUtils.hasText(postcode) || postcode.length() > 200) {
            throw new WrongInputException("postcode must not be blank! (1 <= postcode <= 200)");
        }
        if(orderItems == null || orderItems.size() == 0) {
            throw new WrongInputException("orderItems must have at least 1 element");
        }
    }
}
