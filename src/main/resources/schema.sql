create table beerStocked
(
   id integer not null AUTO_INCREMENT,
   name varchar(255) not null,
   country varchar(255) not null,
   brewer varchar(255) not null,
   type varchar(255) not null,
   abv decimal(3,1) not null,
   price decimal (10,2) null,
   image varchar(255),
   description varchar(1000),
   totalStock integer,
   availableStock integer,
   primary key(id)
);

create table shoppingCartItems
(
   sessionId varchar(255) not null,
   beerId integer not null,
   quantity integer not null,
   primary key(sessionId, beerId),
   foreign key(beerId) references beerStocked(id)
);

create table OrderDetails
(
   id integer not null AUTO_INCREMENT,
   name varchar(255) not null,
   email varchar(255) not null,
   address varchar(255) not null,
   city varchar(255) not null,
   county varchar(255) not null,
   pcode varchar(255) not null,
   sessionId varchar(255) null,
   primary key(id)
);

create table BeerOrdered
(
   orderId integer not null,
   beerId integer not null,
   name varchar(255) not null,
   price decimal (10,2) not null,
   quantity integer not null,
   primary key(orderId, beerId),
   foreign key(orderId) references OrderDetails(id),
   foreign key(beerId) references beerStocked(id)
);