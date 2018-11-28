package com.example.globalbeershop.controller;

import com.example.globalbeershop.BeerStocked.BeerStocked;
import com.example.globalbeershop.BeerStocked.BeerStockedRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Null;
import java.util.List;

@Controller
public class GlobalBeerShopController {

    private static final String appName = "Global Beer Shop";

    @Autowired
    BeerStockedRepositry BeerStockedrepo;

    @GetMapping("/")
    public String index(Model model,
                       @RequestParam(value = "name", required = false,
                               defaultValue = "Guest") String name) {
        model.addAttribute("name", name);
        model.addAttribute("title", appName);
        return "index";

    }

    @GetMapping("/shop")
    @ResponseBody
    public String shop(Model model,
                        @RequestParam(value = "country", required = false) String country,
                       @RequestParam(value = "brewer", required = false) String brewer,
                       @RequestParam(value = "abv", required = false) String abv,
                       @RequestParam(value = "type", required = false) String type)
    {

        List<BeerStocked> queryResults;

        //If no search restraints were given
        if(country==null && brewer==null && abv==null && type==null){
            queryResults = BeerStockedrepo.findAll();
        }

        else if(country!=null && brewer==null && abv==null && type==null){
            System.out.println("\nSearching by country");
            queryResults = BeerStockedrepo.findByColumn("country", country);
        }

        else{
            queryResults = null;
        }

        String resultsString = "";
        for(BeerStocked beer : queryResults) {
            resultsString+=("<br>" + beer.toString());
        }

        return "beers n tings: "+resultsString;

    }
}
