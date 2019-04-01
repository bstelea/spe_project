# Requirements
### Stakeholders, Actors and Goals

Based on our project’s application domain, business sector and key problem; we can determine that the key stakeholders and the goals that they have:

* __Global Beer Shop (our Clients):__ Will own, run and profit from the system we create for them.
* __Customers:__ Will use our system to browse and purchase the beers that GBS are selling.
* __Delivery Services:__ Will be contracted by Global Beer Shop to deliver orders placed on the system, charging GBS a fee.
* __Beer Distributors:__ Will profit from their product being sold on the system.
* __Server Company:__ Will be contracted to host our system and database, charging GBS a fee.
* __Payment Service:__  Will process transactions for our system, charging GBS a fee.
* __Competitors:__ Will have a new competitor to their business which could affect their sales and/or website traffic.

However, in the scope of our project, we will not interact with a majority of these stakeholders (e.g. they are stakeholders for Global Beer Shop as a business using our product, but our project is solely to develop the product for Global Beer Shop). As the developers of the system, we only will consider the following stakeholders and their goals as our System Actors: Global Beer Shop, Users (Customers), the Payment Service and the Database.

(We can consider the database being hosted by the Server Company as its own actor as the system interacts with it)

The following use-case diagram highlights how these actors interact with each other to achieve their goals:

![Use Case Goals](https://github.com/bstelea/spe_project/blob/bogdanRefact/portfolio/image/use_case.png "Use Case Goals")

The most core goal in our system is for Global Beer Shop to sell beers (equivelantly, users buying beers). These are some of the different flow in steps that could occur on our system when a user attempts to purchase a beer:

BASIC FLOW (_user purchases a single beer_):
1) User searches for a beer on System
1) User adds beer to shopping cart.
1) System updates cart items in database.
1) User goes to checkout on System.
1) System checks stock in Databse.
1) There is enough of stock for all items in the Database.
1) User enters delivery details.
1) User enters payment details.
1) System contacts Payment Service
1) Payment Service succesfully processes payment.
1) GBS receives payment from Payment Service.
1) System logs order in Database.
1) System emails GBS and User about order.

ALTERNATIVE FLOW (_user adds and removes various beers from cart before purchasing_):
1) User searches for some beer on System
1) User adds beer to shopping cart.
1) System updates cart items in database.
1) User searches for another beer on System
1) User adds beer to shopping cart.
1) System updates cart items in database.
1) User searches for yet another beer on System
1) User adds beer to shopping cart.
1) System updates cart items in database.
1) User inspects shopping cart.
1) User removes one beer completely from shopping cart.
1) System updates cart items in database.
1) User edits another beer's quantity in the cart shopping cart.
1) System updates cart items in database.
1) User goes to checkout on System.
1) System checks stock in Databse.
1) There is enough of stock for all items in the Database.
1) User enters delivery details.
1) User enters payment details.
1) System contacts Payment Service
1) Payment Service succesfully processes payment.
1) GBS receives payment from Payment Service.
1) System logs order in Database.
1) System emails GBS and User about order.

EXCEPTIONAL FLOW (_payment transaction fails at checkout_):
1) User goes to checkout on System.
1) System checks stock in Databse.
1) There is enough of stock for all items in the Database.
1) User enters delivery details.
1) User enters payment details.
1) System contacts Payment Service
1) Payment Service fails to processes payment.
1) GBS receives error message from Payment Service.
1) System alerts User of failed payment.

EXCEPTIONAL FLOW (_not enough available stock of that beer at checkout_):
1) User goes to checkout on System.
1) System checks stock in Databse.
1) There is not enough of stock for one item in the Database.
1) System redirects User back to the shopping cart.
1) System alerts User of lack of stock.

### MVP System Requirements

The first release we will develop will be the Minimum Viable Product (MVP) which is the simplest version of our product that provides the bare necessity of functionality to achieve what the final system will - in our case, it will be a very basic e-commerce website without any fancy features.

Based on information specified by our clients; the interactions of our system actors and their goals; and existing examples of e-commerce websites, we can determine the following functional and non-functional requirements for all aspects of our MVP:

(We have chosen to only design the requirements for the MVP at this stage because it is the first version we implement and it will provide the basis that all future releases will be built upon)

1) GENERAL USER INTERFACE
    1) The website should have a Wild West inspired design
    1) The website must, at least, consist of:
        1) a Home Page
        1) a Shop page
        1) an Product page
        1) a Shopping Cart page
        1) a Checkout page
        1) an Order Completion page
    1) All pages must have the same Header and a Footer.
    1) The Header and Footer should include the Global Beer Shop logo and slogan(s).
    1) The Header should have links to:
        1) Home Page
        1) Product List
        1) Shopping Cart
    1) The Footer should have links to:
        1) Global Beer Shop social media accounts
        1) Contact Us page
        1) FAQs
        1) Terms and Conditions
        1) Privacy.
    1) The style and theme of the website design must be consistent throughout all pages.

1) SESSIONS
    1) When a new user accesses the website, the server must:
        1) Assign that user an unique session ID
        1) Log that session
    1) When a session expires, the server must:
        1) Unlog that session
        1) Remove any temporary data stored during that session.
    1) Sessions must be given a maximum time limit of 1 hour, in case that a user’s browser does not automatically close their  sessions after the page is closed.

