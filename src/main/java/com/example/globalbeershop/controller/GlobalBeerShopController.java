package com.example.globalbeershop.controller;

import com.example.globalbeershop.BeerStocked.BeerStocked;
import com.example.globalbeershop.BeerStocked.BeerStockedRepositry;
import com.example.globalbeershop.Order.Order;
import com.example.globalbeershop.Order.OrderItem;
import com.example.globalbeershop.Order.OrderRepository;
import com.example.globalbeershop.ShoppingCart.CartItem;
import com.example.globalbeershop.ShoppingCart.ShoppingCart;
import com.example.globalbeershop.ShoppingCart.ShoppingCartRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class GlobalBeerShopController {

    private static final String appName = "Global Beer Shop";

    @Autowired
    BeerStockedRepositry BeerStockedRepo;
    @Autowired
    ShoppingCartRepositry ShoppingCartRepo;
    @Autowired
    OrderRepository OrderRepo;

    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("title", appName);
        //update filtering values with distinct values from database
        String[] filterCols = new String[]{"country", "brewer", "abv", "type"};
        for(String col : filterCols) model.addAttribute(col, BeerStockedRepo.getAllDisinctCol(col));
        if(session.isNew()){
            System.out.printf("new session\n");
            model.addAttribute("sessionID", session.getId());
            model.addAttribute("cart", new ShoppingCart(session.getId()));
            model.addAttribute("prevReq", request);
        }
        return "index";

    }


    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "country", required = false) String country,
                       @RequestParam(value = "brewer", required = false) String brewer,
                       @RequestParam(value = "abv", required = false) String abv,
                       @RequestParam(value = "type", required = false) String type,
                       @RequestParam(value = "sortCol", required= false) String sortCol,
                       @RequestParam(value = "sortOrd", required= false) String sortOrd,
                       HttpServletRequest request    )
    {

        //update filtering values with distinct values from database
        String[] filterCols = new String[]{"country", "brewer", "abv", "type"};
        for(String col : filterCols) model.addAttribute(col, BeerStockedRepo.getAllDisinctCol(col));


        //update values chosen to query by


        List<String> cols = new ArrayList<>();
        List<Object> vals = new ArrayList<>();
        List<String> sort = new ArrayList<>();

        //if sorting reqs are given
        if(sortCol != null && sortOrd != null && sortCol != "" && sortOrd != ""){
            sort.add(0, sortCol);
            sort.add(1, sortOrd);
            model.addAttribute("sortColParam", sortCol);
            model.addAttribute("sortOrdParam", sortOrd);
        }
        else{
            //default sorting
            sort.add(0, "name");
            sort.add(1, "ASC");
            model.addAttribute("sortColParam", "name");
            model.addAttribute("sortOrdParam", "ASC");
        }

        if(name!=null && name!=""){
            cols.add("name");
            vals.add(name);
            model.addAttribute("nameParam", name);
        }
        else{
            model.addAttribute("nameParam", "");
        }

        if(country!=null && country!="") {
            cols.add("country");
            vals.add(country);
            model.addAttribute("countryParam", name);
        }
        else{
            model.addAttribute("countryParam", "");
        }


        if(brewer!=null && brewer!=""){
            cols.add("brewer");
            vals.add(brewer);
            model.addAttribute("brewerParam", name);
        }
        else{
            model.addAttribute("brewerParam", "");
        }


        if(abv!=null && abv!=""){
            cols.add("abv");
            vals.add(abv);
            model.addAttribute("abvParam", name);
        }
        else{
            model.addAttribute("abvParam", "");
        }


        if(type!=null && type!=""){
            cols.add("type");
            vals.add(type);
            model.addAttribute("typeParam", name);
        }
        else{
            model.addAttribute("typeParam", "");
        }


        List<BeerStocked> queryResults;

        //If no search restraints were given, just search for all
        if(cols.isEmpty()) queryResults = BeerStockedRepo.findAll(sort);

            //else, search by the cols and vals (and potentially an order restraint for the results) given
        else queryResults = BeerStockedRepo.findByColumn(cols, vals, sort);

        System.out.printf("updating shop page");
        model.addAttribute("beerList", queryResults);

        return "shop";

    }

    @GetMapping("/item/{id}")
    public String viewItem (Model model, HttpServletResponse response, @PathVariable("id") String beerId) throws IOException {

        BeerStocked item = BeerStockedRepo.findById(beerId);
        if(item==null){
            response.sendRedirect("/shop");
            return null;
        }

        model.addAttribute("displayItem", item);
        return "item";
    }

    @PostMapping("/cart")
    public void editCart (Model model, HttpSession session, HttpServletResponse response,
                          @RequestParam(value = "add", required = false) String beerId,
                          @RequestParam(value = "quantity", required = false) String quantity,
                          @RequestParam(value = "delete", required = false) String deleteId,
                          HttpServletRequest request) throws IOException {


        if(!session.isNew()) {
            String sessionId = session.getId();
            if (beerId != null && quantity != null) ShoppingCartRepo.addItemToCart(sessionId, beerId, quantity);

            else if (deleteId != null) ShoppingCartRepo.removeItemFromCart(sessionId, deleteId);

        }

        response.sendRedirect("/cart");
    }


    @GetMapping("/cart")
    public String openCart (Model model, HttpSession session) {

        if(session.isNew()) return "index";

        ShoppingCart cart = ShoppingCartRepo.findSessionShoppingCart(session.getId());
        model.addAttribute("cart", cart);
        return "cart";
    }


    @GetMapping("/checkout")
    public String openCheckout (Model model, HttpSession session, HttpServletResponse response) throws IOException {

        if(session.isNew()) return "index";

        ShoppingCart cart = ShoppingCartRepo.findSessionShoppingCart(session.getId());
        if(cart.getItems().isEmpty()){
            response.sendRedirect("/index");
            return null;
        }

        int noAvailableStockCount = 0;
        for(CartItem item : cart.getItems()){
            if(!ShoppingCartRepo.hasAvailableStock(Long.toString(item.getItem().getId()), Integer.toString(item.getQuantity()))){
                noAvailableStockCount++;
            }
        }

        //if not enough available stock
        if (noAvailableStockCount!=0) return("/index");

        //if enough stock, reduce available stock
        for(CartItem item : cart.getItems()){
            BeerStockedRepo.reduceAvailableStock(Long.toString(item.getItem().getId()), Integer.toString(item.getQuantity()));
        }

        model.addAttribute("cart", cart);
        return "checkout";
    }

    @GetMapping("/orderComplete")
    public String processCheckout (Model model, HttpSession session, HttpServletResponse response,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "address") String address,
                                 @RequestParam(value = "city") String city,
                                 @RequestParam(value = "county") String county,
                                 @RequestParam(value = "pcode") String postcode
    ) throws IOException {


        if(session.isNew()) return "index";

        /*


        //if valid, process payment

        //reduce actual stock
        //log order
        ShoppingCart cart = ShoppingCartRepo.findSessionShoppingCart(session.getId());
        if(cart.getItems().isEmpty()) return "index";

        Order order = OrderRepo.addOrder(name, email, address, city, county, postcode, session.getId());

        for(CartItem item : cart.getItems()){
            BeerStockedRepo.reduceActualStock(Long.toString(item.getItem().getId()), Integer.toString(item.getQuantity()));
            OrderItem oItem = new OrderItem(order.getOrderID(), item.getItem().getId(), item.getItem().getName(), item.getItem().getPrice(), item.getQuantity());
            order.addItem(oItem);
            OrderRepo.addItemToOrder(order.getOrderID(), oItem);
        }

        //email parties
        */

        return "orderComplete";
    }


}

