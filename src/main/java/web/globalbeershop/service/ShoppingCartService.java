package web.globalbeershop.service;

import web.globalbeershop.data.Beer;
import web.globalbeershop.exception.NotEnoughBeersInStockException;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addBeer(Beer beer, Integer quantity);

    void updateBeer(Beer beer, Integer quantity);

    void removeBeer(Beer beer);

    Map<Beer, Integer> getBeersInCart();

    void checkout() throws NotEnoughBeersInStockException;

    Double getTotal();
}
