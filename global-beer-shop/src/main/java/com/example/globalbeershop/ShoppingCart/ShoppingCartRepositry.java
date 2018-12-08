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

    /**
     * Used to get a session's shopping cart
     */
    class ShoppingCartRowMapper implements RowMapper<CartItem> {

        @Override
        public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItem(rs.getLong("beer"), rs.getDouble("price"), rs.getInt("quantity"));
        }

    }

    /**
     * Used to check the available stock of an item
     */
    class AvailableStockRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {

            return rs.getInt("availableStock");
        }

    }

    /**
     * Used to check if an item is already in a session's cart
     */
    class alreadyInSessionCartChecker implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("quantity");
        }

    }

    /**
     * Gets the contents of a given session's shopping cart
     * @param sessionId The session who's shopping cart to get
     * @return A list containing all cartItems corresponding to the given session
     */
    public ShoppingCart findSessionShoppingCart (String sessionId) {
        List<CartItem> results = jdbcTemplate.query(
                "SELECT ShoppingCartItems.sessionId AS id, ShoppingCartItems.beerId AS beer, ShoppingCartItems.quantity AS quantity, BeerStocked.price AS price FROM ShoppingCartItems JOIN BeerStocked ON ShoppingCartItems.beerId = BeerStocked.id WHERE ShoppingCartItems.sessionId = ?",
                new Object[]{sessionId}, new ShoppingCartRowMapper());

        ShoppingCart cart = new ShoppingCart(sessionId);

        for(CartItem i : results) cart.addItem(i.getItemID(),i.getPrice(), i.getQuantity());

        return cart;
    }

    /**
     * If possible, adds beerId to the cart in the given quantity (increases the existing quantity if already in cart)
     * @param sessionId The id of the session requesting to add an item
     * @param beerId The id of the beer to add to the cart
     * @param quantity The quantity of the given beer to add
     * @return True if the shopping cart was updated, False otherwise
     */
    public boolean addItemToCart (String sessionId, String beerId, String quantity) {

            //do not allow adding items in quantities <= 0
            if(Integer.parseInt(quantity) <= 0 ) return false;

            //tries to get the quantity of the given beerId already in the cart
            List<Integer> alreadyInCart = jdbcTemplate.query("SELECT quantity FROM ShoppingCartItems WHERE sessionId = ? AND beerId = ?",
                    new Object[]{sessionId, beerId}, new alreadyInSessionCartChecker());

            /*if the results aren't empty (e.g. the item was already in the stock),
            and there is enough available stock of the item (including the amount already in the cart)
             */
            if (!alreadyInCart.isEmpty() && (hasAvailableStock(beerId, alreadyInCart.get(0),  quantity))){
                //adds
                jdbcTemplate.update("UPDATE ShoppingCartItems SET quantity = quantity + ? WHERE sessionId = ? AND beerId = ?",
                        quantity, sessionId, beerId);
                return true;
            }


            /*if the results are empty (e.g. item wasn't already in the cart)
                and there is enough available stock for the quantity on its own
             */
            if(alreadyInCart.isEmpty() && hasAvailableStock(beerId, 0, quantity)){
                    jdbcTemplate.update("INSERT INTO ShoppingCartItems (sessionId, beerId, quantity) VALUES (?, ?, ?)",
                            sessionId, beerId, quantity);
                    return true;

            }

        //if nothing changed in the shopping cart
        return false;
    }

    /**
     * Checks if there is enough available stock of beerId to add a given quantity to a shopping cart
     * @param beerId The item being checked
     * @param quantityInCart The quantity of item already in some cart
     * @param quantityToAdd The quantity of the item trying to be added to the cart
     * @return True if quantityInCart + quantityToAdd <= available stock, false otherwise
     * @throws IllegalArgumentException Invalid request
     */
    private boolean hasAvailableStock (String beerId, Integer quantityInCart, String quantityToAdd) throws IllegalArgumentException {
        List<Integer> stock = jdbcTemplate.query("SELECT id, availableStock FROM BeerStocked WHERE id = ? and availableStock >= ?+?", new Object[]{beerId,quantityInCart, quantityToAdd}, new AvailableStockRowMapper());

        //if results empty, that means there isn't sufficient available stock
        if(stock.isEmpty()) return false;
        return true;
    }


    public boolean removeItemFromCart (String sessionId, String beerId) {


        //checks if item already in the cart
        List<Integer> alreadyInCart = jdbcTemplate.query("SELECT quantity FROM ShoppingCartItems WHERE sessionId = ? AND beerId = ?",
                new Object[]{sessionId, beerId}, new alreadyInSessionCartChecker());

        //if the results aren't empty (e.g. the item was in the cart)
        if (!alreadyInCart.isEmpty()) {
            //removes item
            jdbcTemplate.update("DELETE FROM ShoppingCartItems WHERE sessionId = ? AND beerId = ?", sessionId, beerId);
            return true;
        }

        //if items was not in the cart in the first place/session does not have a cart yet
        return false;
    }
}
