package web.globalbeershop.controller;



import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import web.globalbeershop.data.Beer;
import web.globalbeershop.data.QBeer;
import web.globalbeershop.repository.BeerRepository;
import web.globalbeershop.service.BeerService;
import web.globalbeershop.util.Pager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {

    @Autowired
    BeerRepository BeerRepo; //easier to use Repo, no need for a beer service

    private static final int INITIAL_PAGE = 0;

    private final BeerService beerService;

    @Autowired
    public ShopController(BeerService beerService) {
        this.beerService = beerService;
    }


    //Serves get requests for shop page
    @GetMapping("/shop")
    public String getShop(Model model,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "country", required = false) String country,
                          @RequestParam(value = "brewer", required = false) String brewer,
                          @RequestParam(value = "abv", required = false) String abv,
                          @RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "sortCol", required= false) String sortCol,
                          @RequestParam(value = "sortOrd", required= false) String sortOrd,
                          @RequestParam(value = "page", required = false) Integer page,
                          @RequestParam(value = "size", required = false) Integer size,
                          @RequestParam(value = "continent", required = false) String continent){


        List<Object> paramsGiven = Arrays.asList(name, country, brewer, abv, type, sortCol, sortOrd, size);

        //calculates filtering/sorting for query params specified by user
        BooleanExpression predicate = getQueryPredicate(name, country, brewer, abv, type, continent);
        PageRequest paging = getOrderedPageable(page, size, sortCol, sortOrd);

        //updates drop-down menu options and pre-selected values in search tool on Shop page
        updateSearchToolLists(model);
        model.addAttribute("paramsGiven", paramsGiven);

        //performs query using predicate and ordering specified, and saves results in the user's model
        Page<Beer> beers = BeerRepo.findAll(predicate, paging);
        model.addAttribute("beers", beers);
        model.addAttribute("beers", beers);
        model.addAttribute("pager", new Pager(beers));
        return "shop";
    }




    //Using params given by user, generates Boolean Expression to filter query results
    private BooleanExpression getQueryPredicate(String name, String country, String brewer, String abv, String type, String continent){

        //Default filtering in all queries is for beers with stock > 0 (e.g. in stock)
        BooleanExpression predicate = QBeer.beer.stock.gt(0);


        //checks all possible params user could provide, and constructs query predicate using given params
        if(paramGiven(name)) predicate=predicate.and(QBeer.beer.name.like( "%"+name+"%"));
        if(paramGiven(continent)) predicate=predicate.and(QBeer.beer.continent.eq(continent));
        if(paramGiven(country)) predicate=predicate.and(QBeer.beer.country.eq(country));
        if(paramGiven(brewer)) predicate=predicate.and(QBeer.beer.brewer.eq(brewer));
        if(paramGiven(abv)) predicate=predicate.and(QBeer.beer.abv.eq(Double.parseDouble(abv)));
        if(paramGiven(type)) predicate=predicate.and(QBeer.beer.type.eq(type));

        return predicate;
    }

    //Creates pageable query object for the given page of given size, with given column sorting
    private PageRequest getOrderedPageable(Integer page, Integer size, String sortCol, String sortOrd){


        //if index or size null/invalid, set to default values
        Integer index;
        if(page==null) index = INITIAL_PAGE;
        else{
            if(page < 0) index = INITIAL_PAGE;
            else index = page - 1;
        }

        if(size==null){
            size = 9;
        }
        else{
            if(!Arrays.asList(9, 27, 45, 99).contains(size)) size = 9;
        }




        //default sorting (e.g. when user doesn't specify an order) is by name ascending
        Sort sort = new Sort(Sort.Direction.ASC, "name");

        //constructs order specifier based on given params (if valid)
        if(paramGiven(sortCol)){

            //if col and sort valid, update Sort object
            if(Arrays.asList("name", "country", "brewer", "abv", "type", "price").contains(sortCol.toLowerCase())){
                if(paramGiven(sortOrd)){
                    if(sortOrd.toUpperCase() == "ASC") sort = new Sort(Sort.Direction.ASC, sortCol);
                    else if (sortOrd.toUpperCase() == "DESC") sort = new Sort(Sort.Direction.DESC, sortCol);
                }
                else sort = new Sort(Sort.Direction.ASC, sortCol);

            }

        }

        return new PageRequest(index, size, sort);
    }


    //checks if a value for a given String parameter was given
    private boolean paramGiven(String p){
        return (p != null && p!= "");
    }

    //updates values in search tool options based on distinct col values in DB
    private void updateSearchToolLists(Model model){
        model.addAttribute("countries", BeerRepo.findAllDistinctCountry());
        model.addAttribute("brewers", BeerRepo.findAllDistinctBrewer());
        model.addAttribute("abvs", BeerRepo.findAllDistinctAbv());
        model.addAttribute("types", BeerRepo.findAllDistinctType());

    }
}
