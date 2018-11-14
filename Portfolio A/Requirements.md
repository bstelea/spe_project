## Stakeholders, Actors and Goals

Based on our project’s application domain, business sector and key problem; we can determine that the key stakeholders of our final product(s) will eventually be:

* __Global Beer Shop:__ Will own, run and profit from the website we create for them.
* __Legislators:__ Are able to pass laws which may affect the way the company/website is run. They will also continuously regulate the website.
* __Users:__ Directly interact with the website we create, either by browsing or buying beers.
* __Delivery Services:__ Will be making beer deliveries for the website after a purchase.
* __Beer Distributors:__ Will be obtaining another medium in which to sell their product.
* __Server Company:__ Will be running another website on their servers.
* __Database Company:__ Will host another database.
* __Competitors:__ Will have a new competitor to their business which could affect sales and/or website traffic.
* __Banks:__ Will gain a new company account.

However, in the scope of our project, we will not interact with a majority of these parties (e.g. they are stakeholders for Global Beer Shop as a business using our product, but our project is solely to develop the product for Global Beer Shop). As developers, we have to consider the System Actors - these are the stakeholders that will interact directly with our product- we have determined our actors to be:

* __Customer:__  Visitors to the sight. They will be searching for and buying beer through the site.
* __Website:__ The frontend and User Interface of the system through which the Customer can interact with the system.
* __Server:__ The dynamic backend software that responds to Customer interactions via the Website, provides a majority of the system functionality and manages the database.
* __Global Beer Shop:__ our clients and their company.

All of these actors interact together to accomplish “goals” (e.g. Customer orders a beer on the Website) and they perform different tasks to do this. 

By considering the functionality our clients require from the final system, we can outline all the goals of our actors and their interactions in the following use-case diagram:


[USE CASE DIAGRAM]

<ol><li></li></ol>
The most key goals, and the steps involved in achieving them are as follows:

| GOAL | BASIC FLOW | ALTERNATIVE FLOW | EXCEPTIONAL FLOW |
| --- | --- | --- | --- |
| Order Beer | _(user adds beer to basket)_ <ol><li>Consider beer catalogue.</li><li>Filter beers/search for beer.</li><li>Try to add quantity of beer to basket.</li><li>Server checks stock is available.</li><li>Add quantity of beer to basket.</li><li>Redirect user to beer catalogue.</li></ol> | _(user removes beer from basket)_ <ol><li>User inspects basket.</li> <li>User removes beer from basket (assuming beer is in basket).</li> <li>Beer is cleared from basket.</li></ol><ol> _(user increases quantity of beer in basket)._ <li>User inspects basket. <li>User tries to change quantity of beer in basket.</li> <li>Server checks if requested amount of beer is available.</li> <li>Server updates quantity of that beer in the User’s basket.</li></ol> | 
(not enough available stock of that beer)
Consider beer catalogue.
Filter beer/ search for beer.
Try to add beer to basket.
Server checks stock is available.
Error message.
Redirect customer back to beer catalogue.			

Pay for Beer
(payment goes through without issues)
Customer goes to checkout.
Server re-checks if there is still enough available stock.
Server reduces the available stock.
Customer puts in details.
Customer views delivery options
Customer chooses delivery option.
Customer views payment options.
Customer chooses payment option. 
Customer purchases order.
Server updates total stock to match available stock.
Server redirects Customer to Order Completion page


(customer cancels checkout)
Customer goes to checkout.
Server re-checks if there is still enough available stock.
Server reduces the available stock.
Customer puts in details.
Customer views delivery options
Customer chooses delivery option.
Customer views payment options.
Customer chooses payment option. 
Customer cancels checkout.
Server increases the available stock to previous amount.
Customer is redirected to basket.


(customer payment is unsuccessful or details entered are invalid)
Customer goes to checkout.
Server re-checks if there is still enough available stock.
Server reduces the available stock.
Customer puts in details.
Customer views delivery options
Customer chooses delivery option.
Customer views payment options.
Customer chooses payment option. 
Checkout fails due to unsuccessful payment or invalid details.
Customer is shown error message.
Server increases the available stock to previous amount.
Server redirects Customer to Basket.

