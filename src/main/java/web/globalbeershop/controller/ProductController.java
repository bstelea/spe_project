package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import web.globalbeershop.service.BeerService;

@Controller
public class ProductController {

    private final BeerService beerService;

    @Autowired
    public ProductController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/products/{id}")
    public String product(Model model, @PathVariable Long id) {
        model.addAttribute("beer", beerService.findById(id).get());
        return "product-page";
    }
}
