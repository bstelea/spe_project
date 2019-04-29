# Requirements
### Stakeholders, Actors and Goals

Based on our project’s application domain, business sector and key problem; we can determine the key stakeholders and the goals that they have:

* __Global Beer Shop (our Clients):__ Will own, run and profit from the system we create for them.
* __Customers:__ Will use our system to browse and purchase the beers that GBS are selling.
* __Delivery Services:__ Will be contracted by Global Beer Shop to deliver orders placed on the system, charging GBS a fee.
* __Beer Distributors:__ Will profit from their product being sold on the system.
* __Server Company:__ Will be contracted to host our system and database, charging GBS a fee.
* __Payment Service:__  Will process transactions for our system, charging GBS a fee.
* __Competitors:__ Will have a new competitor to their business which could affect their sales and/or website traffic.

However, in the scope of our project, we will not interact with a majority of these stakeholders (e.g. they are stakeholders for Global Beer Shop as a business using our product, but our project is solely to develop the product for Global Beer Shop). As the developers of the system, we will only consider the following stakeholders and their goals as our System Actors: Global Beer Shop, Users (Customers), the Payment Service and the Database.

(We can consider the database being hosted by the Server Company as its own actor as the system interacts with it)

The following use-case diagram highlights how these actors interact with each other to achieve their goals:

![Use Case Goals](https://github.com/bstelea/spe_project/blob/bogdanRefact/portfolio/image/use_case.png "Use Case Goals")

The core goal in our system is for Global Beer Shop to sell beers (equivalently, users buying beers). These are some of the different flows broken down into steps that could occur on our system when a user attempts to purchase a beer:

BASIC FLOW (_user purchases a single beer_):
1) User searches for a beer on System
1) User adds beer to shopping cart.
1) System updates cart items in database.
1) User goes to checkout on System.
1) System checks stock in Database.
1) There is enough stock for all items in the Database.
1) User enters delivery details.
1) User enters payment details.
1) System contacts Payment Service
1) Payment Service successfully processes payment.
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
1) System checks stock in Database.
1) There is enough stock for all items in the Database.
1) User enters delivery details.
1) User enters payment details.
1) System contacts Payment Service
1) Payment Service successfully processes payment.
1) GBS receives payment from Payment Service.
1) System logs order in Database.
1) System emails GBS and User about order.

EXCEPTIONAL FLOW (_payment transaction fails at checkout_):
1) User goes to checkout on System.
1) System checks stock in Database.
1) There is enough stock for all items in the Database.
1) User enters delivery details.
1) User enters payment details.
1) System contacts Payment Service
1) Payment Service fails to processes payment.
1) GBS receives error message from Payment Service.
1) System alerts User of failed payment.

EXCEPTIONAL FLOW (_not enough available stock of that beer at checkout_):
1) User goes to checkout on System.
1) System checks stock in Database.
1) There is not enough stock for one item in the Database.
1) System redirects User back to the shopping cart.
1) System alerts User of lack of stock.

### System Requirements

Based on information specified by our clients; the interactions of our system actors and their goals; and existing examples of e-commerce websites, we can determine the following functional and non-functional requirements for all aspects of our product:

1) GENERAL
    1) The website should have a Wild West inspired design
    1) The website must, at least, consist of:
        1) a Home Page
        1) a Shop page
        1) a Product page
        1) a Shopping Cart page
        1) a Checkout page
        1) an Order Complete page
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
    1) Our clients must approve the final UI design.
    1) The website must be able serve at least 25 users at the same time.

1) HOME PAGE
    1) The Home Page must:
        1) Publicize the services offered by the Global Beer Shop
        1) Encourage users to view beers on sale
        1) Include the company slogan
        1) Provide some way for users to contact GBS
    1) The Home Page should:
        1) Include an interactive map of the world that allows users to view all products in stock that originate from a specific country.
        1) Recommend certain products
        1) Avoid using excessive amounts of text
        1) Emphasise the use of images.
        
1) SEARCHING FOR BEERS
    1) Users must be able to search for beers by:
        1) Name
        1) Country of Origin
        1) Brewer
        1) ABV
        1) Beer Type
    1) There must be a filtering and sorting tool.
    1) Using the search tool must return all beers such that:
        1) They all meet constraint(s) set by the user.
        1) They are all in stock.
    1) Using the sorting tool must return all beers such that:
    1) Users must be able to also sort the beers they search by the same columns that they can search by an set a sorting order (ascending or descending).    
    1) Default settings for filter/sort tools are no filtering and sorting by beer name in ascending order.
    1) It should not take any longer than 15 seconds for the User to be displayed search results after using the Search Tool.
    1) Beers must be displayed at least with their:
        1) Name
        1) Price
        1) Image

1) SHOPPING CART
    1) For each non-expired user session, there must be a Shopping Cart for that user.
    1) The Shopping Cart page must display:
        1) All items that have been added to it and not removed
        1) These items’ quantities
        1) The subtotal for these items
        1) The overall basket total.
    1) Unless modified by the user, the contents of the shopping cart must stay consistent until either:
        1) The order checkout is complete
        1) The user’s HTTP session expires.
    1) Users must be able to:
        1) Remove items from their Cart
        1) Edit the quantities of items in the Cart
        1) Checkout their order as a guest (e.g. doesn’t need to create an account to purchase).
    1) For the user to be able to checkout their order:
        1) There must be at least one item in the cart.
        1) The quantities of each item in the cart must be less than or equal to the available stock for that item at the time of checkout.

    1) Each user session can only have one Shopping Cart.
    1) If the Shopping Cart is empty:
        1) It should not display any items
        1) It should encourage the user to browse the beers on offer.
    1) The maximum net amount of stock (regardless of which product) that can be added to the Shopping Cart is the total number of units available in stock.
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
    1) If a user chooses to cancel their checkout, they must be redirected to the Shopping Cart.
    1) All user input must be validated to be of correct form for that personal/payment/delivery detail being collected.
    1) Users must be alerted of any input that is currently invalid.
    1) All user input must be processed for SQL injection and Cross-Site Scripting.
    1) The Checkout page(s) must use HTTPS (TLS encryption) to protect sensitive user information.
    1) Processing an ordering should not take longer than 30 seconds.


1) ORDER COMPLETION
    1) Each order completed must be assigned a unique reference number.
    1) When an order is complete, the server must:
        1) Reduce the Actual Stock of the products just ordered by the corresponding quantities to reflect the purchase made.
        1) Email the customer to alert them that their order has been a success
        1) Email the Global Beer Shop to alert them that a customer has placed an order
        1) Log the order
        1) Clear the contents of that user’s Shopping Cart.
    1) The emails must be sent and received within 12 hours of the order completion.
    1) The stock of items being purchased in a order must be decremented accordingly before the order is complete.    