Log Order
(order is logged into database)
Order is placed and paid for.
Order is logged into database.
Email is sent to customer and GBS.




Exceptional Flow (server crashes)
Order is placed and paid for.
Attempt to log order to database, but it is down.
Server is returned to previous backup.
Second attempt to log order to database.
Email is sent to customer and GBS.

Deliver Beer
(GBS sends beer to customer)
GBS view order email.
GBS arrange for beer delivery.
Beer is successfully delivered by courier.

-
(Order is canceled after ‘Log Order’ )
GBS view order email.
GBS arrange for beer delivery.
Client cancels order.
Revert database.
Send email to client and GBS to confirm cancellation of order.

## MVP System Requirements
As stated earlier, our project will follow an incremental, iterative approach; where we hope to deliver the applications over several releases of increasing functionality and quality. Our first release planned is that of our Minimum Viable Product (MVP), which the later Beta and Final releases will be built upon.

Staying true to our agile approach, we will now consider the MVP our current “sub-project” and will continue the planning and designing just for the MVP. The MVP will be the absolute minimal solution to the problem at hand, and must only include features deemed essential to providing e-commerce functionality.

We will elicit the MVP’s requirements by considering an abstract e-commerce website and what we is the simplest set of features we’d expect it to have to allow us to purchase a product on it. 
Using this, we can reduce the use-case goals we established earlier for final release into the optimal set required for just the MVP release:

[MVP STRUCTURE DIAGRAM]

The diagram above represents the structure of the MVP and highlights the most key functionality (both in the front end and back end) that it should provide. Alongside our use-case flows and requirements given by our clients, we can use this MVP structure to finally elicit our MVP’s system requirements:

GENERAL USER INTERFACE
FUNCTIONAL
The website should have a Wild West inspired design
All pages must have a Header and a Footer.
The Header and Footer should include the Global Beer Shop logo and slogan(s).
The Header should have links to:
Home Page
Product List
Shopping Cart
The Footer should have links to:
Global Beer Shop social media accounts
Contact Us page
FAQs
Terms and Conditions
Privacy.

NON-FUNCTIONAL
The style and theme of the website design must be consistent throughout all pages.

USER HTTP SESSIONS
FUNCTIONAL
When a new user accesses the website, the server must:
Assign that user an unique session ID
Log that session
When a session expires, the server must:
Unlog that session
Remove any temporary data stored during that session.

NON-FUNCTIONAL
Sessions must be given a maximum time limit of 1 hour, in case that a user’s browser does not automatically close their sessions after the page is closed.

SEARCH TOOL
FUNCTIONAL
Users should be able to use the Search Tool to find products based on:
Country of Origin
Brewer
ABV
Beer Type
Using the Search Tool must redirect the user to the Product List page, where the products displayed are either:
The products that meet the query requirements inputted by the user.
All the products in stock, if no query requirements were given.

NON-FUNCTIONAL
The Search Tool must be re-usable on various pages, including as a “filter” tool (functionally identical).
The input options that the Search Tool offers must be only include those for available products (e.g. you can’t search for a product by an attribute that no product currently has).
It should not take any longer than 15 seconds for the User to be displayed search results after using the Search Tool.

HOME PAGE
FUNCTIONAL
The Home Page must:
Publicize the services offered by the Global Beer Shop
Encourage users to view products on sale
Provide a Search Tool that allows users to find certain product(s)
Include the company slogan
The Home Page should:
Include an interactive map of the world that allows users to view all products in stock that originate from a specific country.
Recommend certain products

NON-FUNCTIONAL
The Home Page should:
Avoid using excessive amounts of text
Emphasise the use of images.

PRODUCT LIST 
FUNCTIONAL
Products must be displayed with at least their name, price and image.
Users can change the order that products are displayed in
Products must be displayed in the order specified by the user, if one has be specified.
Users can choose to filter/re-search for products to be displayed.
The only products displayed must be those that match the filter/search requirements of the user, if such requirements have been given.
If a User clicks on the image or name of a product on display, they must be redirected to that product’s corresponding Product page.
As not all products can be on display at once, the User must be able to navigate through the various display sets of the products.
Returning to a given display set of products must display the same products as before, if the filter/sort requirements are unchanged.

