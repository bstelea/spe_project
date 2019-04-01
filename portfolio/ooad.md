# Object Oriented Architecture Design

Irregardless of how we design the system, we know that it will use a database to store all the data it uses - as is the case for most e-commerce websites. We are not required in this project to create the final database that our clients will deploy with, it will be an external system created and provided by our clients and we just need to setup our system to connect to it. However, during development, we will require our own database to test our system. The final product is requried to be able to connect to a MySQL database being hosted on the cloud; however for now we will use a tempoary, in-memory database while we still have not completed the MVP.

By reading our system requirements, you can determine that our database will be required to store data of the beers and their stock; user shopping carts and their contents; and customer orders and items. To achieve this, we have create the following schema, following good pracitce, to be used in our datbases:

![alt text](https://github.com/bstelea/spe_project/blob/bogdanRefact/portfolio/image/schema.png "Database Schema Diagram")

It's common practice for Web Applications, like our project, to be designed to follow the Model-View-Controller (MVC) architecture, where the components of the system play one of three roles:
1. **Controller**: receives requests (e.g. HTTP requests) and controls the View and Model accordingly to formulate the response (e.g. queries the database and get the Model to update with the results, or gets the View to update a webpage to show some data).
1. **Model**: Encapsulates the state of the application (e.g. the different data, objects and entities). Practically, it holds all the data from the system database so it can be used by the Controller and View.
1. **View**: Renders the Model of the application (e.g. generates pages of the website). The response made to the user comes from the View (e.g. returns the page they requested).

The Spring Boot and Spring MVC frameworks are very powerful tools that allows one to quickly and easily create Java-based Web Applications that you can "just run". Spring makes a lot of development more convinient, by automatically handling and setting-up more complex components in the background.

We will implement our MVC web application using Spring, which models the different parts of the MVC architecture as "beans" - objects created and managed by the Spring container. We will use, at least, the following types of beans:
1. **Controllers**: objects that receive and respond to HTTP requests from the user. They have "request mapping" methods that are automatically triggered to provide a response for a given user request. The Controllers manage the majority of backend logic in the server, and they create the model and views to be returned to the user.
1. **Entities**: objects that represent an entity modelled in our database, e.g. a Java object with correspoding attributes to some row in a table in the databass and their fields. The controllers never actually interact with the data in the database directly, but manipulates it using their respective entities.
1. **Repositories**: objects that can directly query our system's database, and generate Entity beans based on the results to be used by the Controllers.

The Entities fetched by the Controller can be rendered onto a HTML page dynamically using the Thymeleaf template engine.

As these Entities need to correspond to the tables in our database, we can determine from the schema defined earlier that our Spring application will require the following beans with the following relationships:

![alt text](https://github.com/bstelea/spe_project/blob/bogdanRefact/portfolio/image/entity_uml.png "Static UML Diagram")

Having modelled our database and entities in the previous diagrams will prove very benefitial to us in early development, as it will allow us to implement our data source and the object we use to iteract with it early on - a crucial prerequesit for most of our requirements. The database schema can be directly expressed in SQL dialect, while we only need to add Getter and Setter methods on top of the attributes listed for the entities when we come to implement them - making these diagrams essentially complete models of what they represent.

Unfortunately, these only represent static elements our our system. Based on our research into the Spring framework, however, has revealed that user interactions with the web application result in the following basic pattern:

![alt text](https://github.com/bstelea/spe_project/blob/bogdanRefact/portfolio/image/sequence_diagram.png "Dynamic UML Diagram")

This diagram is incredibly useful as it is a template for a lot of the code we will write. Anytime a page a user requests requires certain data, it will require functions that roughly follow the diagrams sequence of events. It is also applicable for HTTP POST requests, where the Controller would instead receive data in the request which it can process and store into the Databse using Entities and Repositories.

Unfortunately, neither the static or dynamic diagrams actually model the actual logic required to achieve such interactions and functinality. Fortunately, as we are doing Test-Driven Development, we can set out more specific guidelines or our code by writing unit tests for it before we implement.


