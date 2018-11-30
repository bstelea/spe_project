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
   description varchar(255),
   totalStock integer,
   availableStock integer,
   primary key(id)
);

create table shoppingCartItems
(
   id integer not null AUTO_INCREMENT,
   beerId integer not null,
   quantity integer not null,
   primary key(id),
   foreign key(beerId) references beerStocked(id)
);