NON-FUNCTIONAL
By default, the products should be sorted in ascending alphabetical order.
Products should be clearly labelled if they are in or out of stock.
Products and their details on display must be segregated from others so products can be easily distinguished from one another.
Only a maximum of 12 products can be on display at once.

INDIVIDUAL PRODUCT 
FUNCTIONAL
For a given product, the page must display:
The name
The price
At least one image
The type of the product
The product’s ABV
Country of Origin
A short description of the product
The product can be added to the user’s Shopping Cart, in varying quantity.
A product can only be added to a Shopping Cart if, at the time of the attempt, the quantity being added is less than or equal to the available stock of that product.

NON-FUNCTIONAL
If a product cannot be added to the Shopping Cart due to insufficient stock, the user must be alerted of this.
The default quantity that products are added to the Shopping Cart is 1.

SHOPPING CART PAGE
FUNCTIONAL
For each current user session, there be a Shopping Cart.
The Shopping cart must display:
All items that have been added to it and not removed
These items’ quantities
The subtotal for these items
The overall basket total.
Unless modified by the user, the contents of the shopping cart must stay consistent until either:
The order checkout is complete
The user’s HTTP session expires.
While in the Shopping Cart, Users must be able to:
Remove items from their basket
Edit the quantities of items in the Cart
Checkout their order as a guest (e.g. doesn’t need to create an account to purchase).
For the user to be able to checkout their order:
There must be at least one item in the cart.
The quantities of each item in the cart must be less than or equal to the available stock for that item at the time of checkout.

NON-FUNCTIONAL
Each user session can only have one Shopping Cart.
If the Shopping Cart is empty:
It should not display any items
It should encourage the user to browse the beers on offer.
The maximum net amount of stock (regardless of which product) that can be added to the Shopping Cart is 100 units.
The total of the Shopping Cart must be updated every time that:
A user navigates to the Shopping Cart page.
An item is added by the corresponding user to the Cart.
If a user attempts to checkout items with insufficient Available Stock, the user must be told to either remove that item, reduce its quantity or try purchasing later when the items may be available again.

CHECKOUT
FUNCTIONAL
The Checkout page must display to the user:
A short summary of the products that the user is attempting to purchase
The final total for the order.
Delivery options, which the user can choose between.
Payment options, which the user can choose between
There must at least be a payment option for  “Cash On Delivery”
There must be an option that allows the user to cancel the checkout process.
When a customer enters the checkout stage, the available stock of each of the products in their Shopping Cart must be reduced by the corresponding quantity in the Shopping Cart.
If a customer exits the checkout stage before fully completing it, the available stock of each of the products in their Shopping Cart must be increased by the corresponding quantity in the Shopping Cart.
The user must enter their personal, delivery and payment details to complete the checkout. 
Checkout can only be completed is all data given by the user is valid.
Checkout can only be completed when the selected payment option has been fully processed.
If a user’s session expires in the middle of checking out, they must be redirected to the Home Page.
If a user chooses to cancel their checkout, they must be redirected to the Shopping Cart.

NON-FUNCTIONAL
All user input must be validated to be of correct form for that personal/payment/delivery detail being collected.
Users must be alerted of any input that is currently invalid.
All user input must be processed for SQL injection and Cross-Site Scripting.
The Checkout page(s) must use HTTPS (TLS encryption) to protect sensitive user information.

ORDER COMPLETION
FUNCTIONAL
Each order completed must be assigned some sort of unique reference number.
When an order is complete, the server must:
The Actual Stock of the products just ordered must be reduced by the corresponding quantities to reflect the purchase made.
Email the customer to alert them that their order has been a success
Email the Global Beer Shop to alter them that a customer has placed an order
Log the order
Clear the contents of that user’s Shopping Cart.

NON-FUNCTIONAL
The emails must be sent and received within 12 hours of the order completion.
