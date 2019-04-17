package web.globalbeershop.exception;

import web.globalbeershop.data.Beer;

public class NoBeersInCartException extends Exception {
    private static final String DEFAULT_MESSAGE = "Your cart is empty";

    public NoBeersInCartException() {
        super(DEFAULT_MESSAGE);
    }

}
