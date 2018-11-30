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
            return new CartItem(rs.getLong("id"), rs.getInt("quantity"));
        }

    }

    public ShoppingCart findUserShoppingCart (long id) {
        List<CartItem> results = jdbcTemplate.query("SELECT * FROM ShoppingCartItems WHERE id = ?", new ShoppingCartRowMapper());

        ShoppingCart cart = new ShoppingCart(id);

        for(CartItem i : results) cart.addItem(i.getItemID(), i.getQuantity());

        return cart;
    }


}
