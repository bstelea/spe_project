package web.globalbeershop;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mysql.cj.exceptions.AssertionFailedException;
import org.hibernate.AssertionFailure;
import org.hibernate.boot.model.relational.Database;
import org.junit.Before;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import web.globalbeershop.controller.CartController;
import web.globalbeershop.data.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.service.ShoppingCartService;
import web.globalbeershop.service.impl.ShoppingCartServiceImpl;

import javax.inject.Inject;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTest {

    @Mock
    BeerRepository beerRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    @Test
    public void newCartIsEmptyTest() {
        Assert.assertTrue(shoppingCartService.getBeersInCart().isEmpty());
        Assert.assertTrue(shoppingCartService.getTotal() == 0);
    }

    @Autowired
    @Test
    public void addOneItemToCartTest() {
        Beer beer = new Beer(70L, "Heineken", 5.0, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");

        shoppingCartService.addBeer(beer, 1);
        Assert.assertFalse(shoppingCartService.getBeersInCart().isEmpty());
        Assert.assertTrue(shoppingCartService.getTotal() == 5);
        shoppingCartService.removeBeer(beer);
    }

    @Test
    public void removeItemFromCartTest() {
        Beer beer = new Beer(70L, "Heineken", 5.0, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");

        shoppingCartService.addBeer(beer, 100);
        Assert.assertFalse(shoppingCartService.getBeersInCart().isEmpty());
        shoppingCartService.removeBeer(beer);
        Assert.assertTrue(shoppingCartService.getBeersInCart().isEmpty());
    }

    @Test
    public void gettingTotalPriceOfMultipleBeersInCartTest() {
        Beer beer1 = new Beer(1L, "Heineken", 5.0, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");
        Beer beer2 = new Beer(2L, "Brewdog", 3.0, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");
        Beer beer3 = new Beer(3L, "Guinness", 2.43, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");
        Beer beer4 = new Beer(4L, "Stella Artois", 7.52, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");

        shoppingCartService.addBeer(beer1, 1);
        shoppingCartService.addBeer(beer2, 1);
        shoppingCartService.addBeer(beer3, 1);
        shoppingCartService.addBeer(beer4, 1);

        Assert.assertTrue(shoppingCartService.getTotal() == 17.95);
        shoppingCartService.removeBeer(beer1);
        Assert.assertTrue(shoppingCartService.getTotal() == 12.95);
        shoppingCartService.removeBeer(beer2);
        Assert.assertTrue(shoppingCartService.getTotal() == 9.95);
        shoppingCartService.removeBeer(beer3);
        shoppingCartService.removeBeer(beer4);
        Assert.assertTrue(shoppingCartService.getTotal() == 0);
    }

    @Test
    public void increasingQuantityOfBeerInCartTest() {
        Beer beer = new Beer(1L, "Heineken", 5.0, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");

        shoppingCartService.addBeer(beer, 1);
        Assert.assertTrue(shoppingCartService.getTotal() == 5);
        shoppingCartService.addBeer(beer, 1);
        Assert.assertTrue(shoppingCartService.getTotal() == 10);
        shoppingCartService.updateBeer(beer, 3);
        Assert.assertTrue(shoppingCartService.getTotal() == 15);
        shoppingCartService.updateBeer(beer, 1);
        Assert.assertTrue(shoppingCartService.getTotal() == 5);

        shoppingCartService.removeBeer(beer);
    }

    @Test
    public void updateBeerInCartToZeroDoesNotWorkTest() {
        Beer beer = new Beer(1L, "Heineken", 5.0, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer");

        shoppingCartService.addBeer(beer, 4);
        Assert.assertTrue(shoppingCartService.getTotal() == 20);
        shoppingCartService.updateBeer(beer, 0);
        Assert.assertTrue(shoppingCartService.getTotal() == 20);
        shoppingCartService.removeBeer(beer);
        Assert.assertTrue(shoppingCartService.getBeersInCart().isEmpty());
    }

    @Test
    public void validateCartWithIncorrectQuantityTest() {
        Optional<Beer> beer = Optional.of(new Beer(1L, "Heineken", 5.0, 2, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer"));
        when(beerRepository.findById(any())).thenReturn(null);
        when(beerRepository.findById(1L)).thenReturn(beer);

        shoppingCartService.addBeer(beer.get(), 3);

        try {
            shoppingCartService.validateCart();
        } catch (NotEnoughBeersInStockException expectedException) {
            Assert.assertTrue(shoppingCartService.getTotal() == 15);
            shoppingCartService.removeBeer(beer.get());
        } catch (NoBeersInCartException unexpectedException) {
            System.out.println("Mock repository error");
        }
        shoppingCartService.removeBeer(beer.get());
    }

    @Test
    public void validateCartWithCorrectQuantityTest() throws NotEnoughBeersInStockException, NoBeersInCartException {
        Optional<Beer> beer = Optional.of(new Beer(1L, "Heineken", 5.0, 2, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer"));
        when(beerRepository.findById(any())).thenReturn(null);
        when(beerRepository.findById(1L)).thenReturn(beer);

        shoppingCartService.addBeer(beer.get(), 1);

        Assert.assertTrue(shoppingCartService.getTotal() == 5);
        shoppingCartService.validateCart();
        shoppingCartService.removeBeer(beer.get());
    }

    @Test
    public void validateCartWithMultipleBeersTest() throws NotEnoughBeersInStockException, NoBeersInCartException {
        Optional<Beer> beer1 = Optional.of(new Beer(1L, "Heineken", 5.0, 1, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer"));
        Optional<Beer> beer2 = Optional.of(new Beer(2L, "Coors Light", 3.0, 10, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer"));
        Optional<Beer> beer3 = Optional.of(new Beer(3L, "Peroni", 5.50, 100, "Netherlands", "Heineken Brewers", "Lager", 5.0, "/img/beer-template.jpg", "This is the description for the Heineken beer"));

        when(beerRepository.findById(any())).thenReturn(null);
        when(beerRepository.findById(1L)).thenReturn(beer1);
        when(beerRepository.findById(2L)).thenReturn(beer2);
        when(beerRepository.findById(3L)).thenReturn(beer3);

        shoppingCartService.addBeer(beer1.get(), 1);
        shoppingCartService.addBeer(beer2.get(), 9);
        shoppingCartService.addBeer(beer3.get(), 73);

        Assert.assertTrue(shoppingCartService.getTotal() == 433.5);
        shoppingCartService.validateCart();
        shoppingCartService.removeBeer(beer1.get());
        shoppingCartService.removeBeer(beer2.get());
        shoppingCartService.removeBeer(beer3.get());
    }

    @Test
    public void validateCartWithNoBeersTest() {
        try {
            shoppingCartService.validateCart();
        }
        catch (NotEnoughBeersInStockException unexpectedException) {
            System.out.println("Mock repository error");
        }
        catch (NoBeersInCartException expectedException) {
            Assert.assertTrue(shoppingCartService.getTotal() == 0);
            Assert.assertTrue(shoppingCartService.getBeersInCart().isEmpty());
        }
    }
}
