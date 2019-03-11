package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.exception.NoBeersInCartException;
import web.globalbeershop.exception.NotEnoughBeersInStockException;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.service.ShoppingCartService;

@Controller
public class CartController {

    private final ShoppingCartService shoppingCartService;

    private final BeerService beerService;

    @Autowired
    public CartController(ShoppingCartService shoppingCartService, BeerService beerService) {
        this.shoppingCartService = shoppingCartService;
        this.beerService = beerService;
    }

    @GetMapping("/cart")
    public String cart(Model model) {

        model.addAttribute("beers", shoppingCartService.getBeersInCart());
        model.addAttribute("total", shoppingCartService.getTotal().toString());
        return "cart";

//        ModelAndView modelAndView = new ModelAndView("/cart");
//        modelAndView.addObject("beers", shoppingCartService.getBeersInCart());
//        modelAndView.addObject("total", shoppingCartService.getTotal().toString());
//        return modelAndView;
    }

    @PostMapping("/cart/add")
    public String addBeerToCart(@RequestParam(value = "id") Long id, @RequestParam(value = "quantity") Integer quantity ,Model model) {
        if(beerService.findById(id).isPresent()) {
            shoppingCartService.addBeer(beerService.findById(id).get(), quantity);
        }
        return "redirect:/shop";
    }

    @PostMapping("/cart/remove")
    public String removeBeerFromCart(@RequestParam("id") Long id, Model model) {
        beerService.findById(id).ifPresent(shoppingCartService::removeBeer);
        return cart(model);
    }

    @PostMapping("/cart/update")
    public String updateBeerInCart(@RequestParam("id") Long id, @RequestParam(value = "quantity") Integer quantity, Model model) {
        if(beerService.findById(id).isPresent()) {
            shoppingCartService.updateBeer(beerService.findById(id).get(), quantity);
        }
        return cart(model);
    }

    @GetMapping("/cart/checkout")
    public String goToCheckout(Model model) {
        try {
            shoppingCartService.validateCart();
        } catch (NotEnoughBeersInStockException e) {

            model.addAttribute("outOfStockMessage", e.getMessage());
            return cart(model);

//            return cart().addObject("outOfStockMessage", e.getMessage());
        } catch (NoBeersInCartException b) {
            model.addAttribute("emptyCartMessage", b.getMessage());
            return cart(model);
        }
        return "redirect:/checkout";
    }
}
