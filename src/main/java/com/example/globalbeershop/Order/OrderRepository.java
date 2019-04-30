package com.example.globalbeershop.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    class OrderIDRowMapper implements RowMapper<Long> {

        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }

    }

    public Order addOrder(String name, String email, String address, String city, String couty, String pcode, String sessionID) {
        jdbcTemplate.update("INSERT INTO orderDetails (name, email, address, city, county, pcode, sessionId) VALUES (?, ?, ?, ?, ?, ?, ?)",
                name, email, address, city, couty, pcode, sessionID);
        List<Long> newOrderID = jdbcTemplate.query("SELECT id FROM orderDetails WHERE sessionId = ? ", new Object[]{sessionID}, new OrderIDRowMapper());
        Long orderID = newOrderID.get(0);
        jdbcTemplate.update("UPDATE orderDetails SET sessionId = NULL WHERE id = ?", orderID);
        return new Order(orderID, name, email, address, city, couty, pcode);
    }



    public void addItemToOrder(Long orderID, OrderItem item) {
        jdbcTemplate.update("INSERT INTO beerOrdered (orderId, beerId, name, price, quantity) VALUES (?, ?, ?, ?, ?)",
                orderID, item.getBeerID(), item.getName(), item.getPrice(), item.getQuantity());
    }

}