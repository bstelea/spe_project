package web.globalbeershop;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.web.context.WebApplicationContext;
import web.globalbeershop.data.Beer;
import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.service.ShoppingCartService;
import web.globalbeershop.service.impl.ShoppingCartServiceImpl;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@Transactional
public class BeerTest {

    @Mock
    BeerRepository beerRepository;

    @InjectMocks
    ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void basicTest() {
        Optional<Beer> beer = beerRepository.findById(1L);
        if (beer.isPresent()) {
            Assert.assertTrue(beerRepository.count() == 30L);
        }
    }

    @Test
    public void createBeerTest() {
        Beer beer = new Beer();
        beer.setName("Coombe");
        beer.setPrice(4.5);
        beer.setStock(23);
        beer.setCountry("Finland");
        beer.setBrewer("Coombe brewer");
        beer.setType("Ale");
        beer.setAbv(8.0);

        Optional<Beer> beer2 = beerRepository.findById(1L);
        if (beer2.isPresent()) {
            Assert.assertTrue(beer.getName().equals(beer2.get().getName()));
        }
    }

    @Test
    public void shoppingCartServiceInitTest() {
        Assert.assertTrue(shoppingCartService.getBeersInCart().isEmpty());
        Assert.assertTrue(shoppingCartService.getTotal() == 0);
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addBeerToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(1L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 1);
            Assert.assertTrue(shoppingCartService.getTotal() == beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get().getName()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addNegativeQuantityBeerToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(1L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), -1);
            Assert.assertTrue(shoppingCartService.getTotal() == 0);
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 0);
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addZeroQuantityBeerToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(1L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 0);
            Assert.assertTrue(shoppingCartService.getTotal() == 0);
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 0);
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(1L);
        Optional<Beer> beer2 = beerRepository.findById(5L);
        Optional<Beer> beer3 = beerRepository.findById(10L);

        if (beer.isPresent() && beer2.isPresent() && beer3.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            shoppingCartService.addBeer(beer2.get(), 5);
            shoppingCartService.addBeer(beer3.get(), 9);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice() + 9 * beer3.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 3);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer3.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersWithIllegalQuantityToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(1L);
        Optional<Beer> beer2 = beerRepository.findById(9L);
        Optional<Beer> beer3 = beerRepository.findById(24L);

        if (beer.isPresent() && beer2.isPresent() && beer3.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            shoppingCartService.addBeer(beer2.get(), -5);
            shoppingCartService.addBeer(beer3.get(), 9);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 9 * beer3.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer3.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addTheSameBeerMultipleTimesToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(9L);
        Optional<Beer> beer2 = beerRepository.findById(9L);

        if (beer.isPresent() && beer2.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 8 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMoreBeersThanStockToShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(9L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 1000);
            Assert.assertTrue(shoppingCartService.getTotal() == 1000 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addBeerToShoppingCartAndUpdateQuantityTest() {
        Optional<Beer> beer = beerRepository.findById(10L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer.get(), 7);
            Assert.assertTrue(shoppingCartService.getTotal() == 7 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addBeerToShoppingCartAndUpdateWithZeroQuantityTest() {
        Optional<Beer> beer = beerRepository.findById(10L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer.get(), 0);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addBeerToShoppingCartAndUpdateWithNegativeQuantityTest() {
        Optional<Beer> beer = beerRepository.findById(14L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer.get(), -5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersToShoppingCartAndUpdateAllOfTheQuantitiesTest() {
        Optional<Beer> beer = beerRepository.findById(10L);
        Optional<Beer> beer2 = beerRepository.findById(23L);

        if (beer.isPresent() && beer2.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.updateBeer(beer.get(), 9);
            shoppingCartService.updateBeer(beer2.get(), 10);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice() + 10 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersToShoppingCartAndUpdateSomeOfTheQuantitiesTest() {
        Optional<Beer> beer = beerRepository.findById(14L);
        Optional<Beer> beer2 = beerRepository.findById(25L);

        if (beer.isPresent() && beer2.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.updateBeer(beer.get(), 9);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersToShoppingCartAndUpdateSomeOfTheQuantitiesWithZeroTest() {
        Optional<Beer> beer = beerRepository.findById(19L);
        Optional<Beer> beer2 = beerRepository.findById(21L);

        if (beer.isPresent() && beer2.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.updateBeer(beer.get(), 9);
            shoppingCartService.updateBeer(beer2.get(), 0);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersToShoppingCartAndUpdateSomeOfTheQuantitiesWithNegativeTest() {
        Optional<Beer> beer = beerRepository.findById(19L);
        Optional<Beer> beer2 = beerRepository.findById(21L);
        Optional<Beer> beer3 = beerRepository.findById(29L);
        Optional<Beer> beer4 = beerRepository.findById(3L);

        if (beer.isPresent() && beer2.isPresent() && beer3.isPresent() && beer4.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.addBeer(beer3.get(), 12);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice() + 12 * beer3.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.addBeer(beer4.get(), 4);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice() + 12 * beer3.get().getPrice() + 4 * beer4.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.updateBeer(beer.get(), 9);
            shoppingCartService.updateBeer(beer2.get(), 5);
            shoppingCartService.updateBeer(beer3.get(), -14);
            shoppingCartService.updateBeer(beer4.get(), -1000);

            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice() + 5 * beer2.get().getPrice() + 12 * beer3.get().getPrice() + 4 * beer4.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 4);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer3.get()));
            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer4.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addBeerThenUpdateThenAddTest() {
        Optional<Beer> beer = beerRepository.findById(19L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer.get(), 9);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer.get(), 1);
            Assert.assertTrue(shoppingCartService.getTotal() == 10 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void updateMissingBeerFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);
        Optional<Beer> beer2 = beerRepository.findById(24L);

        if (beer.isPresent() && beer2.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer.get(), 9);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer2.get(), 10);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addThenRemoveBeerFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.removeBeer(beer.get());
            Assert.assertTrue(shoppingCartService.getTotal() == 0);
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 0);

            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersThenRemoveAllFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);
        Optional<Beer> beer2 = beerRepository.findById(24L);
        Optional<Beer> beer3 = beerRepository.findById(5L);

        if (beer.isPresent() && beer2.isPresent() && beer3.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.addBeer(beer3.get(), 7);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice() + 7 * beer3.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 3);

            shoppingCartService.removeBeer(beer.get());
            shoppingCartService.removeBeer(beer2.get());
            shoppingCartService.removeBeer(beer3.get());

            Assert.assertTrue(shoppingCartService.getTotal() == 0);
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 0);

            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer3.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addMultipleBeersThenRemoveSomeFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);
        Optional<Beer> beer2 = beerRepository.findById(24L);
        Optional<Beer> beer3 = beerRepository.findById(5L);

        if (beer.isPresent() && beer2.isPresent() && beer3.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.addBeer(beer2.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 2);

            shoppingCartService.addBeer(beer3.get(), 7);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice() + 5 * beer2.get().getPrice() + 7 * beer3.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 3);

            shoppingCartService.removeBeer(beer2.get());
            shoppingCartService.removeBeer(beer3.get());

            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer3.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addThenUpdateThenRemoveBeerFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.updateBeer(beer.get(), 9);
            Assert.assertTrue(shoppingCartService.getTotal() == 9 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.removeBeer(beer.get());
            Assert.assertTrue(shoppingCartService.getTotal() == 0);
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 0);

            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void addThenRemoveThenAddBackBeerFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);

        if (beer.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.removeBeer(beer.get());

            shoppingCartService.addBeer(beer.get(), 5);
            Assert.assertTrue(shoppingCartService.getTotal() == 5 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void removeMissingBeerFromShoppingCartTest() {
        Optional<Beer> beer = beerRepository.findById(19L);
        Optional<Beer> beer2 = beerRepository.findById(24L);

        if (beer.isPresent() && beer2.isPresent()) {
            shoppingCartService.addBeer(beer.get(), 3);
            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            shoppingCartService.removeBeer(beer2.get());

            Assert.assertTrue(shoppingCartService.getTotal() == 3 * beer.get().getPrice());
            Assert.assertTrue(shoppingCartService.getBeersInCart().size() == 1);

            Assert.assertTrue(shoppingCartService.getBeersInCart().containsKey(beer.get()));
            Assert.assertFalse(shoppingCartService.getBeersInCart().containsKey(beer2.get()));
        }
    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void manuallyAddToShoppingCartMapTest() {
        Map<Beer, Integer> beers = shoppingCartService.getBeersInCart();
        Optional<Beer> beer = beerRepository.findById(5L);

        if (beer.isPresent()) beers.put(beer.get(), 1);

        Assert.assertTrue(beers.size() == 0);
        Assert.assertTrue(beers.isEmpty());

    }

    @Test
    @DatabaseSetup("/beer_test.xml")
    public void manuallyUpdateBeerQuantityShoppingCartMapTest() {
        Optional<Beer> beer = beerRepository.findById(6L);

        if (beer.isPresent()) {

        }


    }


}