1) SEARCHING AND FILTERING
    1) Users should be able to use the Search Tool to find products based on:
        1) Country of Origin
        1) Brewer
        1) ABV
        1) Beer Type
    1) Using the Search Tool must redirect the user to the Product List page, where the products displayed are either:
        1) The products that meet the query requirements inputted by the user.
        1) All the products in stock, if no query requirements were given.
    1) The Search Tool must be re-usable on various pages, including as a “filter” tool (functionally identical).
    1) The input options that the Search Tool offers must only include those for available products (e.g. you can’t search for a product by an attribute that no product currently has).
    1) It should not take any longer than 15 seconds for the User to be displayed search results after using the Search Tool.

1) HOME PAGE
    1) The Home Page must:
        1) Publicize the services offered by the Global Beer Shop
        1) Encourage users to view products on sale
        1) Provide a Search Tool that allows users to find certain product(s)
        1) Include the company slogan
    1) The Home Page should:
        1) Include an interactive map of the world that allows users to view all products in stock that originate from a specific country.
        1) Recommend certain products
    1) The Home Page should:
        1) Avoid using excessive amounts of text
        1) Emphasise the use of images.

1) SHOP
    1) Products must be displayed with at least their name, price and image.
    1) Users can change the order in which the products are displayed in
    1) Products must be displayed in the order specified by the user, if one has be specified.
    1) Users can choose to filter/re-search for products to be displayed.
    1) The only products displayed must be those that match the filter/search requirements of the user, if such requirements have been given.
    1) If a User clicks on the image or name of a product on display, they must be redirected to that product’s corresponding Product page.
    1) As not all products can be on display at once, the User must be able to navigate through the various display sets of the products.
    1) Returning to a given display set of products must display the same products as before, if the filter/sort requirements are unchanged.

    1) By default, the products should be sorted in ascending alphabetical order.
    1) Products should be clearly labelled if they are in or out of stock.
    1) Products and their details on display must be segregated from others so products can be easily distinguished from one another.
    1) Only a maximum of 12 products can be on display at once.

1) PRODUCT
    1) For a given product, the page must display:
        1) The name
        1) The price
        1) At least one image
        1) The type of the product
        1) The product’s ABV
        1) Country of Origin
        1) A short description of the product
    1) The product can be added to the user’s Shopping Cart, in varying quantity.
    1) A product can only be added to a Shopping Cart if, at the time of the attempt, the quantity being added is less than or equal to the available stock of that product.
    1) If a product cannot be added to the Shopping Cart due to insufficient stock, the user must be alerted of this.
    1) The default quantity that products are added to the Shopping Cart is 1.

1) SHOPPING CART
    1) For each current user session, there will be a Shopping Cart.
    1) The Shopping cart must display:
        1) All items that have been added to it and not removed
        1) These items’ quantities
        1) The subtotal for these items
        1) The overall basket total.
    1) Unless modified by the user, the contents of the shopping cart must stay consistent until either:
        1) The order checkout is complete
        1) The user’s HTTP session expires.
    1) While in the Shopping Cart, Users must be able to:
        1) Remove items from their basket
        1) Edit the quantities of items in the Cart
        1) Checkout their order as a guest (e.g. doesn’t need to create an account to purchase).
    1) For the user to be able to checkout their order:
        1) There must be at least one item in the cart.
        1) The quantities of each item in the cart must be less than or equal to the available stock for that item at the time of checkout.

    1) Each user session can only have one Shopping Cart.
    1) If the Shopping Cart is empty:
        1) It should not display any items
        1) It should encourage the user to browse the beers on offer.
    1) The maximum net amount of stock (regardless of which product) that can be added to the Shopping Cart is 100 units.
    1) The total of the Shopping Cart must be updated every time that:
        1) A user navigates to the Shopping Cart page.
        1) An item is added by the corresponding user to the Cart.
    1) The user cannot add to the shopping cart more items than the number of items available in stock.

1) CHECKOUT
    1) The Checkout page must display to the user:
        1) A short summary of the products that the user is attempting to purchase
        1) The final total for the order.
        1) Delivery options, which the user can choose between.
        1) Payment options, which the user can choose between
    1) There must at least be a payment option for  “Cash On Delivery”
    1) There must be an option that allows the user to cancel the checkout process.
    1) When a customer enters the checkout stage, the available stock of each of the products in their Shopping Cart must be reduced by the corresponding quantity in the Shopping Cart.
    1) If a customer exits the checkout stage before fully completing it, the available stock of each of the products in their Shopping Cart must be increased by the corresponding quantity in the Shopping Cart.
    1) The user must enter their personal, delivery and payment details to complete the checkout. 
    1) Checkout can only be completed if all data given by the user is valid.
    1) Checkout can only be completed when the selected payment option has been fully processed.
    1) If a user’s session expires in the middle of checking out, they must be redirected to the Home Page.
    1) If a user chooses to cancel their checkout, they must be redirected to the Shopping Cart.
    1) All user input must be validated to be of correct form for that personal/payment/delivery detail being collected.
    1) Users must be alerted of any input that is currently invalid.
    1) All user input must be processed for SQL injection and Cross-Site Scripting.
    1) The Checkout page(s) must use HTTPS (TLS encryption) to protect sensitive user information.
---
1) ORDER COMPLETION
    1) Each order completed must be assigned a unique reference number.
    1) When an order is complete, the server must:
        1) Reduce the Actual Stock of the products just ordered by the corresponding quantities to reflect the purchase made.
        1) Email the customer to alert them that their order has been a success
        1) Email the Global Beer Shop to alter them that a customer has placed an order
        1) Log the order
        1) Clear the contents of that user’s Shopping Cart.
    1) The emails must be sent and received within 12 hours of the order completion.


