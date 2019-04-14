package web.globalbeershop;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import web.globalbeershop.data.Beer;
import web.globalbeershop.data.Review;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.service.impl.ShoppingCartServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartTest {
    @Mock
    BeerRepository beerRepository;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    public CartTest() {
    }

    @Test
    public void newCartIsEmptyTest() {
        Assert.assertTrue(this.shoppingCartService.getBeersInCart().isEmpty());
        Assert.assertTrue(this.shoppingCartService.getTotal() == 0.0D);
    }

    @Test
    public void addOneItemToCartTest() {
        List<Review> reviews = new ArrayList();
        Beer beer = new Beer(1L, "Heineken", 5.0D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews);
        this.shoppingCartService.addBeer(beer, 1);
        Assert.assertFalse(this.shoppingCartService.getBeersInCart().isEmpty());
        Assert.assertTrue(this.shoppingCartService.getTotal() == 5.0D);
        this.shoppingCartService.removeBeer(beer);
    }

    @Test
    public void removeItemFromCartTest() {
        List<Review> reviews = new ArrayList();
        Beer beer = new Beer(1L, "Heineken", 5.0D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews);
        this.shoppingCartService.addBeer(beer, 100);
        Assert.assertFalse(this.shoppingCartService.getBeersInCart().isEmpty());
        this.shoppingCartService.removeBeer(beer);
        Assert.assertTrue(this.shoppingCartService.getBeersInCart().isEmpty());
    }

    @Test
    public void gettingTotalPriceOfMultipleBeersInCartTest() {
        List<Review> reviews1 = new ArrayList();
        Beer beer1 = new Beer(1L, "Heineken", 5.0D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews1);
        List<Review> reviews2 = new ArrayList();
        Beer beer2 = new Beer(2L, "Coors Light", 3.0D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews2);
        List<Review> reviews3 = new ArrayList();
        Beer beer3 = new Beer(3L, "Guinness", 2.43D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews3);
        List<Review> reviews4 = new ArrayList();
        Beer beer4 = new Beer(4L, "Stella Artois", 7.52D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews4);
        this.shoppingCartService.addBeer(beer1, 1);
        this.shoppingCartService.addBeer(beer2, 1);
        this.shoppingCartService.addBeer(beer3, 1);
        this.shoppingCartService.addBeer(beer4, 1);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 17.95D);
        this.shoppingCartService.removeBeer(beer1);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 12.95D);
        this.shoppingCartService.removeBeer(beer2);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 9.95D);
        this.shoppingCartService.removeBeer(beer3);
        this.shoppingCartService.removeBeer(beer4);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 0.0D);
    }

    @Test
    public void increasingQuantityOfBeerInCartTest() {
        List<Review> reviews = new ArrayList();
        Beer beer = new Beer(1L, "Heineken", 5.0D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews);
        this.shoppingCartService.addBeer(beer, 1);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 5.0D);
        this.shoppingCartService.addBeer(beer, 1);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 10.0D);
        this.shoppingCartService.updateBeer(beer, 3);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 15.0D);
        this.shoppingCartService.updateBeer(beer, 1);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 5.0D);
        this.shoppingCartService.removeBeer(beer);
    }

    @Test
    public void updateBeerInCartToZeroDoesNotWorkTest() {
        List<Review> reviews = new ArrayList();
        Beer beer = new Beer(1L, "Heineken", 5.0D, 3, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews);
        this.shoppingCartService.addBeer(beer, 4);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 20.0D);
        this.shoppingCartService.updateBeer(beer, 0);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 20.0D);
        this.shoppingCartService.removeBeer(beer);
        Assert.assertTrue(this.shoppingCartService.getBeersInCart().isEmpty());
    }

    @Test
    public void validateCartWithIncorrectQuantityTest() {
        List<Review> reviews = new ArrayList();
        Optional<Beer> beer = Optional.of(new Beer(1L, "Heineken", 5.0D, 2, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews));
        Mockito.when(this.beerRepository.findById((Long)ArgumentMatchers.any())).thenReturn(null);
        Mockito.when(this.beerRepository.findById(1L)).thenReturn(beer);
        this.shoppingCartService.addBeer((Beer)beer.get(), 3);

        try {
            this.shoppingCartService.validateCart();
        } catch (NotEnoughBeersInStockException var4) {
            Assert.assertTrue(this.shoppingCartService.getTotal() == 15.0D);
            this.shoppingCartService.removeBeer((Beer)beer.get());
        } catch (NoBeersInCartException var5) {
            System.out.println("Mock repository error");
        }

        this.shoppingCartService.removeBeer((Beer)beer.get());
    }

    @Test
    public void validateCartWithCorrectQuantityTest() throws NotEnoughBeersInStockException, NoBeersInCartException {
        List<Review> reviews = new ArrayList();
        Optional<Beer> beer = Optional.of(new Beer(1L, "Heineken", 5.0D, 2, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews));
        Mockito.when(this.beerRepository.findById((Long)ArgumentMatchers.any())).thenReturn(null);
        Mockito.when(this.beerRepository.findById(1L)).thenReturn(beer);
        this.shoppingCartService.addBeer((Beer)beer.get(), 1);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 5.0D);
        this.shoppingCartService.validateCart();
        this.shoppingCartService.removeBeer((Beer)beer.get());
    }

    @Test
    public void validateCartWithMultipleBeersTest() throws NotEnoughBeersInStockException, NoBeersInCartException {
        List<Review> reviews1 = new ArrayList();
        Optional<Beer> beer1 = Optional.of(new Beer(1L, "Heineken", 5.0D, 1, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews1));
        List<Review> reviews2 = new ArrayList();
        Optional<Beer> beer2 = Optional.of(new Beer(2L, "Coors Light", 3.0D, 10, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews2));
        List<Review> reviews = new ArrayList();
        Optional<Beer> beer3 = Optional.of(new Beer(3L, "Peroni", 5.5D, 100, "Netherlands", "Heineken Brewers", "Lager", 5.0D, "/img/beer-template.jpg", "This is the description for the Heineken beer", "Europe", 5.0D, reviews));
        Mockito.when(this.beerRepository.findById((Long)ArgumentMatchers.any())).thenReturn(null);
        Mockito.when(this.beerRepository.findById(1L)).thenReturn(beer1);
        Mockito.when(this.beerRepository.findById(2L)).thenReturn(beer2);
        Mockito.when(this.beerRepository.findById(3L)).thenReturn(beer3);
        this.shoppingCartService.addBeer((Beer)beer1.get(), 1);
        this.shoppingCartService.addBeer((Beer)beer2.get(), 9);
        this.shoppingCartService.addBeer((Beer)beer3.get(), 73);
        Assert.assertTrue(this.shoppingCartService.getTotal() == 433.5D);
        this.shoppingCartService.validateCart();
        this.shoppingCartService.removeBeer((Beer)beer1.get());
        this.shoppingCartService.removeBeer((Beer)beer2.get());
        this.shoppingCartService.removeBeer((Beer)beer3.get());
    }

    @Test
    public void validateCartWithNoBeersTest() {
        try {
            this.shoppingCartService.validateCart();
        } catch (NotEnoughBeersInStockException var2) {
            System.out.println("Mock repository error");
        } catch (NoBeersInCartException var3) {
            Assert.assertTrue(this.shoppingCartService.getTotal() == 0.0D);
            Assert.assertTrue(this.shoppingCartService.getBeersInCart().isEmpty());
        }

    }
}
