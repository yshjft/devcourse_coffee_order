package com.devcourse.coffeeorder.domain.order.dao.order;

import static com.devcourse.coffeeorder.global.util.Util.toLocalDateTime;
import static com.devcourse.coffeeorder.global.util.Util.toUUID;

import java.time.LocalDateTime;
import java.util.*;

import com.devcourse.coffeeorder.domain.order.entity.order.Order;
import com.devcourse.coffeeorder.domain.order.entity.order.OrderStatus;
import com.devcourse.coffeeorder.global.exception.customexception.CreationException;
import com.devcourse.coffeeorder.global.exception.customexception.UpdateException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderJdbcRepository implements OrderRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order create(Order order) {
        int update = jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                "values(UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));

        if(update != 1) {
            throw new CreationException("failed to create Order!");
        }

        return order;
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query("SELECT * FROM orders", orderRowMapper);
    }

    @Override
    public List<Order> findByStatus(OrderStatus orderStatus) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE order_status = :orderStatus ORDER BY created_at",
                Collections.singletonMap("orderStatus", orderStatus.toString()), orderRowMapper);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                            Collections.singletonMap("orderId", orderId.toString().getBytes()), orderRowMapper)
            );
        }catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE email = :email ORDER BY created_at DESC",
                Collections.singletonMap("email", email), orderRowMapper);
    }

    @Override
    public Order update(Order order) {
        int update = jdbcTemplate.update("UPDATE orders SET address = :address, postcode = :postcode, order_status = :orderStatus, updated_at = :updatedAt WHERE order_id = UUID_TO_BIN(:orderId)",
                toOrderParamMap(order));

        if(update != 1) {
            throw new UpdateException(String.format("failed to update order(%s)", order.getOrderId().toString()));
        }

        return order;
    }

    @Override
    public void orderAcceptedToPreparingForShipment(LocalDateTime time) {
        jdbcTemplate.update("UPDATE orders SET order_status = 'PREPARING_FOR_SHIPMENT', updated_at = NOW() WHERE created_at <= :time and order_status = 'ORDER_ACCEPTED'",
                Collections.singletonMap("time", time));
    }

    @Override
    public void delete(Order order) {
        jdbcTemplate.update("DELETE FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", order.getOrderId().toString().getBytes()));
    }

    private final RowMapper<Order> orderRowMapper = ((resultSet, i)->{
        UUID orderId = toUUID(resultSet.getBytes("order_id"));
        String email = resultSet.getString("email");
        String address = resultSet.getString("address");
        String postcode = resultSet.getString("postcode");
        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        LocalDateTime createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));

        return Order.builder()
                .orderId(orderId)
                .email(email)
                .address(address)
                .postcode(postcode)
                .orderStatus(orderStatus)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    });

    private Map<String, Object> toOrderParamMap(Order order) {
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }
}
