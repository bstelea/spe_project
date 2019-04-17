# Object Oriented Architecture Design

Irregardless of how we design the system, we know that it will use a database to store all the data it uses- as is common practice for most e-commerce websites We are not required to actually create a database to be deployed for our clients, they will provide such a database and we just have to connect to it. However, during development, we will require a database to test our system. The final product will be configured to connect to a MySQL database being hosted locally/on the cloud, however we will use a tempoary, in-memory database while we still have not completed the MVP.

By reading our system requirements, you can determine that our database will be required to keep track of the beers and their stock; user shopping carts and their contents; and customer orders and items. To achieve this, we have create the following schema to be used in our datbases:

SCHEMA DIAGRAM

Web Applications, like our project, are most commonly designed to follow the Model-View-Controller (MVC) architecture, where the components of the system play one of three roles:
1. **Controller**: receives requests (e.g. HTTP requests) and controls the View and Model accordingly to formulate the response (e.g. queries the database and get the Model to update with the results, or gets the View to update a webpage to show some data).
1. **Model**: Encapsulates the state of the application (e.g. the different data, objects and entities). Practically, it holds all the data from the system database so it can be used by the Controller and View.
1. **View**: Renders the Model of the application (e.g. generates pages of the website). The response made to the user comes from the View (e.g. returns the page they requested).

The Spring Boot and Spring MVC frameworks are very powerful tool that allows one to quickly and easily create Java-based Web Applications that you can "just run". Spring makes a lot of development more convinient, by automatically handling and setting-up more complex components in the background.

We will implement our MVC web application using Spring, and it will implement the different parts of the MVC architecture as "beans" - objects created and managed by the Spring container. We will use, at least, the following types of beans:
1. **Controllers**: objects that have "request mapping" methods that are automatically triggered to provide a response for a given user request. The Controllers will create the model and views to be returned to the user.
1. **Entities**: objects that represent an entity modelled in our database, e.g. a Java object with correspoding attributes to some row in a table in the databass and their fields.
1. **Repositories**: objects that can directly query our system's database, and generate Entity beans based on the results to be used by the Controllers.

As these Entities need to correspond to the tables in our database, we can determine from the schema defined earlier that our Spring application will require the following beans:

STATIC DIAGRAM



