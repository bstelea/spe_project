package web.globalbeershop.exception;

import web.globalbeershop.data.Beer;

public class NotEnoughBeersInStockException extends Exception {

    private static final String DEFAULT_MESSAGE = "Not enough products in stock";

    public NotEnoughBeersInStockException() {
        super(DEFAULT_MESSAGE);
    }

    public NotEnoughBeersInStockException(Beer beer) {
        super(String.format("Not enough %s beers in stock. Only %d left", beer.getName(), beer.getStock()));
    }
}
