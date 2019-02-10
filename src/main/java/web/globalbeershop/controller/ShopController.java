package web.globalbeershop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.Beer;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.util.Pager;

import java.util.Optional;

@Controller
public class ShopController {

    private static final int INITIAL_PAGE = 0;

    private final BeerService beerService;

    @Autowired
    public ShopController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/shop")
    public ModelAndView shop(@RequestParam("page")Optional<Integer> page) {

        /*Evaluate page. If requested parameter is null or less than 0 (to
         prevent exception), return initial size. Otherwise, return value of
         param. decreased by 1.*/
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Beer> beers = beerService.findAllBeersPageable(new PageRequest(evalPage, 9));
        Pager pager = new Pager(beers);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("beers", beers);
        modelAndView.addObject("pager", pager);
        modelAndView.setViewName("/shop");
        return modelAndView;
    }
}
