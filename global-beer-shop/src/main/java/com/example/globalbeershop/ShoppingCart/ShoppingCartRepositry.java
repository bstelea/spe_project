package com.example.globalbeershop.ShoppingCart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartRepositry {

    @Autowired
    JdbcTemplate jdbcTemplate;

    class ShoppingCartRowMapper implements RowMapper<CartItem> {

        @Override
        public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItem(rs.getLong("id"), rs.getDouble("price"), rs.getInt("quantity"));
        }

    }

    public ShoppingCart findSessionShoppingCart (String sessionId) {
        List<CartItem> results = jdbcTemplate.query(
                "SELECT ShoppingCartItems.sessionId AS id, ShoppingCartItems.quantity AS quantity, BeerStocked.price AS price FROM ShoppingCartItems JOIN BeerStocked ON ShoppingCartItems.beerId = BeerStocked.id WHERE ShoppingCartItems.sessionId = ?",
                new Object[]{sessionId}, new ShoppingCartRowMapper());

        ShoppingCart cart = new ShoppingCart(sessionId);

        for(CartItem i : results) cart.addItem(i.getItemID(),i.getPrice(), i.getQuantity());

        return cart;
    }


}
