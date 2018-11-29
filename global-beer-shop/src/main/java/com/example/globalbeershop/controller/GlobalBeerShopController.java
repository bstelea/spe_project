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
import java.util.ArrayList;
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
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "country", required = false) String country,
                       @RequestParam(value = "brewer", required = false) String brewer,
                       @RequestParam(value = "abv", required = false) String abv,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "sortCol", required= false) String sortCol,
                       @RequestParam(value = "sortOrd", required= false) String sortOrd)
    {

        List<BeerStocked> queryResults;

        List<String> cols = new ArrayList<>();
        List<Object> vals = new ArrayList<>();
        List<String> sort = new ArrayList<>();

        if(sortCol != null && sortOrd != null){
            sort.add(0, sortCol);
            sort.add(1, sortOrd);
        }

        if(name!=null){
            cols.add("name");
            vals.add(name);
        }

        if(country!=null){
            cols.add("country");
            vals.add(country);
        }

        if(brewer!=null){
            cols.add("brewer");
            vals.add(brewer);
        }

        if(abv!=null){
            cols.add("abv");
            vals.add(abv);
        }

        if(type!=null){
            cols.add("type");
            vals.add(type);
        }

        //If no search restraints were given, just search for all
        if(cols.isEmpty()) queryResults = BeerStockedrepo.findAll(sort);

        //else, search by the cols and vals (and potentially an order restraint for the results) given
        else queryResults = BeerStockedrepo.findByColumn(cols, vals, sort);


        //HTML Response
        String resultsString = "";

        //if query was invalid
        if(queryResults == null) return "INVALID QUERY ARGUMENTS!";

        //if there are no results
        else if(queryResults.isEmpty()) return "NO RESULTS for Cols= "+cols.toString()+", Vals= "+vals.toString();


            //else (there are multiple results)
        else {

            //builds up html body
            for (BeerStocked beer : queryResults) {
                resultsString += ("<br>   -" + beer.toString());
            }

            //if no search requirements were specified
            if (cols.isEmpty() || vals.isEmpty()) return "All Beers in Stock, Ordered by " + sort.toString()+ " = " + resultsString;

                //else (requirements were given)
            else return "Query Results , ordered by " + sort.toString() +", for Cols= " + cols.toString() + " and Vals= " + vals.toString() + resultsString;

        }
    }
}
