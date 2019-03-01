package web.globalbeershop.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import web.globalbeershop.data.Beer;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.service.ShoppingCartService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final BeerRepository beerRepository;

    private Map<Beer, Integer> beers = new HashMap<>();

    @Autowired
    public ShoppingCartServiceImpl(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }


    /*If the beer is in the map just increment the quantity by 1.
     * Otherwise, add it to the map with quantity 1.*/
    @Override
    public void addBeer(Beer beer, Integer quantity) {
        if(beers.containsKey(beer)) {
            beers.replace(beer, beers.get(beer) + quantity);
        } else {
            beers.put(beer, quantity);
        }
    }

    @Override
    public void updateBeer(Beer beer, Integer quantity) {
        if (beers.containsKey(beer)) {
            beers.replace(beer, quantity);
        }
    }

    /*If beer is in the map with quantity higher than 1, decrement the quantity by 1.
     * Otherwise, if it has quantity = 1, remove the beer from the map.*/
    @Override
    public void removeBeer(Beer beer) {
        if(beers.containsKey(beer)) {
            beers.remove(beer);
        }
    }

    // Return an unmodifiable copy of the map
    @Override
    public Map<Beer, Integer> getBeersInCart() {
        return Collections.unmodifiableMap(beers);
    }

    @Override
    public void validateCart() throws NotEnoughBeersInStockException, NoBeersInCartException {
        Beer beer;
        if (beers.entrySet().isEmpty()) {
            throw new NoBeersInCartException();
        } else {
            for (Map.Entry<Beer, Integer> entry : beers.entrySet()) {
                // Refresh quantity for every product before checking
                beer = beerRepository.findById(entry.getKey().getId()).get();
                if (beerRepository.findById(entry.getKey().getId()).isPresent()) {
                    if (beer.getStock() < entry.getValue()) {
                        throw new NotEnoughBeersInStockException(beer);
                    }
                }
            }
        }
    }


    @Override
    public void finish(){
        for (Map.Entry<Beer, Integer> entry : beers.entrySet()) {
            Beer beer = beerRepository.findById(entry.getKey().getId()).get();
            if (beerRepository.findById(entry.getKey().getId()).isPresent()) {
                entry.getKey().setStock(beer.getStock() - entry.getValue());
            }
        }
        beerRepository.saveAll(beers.keySet());
        beerRepository.flush();
        beers.clear();
    }

    @Override
    public Double getTotal(){
        Double total = 0.0;
        for(Map.Entry<Beer, Integer> cartItem  : beers.entrySet()) total+= cartItem.getKey().getPrice() * cartItem.getValue();
        return total;
    }
}
