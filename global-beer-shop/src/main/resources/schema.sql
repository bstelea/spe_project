create table beerStocked
(
   id integer not null,
   name varchar(255) not null,
   country varchar(255) not null,
   brewer varchar(255) not null,
   type varchar(255) not null,
   abv decimal(3,1) not null,
   prices decimal (10,2) null,
   image varchar(255),
   description varchar(255),
   totalStock integer,
   availableStock integer,
   primary key(id)
);