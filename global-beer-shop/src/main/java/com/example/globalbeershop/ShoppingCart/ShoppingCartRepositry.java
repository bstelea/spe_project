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
            return new CartItem(rs.getLong("beer"), rs.getDouble("price"), rs.getInt("quantity"));
        }

    }

    class AvailableStockRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

            return rs.getInt("availableStock");
        }

    }

    class alreadyInSessionCartChecker implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

            return rs.getInt("quantity");
        }

    }
    public ShoppingCart findSessionShoppingCart (String sessionId) {
        List<CartItem> results = jdbcTemplate.query(
                "SELECT ShoppingCartItems.sessionId AS id, ShoppingCartItems.beerId AS beer, ShoppingCartItems.quantity AS quantity, BeerStocked.price AS price FROM ShoppingCartItems JOIN BeerStocked ON ShoppingCartItems.beerId = BeerStocked.id WHERE ShoppingCartItems.sessionId = ?",
                new Object[]{sessionId}, new ShoppingCartRowMapper());

        ShoppingCart cart = new ShoppingCart(sessionId);

        for(CartItem i : results) cart.addItem(i.getItemID(),i.getPrice(), i.getQuantity());

        return cart;
    }

    public boolean addItemToCart (String sessionId, String beerId, String quantity) {

            List<Integer> alreadyInCart = jdbcTemplate.query("SELECT quantity FROM ShoppingCartItems WHERE sessionId = ? AND beerId = ?",
                    new Object[]{sessionId, beerId}, new alreadyInSessionCartChecker());

            if (!alreadyInCart.isEmpty() && (hasAvailableStock(beerId, alreadyInCart.get(0),  quantity))){
                jdbcTemplate.update("UPDATE ShoppingCartItems SET quantity = quantity + ? WHERE sessionId = ? AND beerId = ?",
                        quantity, sessionId, beerId);
                return true;
            }
            if(alreadyInCart.isEmpty() && hasAvailableStock(beerId, 0, quantity)){
                    jdbcTemplate.update("INSERT INTO ShoppingCartItems (sessionId, beerId, quantity) VALUES (?, ?, ?)",
                            sessionId, beerId, quantity);
                    return true;

            }


        return false;
    }

    private boolean hasAvailableStock (String beerId, Integer quantityInCart, String quantityToAdd) throws IllegalArgumentException {
        List<Integer> stock = jdbcTemplate.query("SELECT id, availableStock FROM BeerStocked WHERE id = ? and availableStock >= ?+?", new Object[]{beerId,quantityInCart, quantityToAdd}, new AvailableStockRowMapper());
        if(stock.isEmpty()) return false;
        return true;
    }



}
